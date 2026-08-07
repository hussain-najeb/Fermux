package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.storage.SettingsTab

class DownloadWorker(context: Context, params: WorkerParameters ) :
     CoroutineWorker(context, params) {
     override suspend fun doWork(): Result {

          val settingsTab = SettingsTab(applicationContext)
          val sponsorBlock = settingsTab.sponsorBlock.first()
          val sponsorBlockCategories = settingsTab.sponsorBlockCategories.first()
          val aria2c = settingsTab.aria2c.first()
          val aria2cHLSWithDASHCase = settingsTab.aria2cHLSWithDASHCase.first()
          val sleepRequest = settingsTab.sleepRequest.first()

          val audioName = inputData.getString("audio")
          val videoName = inputData.getString("video")
          val audio = audioName?.let { AudioQuality.valueOf(it) }
          val video = videoName?.let { VideoQuality.valueOf(it) }

          val url   = inputData.getString("url") ?: return Result.failure()
          val title = inputData.getString("title") ?: "unknown title"
          val thumbnail = inputData.getString("thumbnail") ?: "unknown thumbnail"
          val duration = inputData.getInt("duration", 0).toLong()
          val uploader = inputData.getString("uploader")

          val showDetails = settingsTab.ytdlpDetails.first()
          var currentProgress = 0f
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
               Log.d("fermux", "failed to save audio JSON", e)
               Log.d("fermux", "failed to save video JSON", e)
          }

          return try {
               downloaderLogic(
                    context = applicationContext,
                    url = url,
                    taskId = id.toString(),
                    musicQuality = audio,
                    videoQuality = video,
                    showDetails = showDetails,
                    sponsorBlock = sponsorBlock,
                    sponsorBlockCategories = sponsorBlockCategories,
                    aria2c = aria2c,
                    aria2cHLSWithDASHCase = aria2cHLSWithDASHCase,
                    sleepRequest = sleepRequest,
                    onUpdate = { progress, line ->
                         val now = System.currentTimeMillis()
                         currentProgress = progress.coerceIn(0f, 100f)

                         // WorkManager progress is persisted in its database. Do not write
                         // once for the progress callback and again for the log callback.
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
               Result.success()
          } catch (e: Exception) {
               Log.d("downloadWorker", "download failed", e)
               Result.failure(workDataOf("error" to (e.message ?: e.toString())))
          }
     }
}
