package org.foss.fermux.ytdlp.logic.oneTimeDownloader

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.work.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import org.foss.fermux.R
import org.foss.fermux.dataStore.DataStoreDownloaderSettings
import org.foss.fermux.database.DownloaderDb
import org.foss.fermux.database.DownloadsDatabaseField
import org.foss.fermux.main.MainActivity
import org.foss.fermux.utils.CopiedFile
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SEQUENTIAL_URL
import org.foss.youtube.next.YoutubeDL
import kotlin.math.roundToInt

class DownloadWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

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

    val routeSetup = Intent(
        Intent.ACTION_VIEW,
        "fermux://downloader".toUri(),
        applicationContext,
        MainActivity::class.java
    ).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
    }

    val openRoute = PendingIntent.getActivity(
        applicationContext,
        id.hashCode(),
        routeSetup,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    private fun createFailureNotif(text: String): Notification {
        createDownloaderNotifChannel()

        return NotificationCompat.Builder(
            applicationContext,
            DOWNLOAD_CHANNEL_ID
        )
            .setSmallIcon(R.drawable.download_notif_cancel)
            .setContentTitle("Download failed, click to view")
            .setContentText(text.take(120))
            .setCategory(NotificationCompat.CATEGORY_ERROR)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOngoing(false)
            .setOnlyAlertOnce(true)
            .setContentIntent(openRoute)
            .build()
    }

    private fun showFailure(message: String) {
        val granted = Build.VERSION.SDK_INT < 33 ||
             ContextCompat.checkSelfPermission(
                 applicationContext,
                 Manifest.permission.POST_NOTIFICATIONS
             ) == PackageManager.PERMISSION_GRANTED

        if (granted) {
            NotificationManagerCompat.from(applicationContext)
                .notify(FAILURE_NOTIFICATION_ID, createFailureNotif(message))
        }
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
        private const val FAILURE_NOTIFICATION_ID = 1003
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createDownloaderForegroundInfo()
    }


    override suspend fun doWork(): Result {

        setForeground(
            createDownloaderForegroundInfo()
        )
        val taskId = id
        val workerJob = currentCoroutineContext().job

        Log.d("DownloadWorker", "Started id=$taskId attempt=$runAttemptCount")

        val settings = DataStoreDownloaderSettings(applicationContext)
        val sponsorBlock = settings.sponsorBlock.first()
        val showDetails = settings.ytdlpDetails.first()
        val sponsorBlockCategories = settings.sponsorBlockCategories.first()
        val sleepRequest = settings.sleepRequest.first()
        val embedThumbnail = settings.embedThumbnail.first()
        val playlistStatus = settings.playlistStatus.first()
        val aria2cMode = settings.aria2NextMode.first()
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
        val quickFormat = settings.quickFormat.first()
        val seqFormat = settings.seqFormat.first()
        val seqVideo = settings.seqVideo.first()
        val seqAudio = settings.seqAudio.first()

        val sequentialUrl = inputData.getString(SEQUENTIAL_URL)
        val url = sequentialUrl
            ?: inputData.getString(URL)
            ?: return Result.failure()
        val audioName = inputData.getString(AUDIO)
        val videoName = inputData.getString(VIDEO)
        val audio = audioName?.let(AudioQuality::valueOf)
        val video = videoName?.let(VideoQuality::valueOf)

        val isSequentialDownload = sequentialUrl != null
        val isQuickDownload =
            !isSequentialDownload && audio == null && video == null

        val selectedQuickAudio = quickAudio.takeIf {
            isQuickDownload &&
                 quickFormat == QuickDownloadFormats.QuickAudio
        }
        val selectedQuickVideo = quickVideo.takeIf {
            isQuickDownload &&
                 quickFormat == QuickDownloadFormats.QuickVideo
        }
        val selectedSeqVideo = seqVideo.takeIf {
            isSequentialDownload &&
                 seqFormat == SeqFormat.SeqVideo
        }
        val selectedSeqAudio = seqAudio.takeIf {
            isSequentialDownload &&
                 seqFormat == SeqFormat.SeqAudio
        }

        var lastProgressUpdateAt = 0L
        var capturedMetadataJson: String? = null

        return try {
            val downloaderInstance = downloaderLogic(
                context = applicationContext,
                url = url,
                taskId = taskId,
                aria2Mode = aria2cMode,
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
                quickDownloadFormats = quickFormat,
                quickAudioQuality = selectedQuickAudio,
                quickVideoQuality = selectedQuickVideo,
                seqFormat = seqFormat,
                seqVideo = selectedSeqVideo,
                seqAudio = selectedSeqAudio,
                onUpdate = { progress, line ->

                    if (isStopped || !workerJob.isActive) {
                        return@downloaderLogic
                    }

                    var justCapturedMetadata = false
                    if (capturedMetadataJson == null) {
                        val markerIndex = line.indexOf(FERMUX_METADATA_MARKER)
                        if (markerIndex != -1) {
                            capturedMetadataJson = line.substring(markerIndex + FERMUX_METADATA_MARKER.length).trim()
                            justCapturedMetadata = true
                        }
                    }

                    val now = System.currentTimeMillis()
                    val currentProgress = progress.coerceIn(0f, 100f)

                    if (justCapturedMetadata || now - lastProgressUpdateAt >= 200L) {
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
            Log.d("DownloadWorker", "Succeeded id=$taskId")

            historyRegistration(capturedMetadataJson, history, downloaderInstance)

            capturedMetadataJson?.let {
                Result.success(workDataOf("metadataJson" to it))
            } ?: Result.success()

        } catch (e: CancellationException) {
            val destroyed = YoutubeDL.destroyProcessById(taskId)
            Log.e("DownloadWorker", "Cancelled id=$taskId destroyed=$destroyed", e)
            throw e
        } catch (e: Exception) {
            Log.e("DownloadWorker", "Failed id=$taskId attempt=$runAttemptCount", e)
            val error = e.message
                ?.take(9_000)
                ?: "Download failed"
            showFailure(e.localizedMessage ?: "Unknown error")
            Result.failure(workDataOf("error" to error))
        }
    }
}

private suspend fun DownloadWorker.historyRegistration(
    capturedMetadataJson: String?,
    history: Boolean,
    downloaderInstance: List<CopiedFile>
) {
    val metadata = capturedMetadataJson?.let { parseYtdlpMetadataJson(it) }

    try {
        if (metadata != null && history) {
            val dao = DownloaderDb.getDatabase(applicationContext).downloadsDao
            val instance = downloaderInstance.firstOrNull() ?: return
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
            }
        }

    } catch (e: Exception) {
        Log.e("fermux", "failed to save video JSON", e)
    }
}
