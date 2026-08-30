package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.foss.fermux.storage.DownloaderSettingsTab
import java.net.UnknownHostException
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

/**
 * The downloader ViewModel used to manage state, cancel tasks, and to handle network and ytdlp related errors.
 */


class DownloaderViewModel : ViewModel() {
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
          Log.e("MetadataFetch", "Fetch failed: ${e.javaClass.simpleName}", e)
          val raw = when (e) {
               is TimeoutCancellationException -> "Timed out waiting for a response"
               else -> e.message ?: e.toString()
          }
          state = DownloadStatus.Error(flavorError.random(), raw)
     }

     /**
      * Used to handle The Downloader's states, settings, metadata, audio and video assignment, and for data to be assigned to [DownloadWorker] to make it work asynchronously and perform the downloading task.
      */
     fun startingDownload(context: Context, audio: AudioQuality?, video: VideoQuality?) {
          val settingsTab = DownloaderSettingsTab(context.applicationContext)
          val metadata = when (val current = state) {
               is DownloadStatus.MidChoice -> current.metadata
               is DownloadStatus.Loaded -> current.metadata
               else -> return
          }

          downloaderJob = viewModelScope.launch {
               val ytdlpDetails = settingsTab.ytdlpDetails.first()
               showYtdlpDetails = ytdlpDetails

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

               activeProcess = requestedUrls.id
               state = DownloadStatus.Downloading(0f, metadata)

               val workManager = WorkManager
                    .getInstance(context)
               workManager.enqueue(requestedUrls)
               workManager.getWorkInfoByIdFlow(requestedUrls.id)
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
                              }

                              else -> {}
                         }
                    }
                    .launchIn(viewModelScope)
          }
     }

     /**
      * Downloader cancel button to clear a process such as:
      *
      * * Handle Mid-download task that's unwanted
      * * Handle the reset process after an error
      * * Clear a successful process
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
