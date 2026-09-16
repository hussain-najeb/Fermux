package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.storage.DataStoreDownloaderSettings
import org.foss.fermux.storage.JSONHistoryCards


/**
 * The downloads worker for background, asynchronous work. This class handles most of the settings work for the [org.foss.fermux.settings.ui.downloader.SimpleDownloaderPage] page and [DownloaderSettingsTab] as well as handling the JSON history cards.
 */
class DownloadWorker(context: Context, params: WorkerParameters) :
     CoroutineWorker(context, params) {
     @RequiresApi(Build.VERSION_CODES.S)
     override suspend fun doWork(): Result {

          val taskId = id.toString()
          val workerJob = currentCoroutineContext().job

          DownloaderSettingsViewModel.DebugLog.debugDownloader("DownloadWorker", "Started id=$taskId attempt=$runAttemptCount")

          val settingsTab = DataStoreDownloaderSettings(applicationContext)
          val sponsorBlock = settingsTab.sponsorBlock.first()
          val showDetails = settingsTab.ytdlpDetails.first()
          val sponsorBlockCategories = settingsTab.sponsorBlockCategories.first()
          val sleepRequest = settingsTab.sleepRequest.first()
          val embedThumbnail = settingsTab.embedThumbnail.first()
          val playlistStatus = settingsTab.playlistStatus.first()
          val aria2cMode = settingsTab.aria2cMode.first()
          val externalDownloaders = settingsTab.externalDownloaders.first()
          val quickJS = settingsTab.quickJS.first()
          val fingerprinting = settingsTab.fingerprinting.first()



          val audioName = inputData.getString("audio")
          val videoName = inputData.getString("video")
          val audio = audioName?.let { AudioQuality.valueOf(it) }
          val video = videoName?.let { VideoQuality.valueOf(it) }
          val url = inputData.getString("url") ?: return Result.failure()
          val title = inputData.getString("title") ?: "unknown title"
          val thumbnail = inputData.getString("thumbnail") ?: "unknown thumbnail"
          val duration = inputData.getInt("duration", 0).toLong()
          val uploader = inputData.getString("uploader") ?: "unknown uploader"

          var lastProgressUpdateAt = 0L

          try {
               if (settingsTab.audioHistory.first() && audio != null) {
                    settingsTab.setJSONAudio(
                         JSONHistoryCards(
                              title,
                              thumbnail,
                              url,
                              uploader,
                              duration,
                              System.currentTimeMillis(),
                         )
                    )
               }

               if (settingsTab.videoHistory.first() && video != null) {
                    settingsTab.setJSONVideo(
                         JSONHistoryCards(
                              title,
                              thumbnail,
                              url,
                              uploader,
                              duration,
                              System.currentTimeMillis()
                         )
                    )
               }
          } catch (e: Exception) {
               DownloaderSettingsViewModel.DebugLog.errorDownloader("fermux", "failed to save audio JSON", e)
               DownloaderSettingsViewModel.DebugLog.errorDownloader("fermux", "failed to save video JSON", e)
          }

          return try {
               downloaderLogic(
                    context = applicationContext,
                    url = url,
                    taskId = taskId,
                    aria2cMode = aria2cMode,
                    externalDownloaders = externalDownloaders,
                    quickJs = quickJS,
                    fingerprinting = fingerprinting,
                    musicQuality = audio,
                    embedThumbnail = embedThumbnail,
                    playlistStatus = playlistStatus,
                    videoQuality = video,
                    showDetails = showDetails,
                    sponsorBlock = sponsorBlock,
                    sponsorBlockCategories = sponsorBlockCategories,
                    sleepRequest = sleepRequest,
                    onUpdate = { progress, line ->
                         if (isStopped || !workerJob.isActive) {
                              return@downloaderLogic
                         }

                         val now = System.currentTimeMillis()
                         val currentProgress = progress.coerceIn(0f, 100f)

                         if (now - lastProgressUpdateAt >= 500L) {
                              lastProgressUpdateAt = now
                              runBlocking {
                                   setProgress(
                                        workDataOf(
                                             "progress" to currentProgress,
                                             "text" to line
                                        )
                                   )
                              }
                         }
                    },
               )

               DownloaderSettingsViewModel.DebugLog.debugDownloader("DownloadWorker", "Succeeded id=$taskId")

               Result.success()

          } catch (e: CancellationException) {
               val destroyed = YoutubeDL.destroyProcessById(taskId)

               DownloaderSettingsViewModel.DebugLog.errorDownloader("DownloadWorker", "Cancelled id=$taskId stopReason=$stopReason destroyed=$destroyed", e)

               throw e
          } catch (e: Exception) {
               Log.d("DownloadWorker", "Failed id=$taskId attempt=$runAttemptCount", e)
               val error = e.message
                    ?.take(4_000)
                    ?: "Download failed"
               Result.failure(workDataOf("error" to error))
          }
     }
}
