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
const val SEQUENTIAL_AUDIO = "audio" // TODO. Must add these later


class SequentialViewModel(
) : ViewModel() {

     var state by mutableStateOf<SequentialDownloadQueueState?>(null)
     private var currentMetadata: DownloadMetadata? = null
     private var showLog by mutableStateOf(false)
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
          state = SequentialDownloadQueueState(SequentialStatus.SequentiallyFetchingMetadata)
          currentMetadata = null
          val settingsTab = DataStoreDownloaderSettings(context.applicationContext)

          if (urls.isEmpty()) {
               state = SequentialDownloadQueueState(SequentialStatus.SequentialError)
               return
          }

          val requestedUrlBuilder = urls.map { url ->
               OneTimeWorkRequestBuilder<DownloadWorker>().addTag("destroy-all-monsters") // godzilla reference?
                    .setInputData(
                         workDataOf(
                              SEQUENTIAL_URL to url
                         )
                    ).setConstraints(wifiConstraint).build()
          }

          allIDs = requestedUrlBuilder.map { it.id }

          downloaderJob = viewModelScope.launch {
               try {
                    val ytdlpDetails = settingsTab.ytdlpDetails.first()
                    showLog = ytdlpDetails

                    val workManager = WorkManager.getInstance(context)

                    for ((index, request) in requestedUrlBuilder.withIndex()) {

                         if (request.id in skippedID) {
                              continue
                         }
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

                                        state = state?.copy(
                                             phase = SequentialStatus.SequentiallyDownloading,
                                             currentProgress = progress,
                                             currentMetadata = currentMetadata,
                                             logs = downloaderLogs,
                                             currentIndex = index,
                                             totalCount = urls.size
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

                                   val updatedComplete = if (metadata != null) {
                                        state?.completed.orEmpty() + metadata
                                   } else {
                                        state?.completed.orEmpty()
                                   }

                                   state = state?.copy(
                                        phase = if (index == urls.lastIndex) SequentialStatus.SequentiallyCompleted else SequentialStatus.SequentiallyDownloading,
                                        currentIndex = index,
                                        totalCount = urls.size,
                                        currentMetadata = metadata,
                                        completed = updatedComplete
                                   )
                                   currentMetadata = null
                              }


                              WorkInfo.State.FAILED -> {
                                   val error = finished.outputData.getString("error")
                                   error?.let {
                                        val failure = SequentialFailures(
                                             errorIndex = index,
                                             errorLog = it,
                                             url = urls[index]
                                        )

                                        state = state?.copy(
                                             failures = state?.failures.orEmpty() + failure
                                        )
                                   }
                                   currentMetadata = null
                                   continue
                              }

                              WorkInfo.State.CANCELLED -> {
                                   state = state?.copy(
                                        phase = SequentialStatus.SequentiallyDownloading,
                                        currentMetadata = null,
                                        currentProgress = 0f
                                   )
                                   currentMetadata = null
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
          currentMetadata = null
          state = state?.copy(phase = SequentialStatus.SequentialCancel)
     }

     fun singleProcessDestroyer(context: Context, index: Int) {
          val requestId = allIDs.getOrNull(index) ?: return
          skippedID += requestId
          WorkManager.getInstance(context).cancelWorkById(requestId)
     }

     fun hideDeleteDialog() {
          state = SequentialDownloadQueueState(
               showModalSheet = false
          )
     }

     fun showBottomSheet() {
          state = SequentialDownloadQueueState(
               showModalSheet = true
          )
     }


}