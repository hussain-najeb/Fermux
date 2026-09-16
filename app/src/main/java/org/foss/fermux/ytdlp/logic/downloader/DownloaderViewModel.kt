package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.storage.DataStoreDownloaderSettings
import java.net.UnknownHostException
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

/**
 * The downloader ViewModel used to manage state, cancel tasks, and to handle network and ytdlp related errors.
 */


class DownloaderViewModel : ViewModel() {
     companion object {
          private const val DOWNLOAD_WORK_NAME = "fermux-active-download"
     }

     var state by mutableStateOf<DownloadStatus>(DownloadStatus.Idle)
     var downloadUrl by mutableStateOf("")
     var downloaderLogs by mutableStateOf("")
     private var activeProcess by mutableStateOf<UUID?>(null)
     private var downloaderJob: Job? = null

     var showYtdlpDetails by mutableStateOf(false)
     val flavorError = listOf(
          "Oh, something must have gone wrong",
          "Could be a connection issue, check your internet connection",
          "Must have been a network issue",
          "What does an LLM say about it?",
          "Did you paste a URL?"
     )

     /**
      * Uses the url to start a metadata collection task, assign the correct state, and handling errors with [downloadErrorHandler].
      */
     fun fetchedMetadata(downloadUrl: String) {
          downloaderJob = viewModelScope.launch {
               state = DownloadStatus.Loading
               try {
                    val metadata = withTimeout(60000L.milliseconds) {
                         fetchingTheMetadata(downloadUrl)
                    }
                    state = DownloadStatus.MidChoice(metadata)
               } catch (e: UnknownHostException) {
                    downloadErrorHandler(e)
               } catch (e: TimeoutCancellationException) {
                    downloadErrorHandler(e)
               } catch (e: Exception) {
                    downloadErrorHandler(e)
               }
          }
     }

     /**
      * Used as a helper function for error handling.
      */
     private fun downloadErrorHandler(e: Exception) {

          DownloaderSettingsViewModel.DebugLog.errorDownloader("MetadataFetch", "Fetch failed: ${e.javaClass.simpleName}", e)

          val raw = when (e) {
               is TimeoutCancellationException -> "Timed out waiting for a response, retry the download"
               else -> e.message ?: e.toString()
          }
          state = DownloadStatus.Error(flavorError.random(), raw)
     }

     /**
      * Used to handle The Downloader's states, settings, metadata, audio and video assignment, and for data to be assigned to [DownloadWorker] to make it work asynchronously and perform the downloading task.
      */
     fun startingDownload(context: Context, audio: AudioQuality?, video: VideoQuality?) {
          if (activeProcess != null || state is DownloadStatus.Downloading) {

               DownloaderSettingsViewModel.DebugLog.debugDownloader("DownloadAdmission", "Ignoring duplicate download request; active id=$activeProcess")

               return
          }

          val settingsTab = DataStoreDownloaderSettings(context.applicationContext)
          val metadata = when (val current = state) {
               is DownloadStatus.MidChoice -> current.metadata
               is DownloadStatus.Loaded -> current.metadata
               else -> return
          }

          val requestedUrls = OneTimeWorkRequestBuilder<DownloadWorker>()
               .setInputData(
                    workDataOf(
                         "url" to downloadUrl,
                         "audio" to audio?.name,
                         "video" to video?.name,
                         "title" to metadata.title,
                         "thumbnail" to metadata.thumbnail,
                         "duration" to metadata.duration,
                         "uploader" to metadata.uploader
                    )
               )
               .build()

          // Close the tap race synchronously, before the coroutine's first suspension.
          activeProcess = requestedUrls.id
          state = DownloadStatus.Downloading(0f, metadata)

          DownloaderSettingsViewModel.DebugLog.debugDownloader("DownloadAdmission", "Prepared download id=${requestedUrls.id}")

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

                         DownloaderSettingsViewModel.DebugLog.debugDownloader("DownloadAdmission","Keeping existing download id=${existingWork.id} state=${existingWork.state}")

                         existingWork.id
                    } else {

                         DownloaderSettingsViewModel.DebugLog.debugDownloader("DownloadAdmission", "Enqueue unique download id=${requestedUrls.id}")

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
                                        val progress = workInfo.progress.getFloat("progress", 0f).coerceIn(0f, 100f)
                                        state = DownloadStatus.Downloading(progress, metadata)
                                   }

                                   WorkInfo.State.SUCCEEDED -> {
                                        state = DownloadStatus.Completed(metadata)
                                        activeProcess = null
                                   }

                                   WorkInfo.State.FAILED -> {
                                        val error = workInfo.outputData.getString("error")
                                        error?.let { state = DownloadStatus.Error(flavorError.random(), rawError = it) }
                                        activeProcess = null
                                   }

                                   WorkInfo.State.CANCELLED -> {
                                        state = DownloadStatus.Idle
                                        activeProcess = null
                                        downloaderLogs = ""
                                   }

                                   else -> {}
                              }
                         }
                         .launchIn(viewModelScope)
               } catch (e: CancellationException) {
                    throw e
               } catch (e: Exception) {
                    DownloaderSettingsViewModel.DebugLog.errorDownloader("DownloadAdmission", "Failed to enqueue id=${requestedUrls.id}", e)
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
     }
}
