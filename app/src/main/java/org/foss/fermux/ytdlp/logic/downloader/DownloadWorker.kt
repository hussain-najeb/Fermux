package org.foss.fermux.ytdlp.logic.downloader

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.*
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import org.foss.fermux.R
import org.foss.fermux.dataStore.DataStoreDownloaderSettings
import org.foss.fermux.database.DownloaderDb
import org.foss.fermux.database.DownloadsDatabaseField
import org.foss.fermux.utils.DebugLogDownloader
import kotlin.math.roundToInt

/**
 * The downloads worker for background, asynchronous work.
 * This class handles most of the settings work for the [org.foss.fermux.settings.ui.downloader.SimpleDownloaderPage]
 * page and [DownloaderSettingsTab] as well as handling the JSON history cards.
 */

class DownloadWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

     private val downloaderWorkNotif: NotificationManager
          get() = applicationContext.getSystemService(
               Context.NOTIFICATION_SERVICE
          ) as NotificationManager

     private fun createDownloaderForegroundInfo(): ForegroundInfo {
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
          createDownloaderNotifChannel()

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
                    "Cancel Download",
                    canceller
               )
               .build()
     }

     private fun createDownloaderNotifChannel() {
          val channel = NotificationChannel(
               DOWNLOAD_CHANNEL_ID,
               "Downloader",
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
               createDownloaderForegroundInfo()
          )

          val taskId = id.toString()
          val workerJob = currentCoroutineContext().job

          DebugLogDownloader.debugDownloader("DownloadWorker", "Started id=$taskId attempt=$runAttemptCount")
          Log.d("DownloadWorker", "Started id=$taskId attempt=$runAttemptCount")

          val settings = DataStoreDownloaderSettings(applicationContext)
          val sponsorBlock = settings.sponsorBlock.first()
          val showDetails = settings.ytdlpDetails.first()
          val sponsorBlockCategories = settings.sponsorBlockCategories.first()
          val sleepRequest = settings.sleepRequest.first()
          val embedThumbnail = settings.embedThumbnail.first()
          val playlistStatus = settings.playlistStatus.first()
          val aria2cMode = settings.aria2cMode.first()
          val thumbnailFormat = settings.thumbnailFormat.first()
          val audioFormat = settings.audioFormat.first()
          val videoFormat = settings.videoFormat.first()
          val videoComp = settings.videoComp.first()
          val externalDownloaders = settings.externalDownloaders.first()
          val quickJS = settings.quickJS.first()
          val fingerprinting = settings.fingerprinting.first()
          val fragRetry = settings.fragRetries.first()
          val retries = settings.retries.first()
          val history = settings.history.first()
          val ipv = settings.ipvConnection.first()
          val quickAudio = settings.quickAudio.first()
          val quickVideo = settings.quickVideo.first()

          val audioName = inputData.getString("audio")
          val videoName = inputData.getString("video")
          val audio = audioName?.let { AudioQuality.valueOf(it) }
          val video = videoName?.let { VideoQuality.valueOf(it) }
          val url = inputData.getString("url") ?: return Result.failure()

          var lastProgressUpdateAt = 0L
          var capturedMetadataJson: String? = null

          return try {
               val downloaderInstance = downloaderLogic(
                    context = applicationContext,
                    url = url,
                    taskId = taskId,
                    aria2cMode = aria2cMode,
                    audioFormats = audioFormat,
                    videoFormats = videoFormat,
                    videoComp = videoComp,
                    thumbnail = thumbnailFormat,
                    externalDownloaders = externalDownloaders,
                    quickJs = quickJS,
                    fingerprinting = fingerprinting,
                    audioQuality = audio,
                    embedThumbnail = embedThumbnail,
                    playlistStatus = playlistStatus,
                    videoQuality = video,
                    ipv = ipv,
                    showDetails = showDetails,
                    sponsorBlock = sponsorBlock,
                    sponsorBlockCategories = sponsorBlockCategories,
                    fragRetry = fragRetry,
                    retries = retries,
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

                         if (now - lastProgressUpdateAt >= 200L) {
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

               DebugLogDownloader.debugDownloader("DownloadWorker", "Succeeded id=$taskId")
               Log.d("DownloadWorker", "Succeeded id=$taskId")

               val metadata = capturedMetadataJson?.let { parseYtdlpMetadataJson(it) }

               try {
                    if (metadata != null && history) {
                         val dao = DownloaderDb.getDatabase(applicationContext).downloadsDao
                         val instance = downloaderInstance.single()
                         val oldDownloaderInstance = dao.getSimilarInstance(metadata.extractor, metadata.mediaId)

                         try {
                              dao.upsertDownload(
                                   DownloadsDatabaseField(
                                        extractor = metadata.extractor,
                                        videoId = metadata.mediaId,
                                        fileUri = instance.uri.toString(),
                                        url = metadata.url,
                                        title = metadata.title,
                                        uploader = metadata.uploader,
                                        thumbnail = metadata.thumbnail,
                                        duration = metadata.duration,
                                        resolution = metadata.resolution,
                                        size = instance.sizeBytes,
                                        format = instance.extension
                                   )
                              )
                              oldDownloaderInstance?.let {
                                   applicationContext.contentResolver.delete(it.fileUri.toUri(), null, null)
                              }
                         } catch (e: Exception) {
                              Log.e("DownloadWorker", "Failed to save download to database", e)
                              DebugLogDownloader.errorDownloader("DownloadWorker", "Failed to save download to database", e)
                         }

                    }

               } catch (e: Exception) {
                    DebugLogDownloader.errorDownloader("fermux", "failed to save audio JSON", e)
                    DebugLogDownloader.errorDownloader("fermux", "failed to save video JSON", e)
               }

               capturedMetadataJson?.let {
                    Result.success(workDataOf("metadataJson" to it))
               } ?: Result.success()

          } catch (e: CancellationException) {
               val destroyed = YoutubeDL.destroyProcessById(taskId)
               DebugLogDownloader.errorDownloader("DownloadWorker", "Cancelled id=$taskId stopReason=$stopReason destroyed=$destroyed", e)
               throw e
          } catch (e: Exception) {
               DebugLogDownloader.errorDownloader("DownloadWorker", "Failed id=$taskId attempt=$runAttemptCount", e)
               val error = e.message
                    ?.take(9_000)
                    ?: "Download failed"
               Result.failure(workDataOf("error" to error))
          }
     }
}