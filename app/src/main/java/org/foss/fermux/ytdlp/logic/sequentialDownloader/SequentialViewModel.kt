package org.foss.fermux.ytdlp.logic.sequentialDownloader

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.foss.fermux.dataStore.DataStoreDownloaderSettings
import org.foss.fermux.utils.DebugLogDownloader
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.*
import java.util.*
import kotlin.coroutines.cancellation.CancellationException

const val SEQUENTIAL_URL = "seq_url"
const val SEQUENTIAL_VIDEO = "video"
const val SEQUENTIAL_AUDIO = "audio"


class SequentialViewModel(
) : ViewModel() {

    var state by mutableStateOf<SequentialDownloadQueueState?>(null)
    var showModalSheet by mutableStateOf(false)
        private set
    val downloadUrl = mutableStateListOf("")
    var downloaderLogs by mutableStateOf("")
    private var downloaderJob: Job? = null
    private var allIDs: List<UUID> = emptyList()
    private val skippedID = mutableSetOf<UUID>()
    var connection by mutableStateOf(Connectivity.Any)


    // This is atomic, btw
    val wifiConstraint = when (connection) {
        Connectivity.Wifi -> {
            Constraints.Builder().setRequiredNetworkType(NetworkType.UNMETERED).build()
        }

        Connectivity.Cellular -> {
            Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        }

        Connectivity.Any -> {
            Constraints.NONE
        }
    }


    fun startSequentiallyDownloading(
        context: Context,
        urls: List<String>
    ) {

        val requestedUrlBuilder = urls.map { url ->
            OneTimeWorkRequestBuilder<DownloadWorker>().addTag("destroy-all-monsters") // godzilla reference?
                .setInputData(
                    workDataOf(
                        SEQUENTIAL_URL to url
                    )
                ).setConstraints(wifiConstraint).build()
        }

        state = SequentialDownloadQueueState(
            phase = SequentialStatus.SequentiallyFetchingMetadata,
            items = requestedUrlBuilder.mapIndexed { index, request ->
                SequentialItem(
                    id = request.id,
                    url = urls[index]
                )
            }
        )
        val settingsTab = DataStoreDownloaderSettings(context.applicationContext)

        if (urls.isEmpty()) {
            state = SequentialDownloadQueueState(SequentialStatus.SequentialError)
            return
        }


        allIDs = requestedUrlBuilder.map { it.id }

        downloaderJob = viewModelScope.launch {
            try {
                val ytdlpDetails = settingsTab.ytdlpDetails.first()

                val workManager = WorkManager.getInstance(context)

                for ((index, request) in requestedUrlBuilder.withIndex()) {

                    if (request.id in skippedID) {
                        continue
                    }
                    var currentMetadata: DownloadMetadata? = null
                    workManager.enqueue(request)

                    val finished = workManager
                        .getWorkInfoByIdFlow(request.id)
                        .filterNotNull()
                        .onEach { workInfo ->
                            if (workInfo.state == WorkInfo.State.RUNNING) {
                                if (ytdlpDetails) {
                                    val logs = workInfo.progress.getString("text")
                                    if (!logs.isNullOrBlank()) {
                                        downloaderLogs = (downloaderLogs + logs)
                                    }
                                }
                                if (currentMetadata == null) {
                                    workInfo.progress.getString("metadataJson")?.let { json ->
                                        currentMetadata = parseYtdlpMetadataJson(json)
                                    }
                                }
                                val progress = workInfo.progress.getFloat("progress", 0f)

                                updateItem(request.id) { item ->
                                    item.copy(
                                        status = if (currentMetadata == null) {
                                            SequentialItemStatus.FetchingMetadata
                                        } else {
                                            SequentialItemStatus.Downloading
                                        },
                                        progress = progress,
                                        metadata = currentMetadata
                                    )
                                }


                                state = state?.copy(
                                    phase = if (currentMetadata == null) {
                                        SequentialStatus.SequentiallyFetchingMetadata
                                    } else {
                                        SequentialStatus.SequentiallyDownloading
                                    }
                                )
                            }
                        }
                        .first { workInfo ->
                            workInfo.state.isFinished
                        }

                    when (finished.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            val metadata = currentMetadata
                                ?: finished.outputData
                                    .getString("metadataJson")
                                    ?.let { parseYtdlpMetadataJson(it) }

                            state = state?.copy(
                                phase = if (index == urls.lastIndex) SequentialStatus.SequentiallyCompleted else SequentialStatus.SequentiallyDownloading
                            )

                            updateItem(request.id) { item ->
                                item.copy(
                                    status = SequentialItemStatus.Completed,
                                    progress = 100f,
                                    metadata = metadata
                                )
                            }
                        }


                        WorkInfo.State.FAILED -> {
                            val error = finished.outputData.getString("error")

                            updateItem(request.id) { item ->
                                item.copy(
                                    status = SequentialItemStatus.Failed,
                                    error = error ?: "Download failed"
                                )
                            }

                            continue
                        }

                        WorkInfo.State.CANCELLED -> {
                            updateItem(request.id) { item ->
                                item.copy(
                                    status = SequentialItemStatus.Cancelled
                                )
                            }

                            continue
                        }

                        else -> {}
                    }

                }
                state = state?.copy(phase = SequentialStatus.SequentiallyCompleted)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                DebugLogDownloader.errorDownloader(
                    "DownloadAdmission",
                    "Failed to enqueue id=${requestedUrlBuilder}",
                    e
                )
                state = state?.copy(phase = SequentialStatus.SequentialError)
            } finally {
                downloaderLogs = ""
            }

        }
    }

    fun allDestroyer(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("destroy-all-monsters")
        downloaderLogs = ""
        downloaderJob?.cancel()
        state = state?.copy(phase = SequentialStatus.SequentialCancel)
    }

    fun singleProcessDestroyer(context: Context, index: Int) {
        val requestId = allIDs.getOrNull(index) ?: return
        skippedID += requestId
        WorkManager.getInstance(context).cancelWorkById(requestId)
    }

    fun hideDeleteDialog() {
        showModalSheet = false
    }

    fun showBottomSheet() {
        showModalSheet = true
    }

    private fun updateItem(
        id: UUID,
        update: (SequentialItem) -> SequentialItem
    ) {
        val currentState = state ?: return

        state = currentState.copy(
            items = currentState.items.map { item ->
                if (item.id == id) update(item) else item
            }
        )
    }

}
