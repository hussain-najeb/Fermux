package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.foss.fermux.storage.DataStoreDownloaderSettings
import org.foss.fermux.utils.DebugLogDownloader
import java.util.*


class DownloaderViewModel : ViewModel() {
     companion object {
          private const val DOWNLOAD_WORK_NAME = "fermux-active-download"
     }

     var state by mutableStateOf<DownloadStatus>(DownloadStatus.Idle)
     var connection by mutableStateOf(Connectivity.Any)
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

     val wifiConstraint = when (connection) {
          Connectivity.Wifi -> {
               Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.UNMETERED)
                    .build()
          }
          Connectivity.Cellular -> {
               Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
          }
          Connectivity.Any -> {
               Constraints.NONE
          }
     }

     fun userPickedArgs() {
          if (downloadUrl.isBlank()) return
          state = DownloadStatus.UserArgs
     }

     fun quickDownloads() {
          if (downloadUrl.isBlank()) return
          state = DownloadStatus.QuickDownload
     }

     private fun downloadErrorHandler(e: Exception) {

          DebugLogDownloader.errorDownloader("MetadataFetch", "Fetch failed: ${e.javaClass.simpleName}", e)

          val raw = when (e) {
               is TimeoutCancellationException -> "Timed out waiting for a response, retry the download"
               else -> e.message ?: e.toString()
          }
          state = DownloadStatus.Error(flavorError.random(), raw)
     }

     fun startingDownload(context: Context, audio: AudioQuality? = null, video: VideoQuality? = null) {
          if (activeProcess != null || state !is DownloadStatus.UserArgs && state !is DownloadStatus.QuickDownload) {

               DebugLogDownloader.debugDownloader(
                    "DownloadAdmission",
                    "Ignoring duplicate download request; active id=$activeProcess"
               )

               return
          }
          state = DownloadStatus.LoadingMetadata
          currentMetadata = null

          val settingsTab = DataStoreDownloaderSettings(context.applicationContext)
          val requestedUrls = OneTimeWorkRequestBuilder<DownloadWorker>()
               .setInputData(
                    workDataOf(
                         "url" to downloadUrl,
                         "audio" to audio?.name,
                         "video" to video?.name
                    )
               )
               .setConstraints(wifiConstraint)
               .build()

          activeProcess = requestedUrls.id

          DebugLogDownloader.debugDownloader("DownloadAdmission", "Prepared download id=${requestedUrls.id}")

          downloaderJob = viewModelScope.launch {
               try {
                    val ytdlpDetails = settingsTab.ytdlpDetails.first()
                    showYtdlpDetails = ytdlpDetails

                    val workManager = WorkManager.getInstance(context)
                    val existingWork = workManager
                         .getWorkInfosForUniqueWorkFlow(DOWNLOAD_WORK_NAME)
                         .first()
                         .firstOrNull { !it.state.isFinished }
                    val observedId = if (existingWork != null) {
                         activeProcess = existingWork.id

                         DebugLogDownloader.debugDownloader(
                              "DownloadAdmission",
                              "Keeping existing download id=${existingWork.id} state=${existingWork.state}"
                         )

                         existingWork.id
                    } else {

                         DebugLogDownloader.debugDownloader(
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
                         .onEach { workInfo ->
                              workInfo ?: return@onEach
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
                                                  title = "Download complete",
                                                  thumbnail = "",
                                                  duration = 0,
                                                  uploader = null,
                                                  audioFormat = null,
                                                  audioQuality = null,
                                                  size = null,
                                                  resolution = null
                                             )
                                        )
                                        activeProcess = null
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
                         .launchIn(viewModelScope)


               } catch (e: CancellationException) {
                    throw e
               } catch (e: Exception) {
                    DebugLogDownloader.errorDownloader(
                         "DownloadAdmission",
                         "Failed to enqueue id=${requestedUrls.id}",
                         e
                    )
                    if (activeProcess == requestedUrls.id) {
                         activeProcess = null
                         downloadErrorHandler(e)
                    }
               }
          }
     }

     /**
      * Downloader cancel button to clear a process such as:
      *
      * * Handle Mid-download task that's unwanted
      * * Handle the reset process after an error
      * * Clear a successful process, after extensive editing to the main wrapper, the (YoutubeDL) class.
      * * Kills all process's that were triggered by the user, libs like ffmpeg, yt-dlp, aria2, and quickJs as well.
      *
      */
     fun cancelButton(context: Context) {
          activeProcess?.let { id ->
               YoutubeDL.destroyProcessById(id.toString())
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
