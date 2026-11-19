package org.foss.fermux.ytdlp.logic.sequentialDownloader

import android.content.Context
import android.util.Log
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
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.*
import java.util.*
import kotlin.coroutines.cancellation.CancellationException

const val SEQUENTIAL_URL = "seq_url"


class SequentialViewModel(
) : ViewModel() {

    var state by mutableStateOf(SequentialDownloadQueueState(phase = SequentialStatus.SeqIdle))
    var selectedLogItemId by mutableStateOf<UUID?>(null)
    var showModalSheet by mutableStateOf(false)
        private set
    var downloadUrl = mutableStateListOf(SequentialItem())
    private var downloaderJob: Job? = null
    private var allIDs: List<UUID> = emptyList()
    private val skippedID = mutableSetOf<UUID>()

    fun startSequentiallyDownloading(
        context: Context,
        urls: List<String>
    ) {
        if (urls.isEmpty()) return

        val settingsTab = DataStoreDownloaderSettings(context.applicationContext)
        selectedLogItemId = null
        state = SequentialDownloadQueueState(phase = SequentialStatus.SeqDownload)

        downloaderJob?.cancel()
        downloaderJob = viewModelScope.launch {
            var requestedUrlBuilder: List<OneTimeWorkRequest> = emptyList()
            try {
                val ytdlpDetails = settingsTab.ytdlpDetails.first()
                val wifiConstraint = settingsTab.wifi.first().toWorkConstraints()

                requestedUrlBuilder = urls.map { url ->
                    OneTimeWorkRequestBuilder<DownloadWorker>().addTag("destroy-all-workers")
                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .setInputData(
                            workDataOf(
                                SEQUENTIAL_URL to url
                            )
                        ).setConstraints(wifiConstraint).build()
                }

                state = SequentialDownloadQueueState(
                    phase = SequentialStatus.SeqDownload,
                    items = requestedUrlBuilder.mapIndexed { index, request ->
                        SequentialItem(
                            id = request.id, url = urls[index]
                        )
                    }
                )
                allIDs = requestedUrlBuilder.map { it.id }
                skippedID.clear()

                val workManager = WorkManager.getInstance(context)
                for (request in requestedUrlBuilder) {
                    if (request.id in skippedID) {
                        continue
                    }
                    var currentMetadata: DownloadMetadata? = null
                    var lastLogChunk: String? = null
                    workManager.enqueue(request)

                    val finished = workManager.getWorkInfoByIdFlow(request.id).filterNotNull().onEach { workInfo ->
                        if (workInfo.state == WorkInfo.State.RUNNING) {
                            if (currentMetadata == null) {
                                workInfo.progress.getString("metadataJson")?.let { json ->
                                    currentMetadata = parseYtdlpMetadataJson(json)
                                }
                            }
                            val progress = workInfo.progress.getFloat("progress", 0f)
                            val logChunk = workInfo.progress.getString("text")
                            val newLogChunk = logChunk?.takeIf {
                                ytdlpDetails && it.isNotBlank() && it != lastLogChunk
                            }
                            if (newLogChunk != null) {
                                lastLogChunk = newLogChunk
                            }

                            updateItem(request.id) { item ->
                                item.copy(
                                    status = if (currentMetadata == null) {
                                        SequentialItemStatus.FetchingMetadata
                                    } else {
                                        SequentialItemStatus.Downloading
                                    },
                                    progress = progress,
                                    metadata = currentMetadata,
                                    logs = when {
                                        newLogChunk == null -> item.logs
                                        item.logs.isEmpty() -> newLogChunk
                                        else -> "${item.logs}\n$newLogChunk"
                                    }

                                )
                            }
                        }
                    }.first { workInfo ->
                        workInfo.state.isFinished
                    }

                    when (finished.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            val metadata = currentMetadata ?: finished.outputData.getString("metadataJson")
                                ?.let { parseYtdlpMetadataJson(it) }
                            updateItem(request.id) { item ->
                                item.copy(
                                    status = SequentialItemStatus.Completed,
                                    progress = 100f,
                                    metadata = metadata
                                )
                            }
                        }

                        WorkInfo.State.FAILED -> {
                            val error = finished.outputData.getString("error") ?: "Download failed"

                            updateItem(request.id) { item ->
                                val updatedLogs = when {
                                    error in item.logs -> item.logs
                                    item.logs.isBlank() -> error
                                    else -> "${item.logs}\n\n$error"
                                }
                                item.copy(
                                    status = SequentialItemStatus.Failed,
                                    error = error,
                                    logs = updatedLogs
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
                state = state.copy(phase = SequentialStatus.SeqDone)

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("DownloadAdmission", "Failed to enqueue id=${requestedUrlBuilder}", e)
                state = state.copy(phase = SequentialStatus.SeqDone)
            }
        }
    }

    fun allDestroyer(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("destroy-all-workers")
        downloaderJob?.cancel()
        downloadUrl.clear()
        downloadUrl.add(SequentialItem())
        skippedID.clear()
        selectedLogItemId = null
        state = SequentialDownloadQueueState(phase = SequentialStatus.SeqIdle)
    }

    fun singleProcessDestroyer(context: Context, index: Int) {
        val requestId = allIDs.getOrNull(index) ?: return
        skippedID += requestId
        updateItem(requestId) { item ->
            item.copy(status = SequentialItemStatus.Cancelled)
        }
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
        val currentState = state

        state = currentState.copy(
            items = currentState.items.map { item ->
                if (item.id == id) update(item) else item
            }
        )
    }
}
