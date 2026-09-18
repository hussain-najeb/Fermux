package org.foss.fermux.ytdlp.logic.downloader

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import org.foss.fermux.R
import org.foss.fermux.storage.DataStoreDownloaderSettings
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.utils.DebugLog
import kotlin.math.roundToInt

/**
 * The downloads worker for background, asynchronous work.
 * This class handles most of the settings work for the [org.foss.fermux.settings.ui.downloader.SimpleDownloaderPage]
 * page and [DownloaderSettingsTab] as well as handling the JSON history cards.
 */

class DownloadWorker(context: Context, params: WorkerParameters) :
     CoroutineWorker(context, params) {

     private val downloaderWorkNotif: NotificationManager
          get() = applicationContext.getSystemService(
               Context.NOTIFICATION_SERVICE
          ) as NotificationManager

     private fun createForegroundInfo(): ForegroundInfo {
          return ForegroundInfo(
               DOWNLOAD_NOTIFICATION_ID,
               createDownloadNotif(
                    progress = null,
                    text = "Preparing Download..."
               ),
               ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
          )
     }

     private fun createDownloadNotif(
          progress: Int?,
          text: String
     ): Notification {
          createNotifChannel()

          val canceller = WorkManager
               .getInstance(applicationContext)
               .createCancelPendingIntent(id)

          return NotificationCompat.Builder(
               applicationContext,
               DOWNLOAD_CHANNEL_ID
          )
               .setSmallIcon(R.drawable.download_notif)
               .setContentTitle("Downloading...")
               .setContentText(text.take(120))
               .setProgress(
                    100,
                    progress ?: 0,
                    progress == null
               )
               .setOnlyAlertOnce(true)
               .setOngoing(true)
               .addAction(
                    R.drawable.download_notif_cancel,
                    "Cancel",
                    canceller
               )
               .build()
     }

     private fun createNotifChannel() {
          val channel = NotificationChannel(
               DOWNLOAD_CHANNEL_ID,
               "downloads",
               NotificationManager.IMPORTANCE_DEFAULT
          ).apply {
               description = "Shows current downloads"
          }
          downloaderWorkNotif.createNotificationChannel(channel)
     }

     companion object {
          private const val DOWNLOAD_CHANNEL_ID = "Downloader_Notifs"
          private const val DOWNLOAD_NOTIFICATION_ID = 1001
     }

     @RequiresApi(Build.VERSION_CODES.S)
     override suspend fun doWork(): Result {


          setForeground(
               createForegroundInfo()
          )

          val taskId = id.toString()
          val workerJob = currentCoroutineContext().job

          DebugLog.debugDownloader("DownloadWorker", "Started id=$taskId attempt=$runAttemptCount")

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
          var capturedMetadataJson: String? = null

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

                         if (capturedMetadataJson == null) {
                              val markerIndex = line.indexOf(FERMUX_METADATA_MARKER)
                              if (markerIndex != -1) {
                                   capturedMetadataJson = line.substring(markerIndex + FERMUX_METADATA_MARKER.length).trim()
                              }
                         }

                         val now = System.currentTimeMillis()
                         val currentProgress = progress.coerceIn(0f, 100f)

                         if (now - lastProgressUpdateAt >= 500L) {
                              lastProgressUpdateAt = now
                              runBlocking {
                                   setProgress(
                                        workDataOf(
                                             "progress" to currentProgress,
                                             "text" to line,
                                             "metadataJson" to capturedMetadataJson
                                        )
                                   )
                              }
                              downloaderWorkNotif.notify(
                                   DOWNLOAD_NOTIFICATION_ID,
                                   createDownloadNotif(
                                        progress = currentProgress.roundToInt(),
                                        text = line
                                   )
                              )
                         }
                    },
               )

               DebugLog.debugDownloader("DownloadWorker", "Succeeded id=$taskId")

               val metadata = capturedMetadataJson?.let { parseYtdlpMetadataJson(it) }
               val historyTitle = metadata?.title ?: title
               val historyThumbnail = metadata?.thumbnail ?: thumbnail
               val historyDuration = metadata?.duration?.toLong() ?: duration
               val historyUploader = metadata?.uploader ?: uploader

               try {
                    if (settingsTab.audioHistory.first() && audio != null) {
                         settingsTab.setJSONAudio(
                              JSONHistoryCards(
                                   historyTitle,
                                   historyThumbnail,
                                   url,
                                   historyUploader,
                                   historyDuration,
                                   System.currentTimeMillis(),
                              )
                         )
                    }

                    if (settingsTab.videoHistory.first() && video != null) {
                         settingsTab.setJSONVideo(
                              JSONHistoryCards(
                                   historyTitle,
                                   historyThumbnail,
                                   url,
                                   historyUploader,
                                   historyDuration,
                                   System.currentTimeMillis()
                              )
                         )
                    }
               } catch (e: Exception) {
                    DebugLog.errorDownloader("fermux", "failed to save audio JSON", e)
                    DebugLog.errorDownloader("fermux", "failed to save video JSON", e)
               }

               capturedMetadataJson?.let {
                    Result.success(workDataOf("metadataJson" to it))
               } ?: Result.success()

          } catch (e: CancellationException) {
               val destroyed = YoutubeDL.destroyProcessById(taskId)

               DebugLog.errorDownloader("DownloadWorker", "Cancelled id=$taskId stopReason=$stopReason destroyed=$destroyed", e)

               throw e
          } catch (e: Exception) {
               DebugLog.errorDownloader("DownloadWorker", "Failed id=$taskId attempt=$runAttemptCount", e)
               val error = e.message
                    ?.take(4_000)
                    ?: "Download failed"
               Result.failure(workDataOf("error" to error))
          }
     }
}
