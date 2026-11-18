package org.foss.fermux.ytdlp.logic.oneTimeDownloader

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.foss.fermux.dataStore.DataStoreDownloaderSettings
import org.foss.youtube.next.YoutubeDL
import java.util.*


const val VIDEO = "video"
const val AUDIO = "audio"
const val URL = "url"

fun Connectivity.toWorkConstraints(): Constraints = when (this) {
    Connectivity.Wifi -> Constraints.Builder()
        .setRequiredNetworkType(NetworkType.UNMETERED)
        .build()

    Connectivity.Cellular -> Constraints.Builder()
        .setRequiredNetworkType(NetworkType.METERED)
        .build()

    Connectivity.Any -> Constraints.NONE
}

class DownloaderViewModel : ViewModel() {
    companion object {
        private const val DOWNLOAD_WORK_NAME = "fermux_actived_download"
    }

    var state by mutableStateOf<DownloadStatus>(DownloadStatus.Idle)
    var downloadUrl by mutableStateOf("")
    var downloaderLogs by mutableStateOf("")
    private var activeProcess by mutableStateOf<UUID?>(null)
    private var downloaderJob: Job? = null
    private var currentMetadata: DownloadMetadata? = null

    var showYtdlpDetails by mutableStateOf(false)
    val flavorError = listOf(
        "Oh, something must have gone wrong",
        "Could be a connection issue, check your internet connection",
        "Must have been a network issue",
        "What does an LLM say about it?",
        "Did you paste a URL?"
    )

    fun userPickedArgs() {
        if (downloadUrl.isBlank()) return
        state = DownloadStatus.UserArgs
    }

    fun quickDownloads() {
        if (downloadUrl.isBlank()) return
        state = DownloadStatus.QuickDownload
    }

    fun startingDownload(
        context: Context,
        audio: AudioQuality? = null,
        video: VideoQuality? = null
    ) {
        if (activeProcess != null || state !is DownloadStatus.UserArgs && state !is DownloadStatus.QuickDownload) {
            Log.d("DownloadAdmission", "Ignoring duplicate download request, active id=$activeProcess")
            return
        }

        state = DownloadStatus.LoadingMetadata
        currentMetadata = null

        val settingsTab = DataStoreDownloaderSettings(context.applicationContext)

        downloaderJob?.cancel()
        downloaderJob = viewModelScope.launch {
            var requestedId: UUID? = null
            try {
                val ytdlpDetails = settingsTab.ytdlpDetails.first()
                showYtdlpDetails = ytdlpDetails
                val wifiConstraint = settingsTab.wifi.first().toWorkConstraints()

                val requestedUrls = OneTimeWorkRequestBuilder<DownloadWorker>()
                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .setInputData(
                        workDataOf(
                            URL to downloadUrl,
                            AUDIO to audio?.name,
                            VIDEO to video?.name
                        )
                    )
                    .setConstraints(wifiConstraint)
                    .build()

                requestedId = requestedUrls.id
                activeProcess = requestedUrls.id

                Log.d("DownloadAdmission", "Prepared download id=${requestedUrls.id}")

                val workManager = WorkManager.getInstance(context)
                val existingWork = workManager
                    .getWorkInfosForUniqueWorkFlow(DOWNLOAD_WORK_NAME)
                    .first()
                    .firstOrNull { !it.state.isFinished }
                val observedId = if (existingWork != null) {
                    activeProcess = existingWork.id

                    Log.d(
                        "DownloadAdmission",
                        "Keeping existing download id=${existingWork.id} state=${existingWork.state}"
                    )

                    existingWork.id
                } else {

                    Log.d(
                        "DownloadAdmission",
                        "Enqueue unique download id=${requestedUrls.id}"
                    )

                    workManager.enqueueUniqueWork(
                        DOWNLOAD_WORK_NAME,
                        ExistingWorkPolicy.KEEP,
                        requestedUrls
                    )
                    requestedUrls.id
                }
                workManager.getWorkInfoByIdFlow(observedId)
                    .filterNotNull()
                    .onEach { workInfo ->
                        when (workInfo.state) {
                            WorkInfo.State.RUNNING -> {
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

                                state = DownloadStatus.Downloading(
                                    downloadProgress = progress,
                                    metadata = currentMetadata
                                )
                            }

                            WorkInfo.State.SUCCEEDED -> {
                                val metadata = currentMetadata
                                    ?: workInfo.outputData
                                        .getString("metadataJson")
                                        ?.let { parseYtdlpMetadataJson(it) }

                                state = DownloadStatus.Completed(
                                    metadata ?: DownloadMetadata(
                                        url = "",
                                        mediaId = "",
                                        extractor = "",
                                        title = "",
                                        thumbnail = null,
                                        duration = 0,
                                        uploader = null,
                                        audioQuality = null,
                                        size = null,
                                        format = "",
                                        resolution = ""
                                    )
                                )
                                activeProcess = null
                                downloaderLogs = ""
                            }

                            WorkInfo.State.FAILED -> {
                                val error = workInfo.outputData.getString("error")
                                error?.let { state = DownloadStatus.Error(flavorError.random(), rawError = it) }
                                activeProcess = null
                                currentMetadata = null
                            }

                            WorkInfo.State.CANCELLED -> {
                                state = DownloadStatus.Idle
                                activeProcess = null
                                downloaderLogs = ""
                                currentMetadata = null
                            }

                            else -> {}
                        }
                    }
                    .first { it.state.isFinished }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e(
                    "DownloadAdmission",
                    "Failed to enqueue id=$requestedId",
                    e
                )
                if (requestedId == null || activeProcess == requestedId) {
                    activeProcess = null
                    downloadErrorHandler(e)
                }
            }
        }
    }


    private fun downloadErrorHandler(e: Exception) {
        Log.e("MetadataFetch", "Fetch failed: ${e.javaClass.simpleName}", e)
        val raw = when (e) {
            is TimeoutCancellationException -> "Timed out waiting for a response, check network connection"
            else -> e.message ?: e.toString()
        }
        state = DownloadStatus.Error(flavorError.random(), raw)
    }

    /**
     * Downloader cancel button to clear a process such as:
     * * Handle Mid-download task that's unwanted
     * * Handle the reset process after an error
     * * Clear a successful process.
     * * Kills all processes that were triggered by the user, such as ffmpeg, yt-dlp, aria2, and quickJs.
     */
    fun cancelButton(context: Context) {
        activeProcess?.let { id ->
            YoutubeDL.destroyProcessById(id)
            WorkManager.getInstance(context).cancelWorkById(id)
        }
        downloaderJob?.cancel()
        downloaderJob = null
        state = DownloadStatus.Idle
        downloadUrl = ""
        downloaderLogs = ""
        activeProcess = null
        currentMetadata = null
    }
}
