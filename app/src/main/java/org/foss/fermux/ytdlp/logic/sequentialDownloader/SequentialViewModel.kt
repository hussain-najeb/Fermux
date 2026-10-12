package org.foss.fermux.ytdlp.logic.sequentialDownloader

import android.content.Context
import androidx.collection.mutableIntListOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.foss.fermux.dataStore.DataStoreDownloaderSettings
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.*
import kotlin.coroutines.cancellation.CancellationException

const val SEQUENTIAL_URL = "url"
const val SEQUENTIAL_VIDEO = "video"
const val SEQUENTIAL_AUDIO = "audio" // TODO. Must add these later


class SequentialViewModel() : ViewModel() {

     var state by mutableStateOf<SequentialDownloadQueueState?>(null)
     private var currentMetadata: DownloadMetadata? = null
     private var showLog by mutableStateOf(false)
     var downloaderLogs by mutableStateOf("")
     var currentIndex = mutableIntListOf(0)
     private var downloaderJob: Job? = null
     var connection by mutableStateOf(Connectivity.Any)


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


     fun startSequentiallyDownloading(
          context: Context,
          urls: List<String>
     ) {
          state = SequentialDownloadQueueState(Phase.SequentiallyIdle)
          currentMetadata = null
          val settingsTab = DataStoreDownloaderSettings(context.applicationContext)


          val requestedUrlBuilder = urls.map { url ->
               OneTimeWorkRequestBuilder<DownloadWorker>()
                    .setInputData(
                         workDataOf(
                              SEQUENTIAL_URL to url
                         )
                    )
                    .setConstraints(wifiConstraint)
                    .build()
          }

          downloaderJob = viewModelScope.launch {
               try {
                    val ytdlpDetails = settingsTab.ytdlpDetails.first()
                    showLog = ytdlpDetails

                    val workManager = WorkManager.getInstance(context)
                    var chain = workManager
                         .beginWith(requestedUrlBuilder.first())
                    requestedUrlBuilder.drop(1).forEach {
                         chain = chain.then(it)
                    }
                    chain.enqueue()


                    requestedUrlBuilder.forEachIndexed { index, request ->
                         workManager.getWorkInfoByIdFlow(request.id)
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
                                             state = state?.copy(
                                                  currentIndex = index,
                                                  totalCount = urls.size,
                                                  phase = Phase.SequentiallyDownloading,
                                                  logs = downloaderLogs,
                                                  currentMetadata = currentMetadata,
                                                  currentProgress = progress
                                             )
                                        }

                                        WorkInfo.State.SUCCEEDED -> {
                                             val metadata = currentMetadata
                                                  ?: workInfo.outputData
                                                       .getString("metadataJson")
                                                       ?.let { parseYtdlpMetadataJson(it) }

                                             state = state?.copy(
                                                  phase = Phase.SequentiallyDownloading,
                                                  currentIndex = index + 1,
                                                  totalCount = index,
                                                  currentMetadata = metadata,
                                                  completed = listOf(
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
                                                  ),
                                             )
                                             currentMetadata = null
                                        }

                                        WorkInfo.State.FAILED -> {
                                             val error = workInfo.outputData.getString("error")
                                             error?.let {

                                             }
                                             currentMetadata = null
                                        }

                                        WorkInfo.State.CANCELLED -> {
                                             state = SequentialDownloadQueueState(
                                                  phase = Phase.SequentialCancel,
                                             )
                                             downloaderLogs = ""
                                             currentMetadata = null
                                        }

//                                        WorkInfo.State.PAUSED???? // TODO. Should probably add this

                                        else -> {}
                                   }
                              }
                              .launchIn(viewModelScope)
                    }

               } catch (e: CancellationException) {
                    throw e
               } finally {
                    downloaderLogs = ""
               }
          }
     }
}