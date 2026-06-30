package org.foss.fermux.ytdlp.logic

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.foss.fermux.settings.logic.SettingsTab

data class DownloadMetadata (
    val title: String,
    val thumbnail: String,
    val duration: Int,
    val uploader: String?, )

sealed class DownloadStatus {
    data object Idle : DownloadStatus() // no card in view. so it's idle and won't display a thing
    data object Loading : DownloadStatus() // loading the damn card.
    data class Loaded(val metadata: DownloadMetadata) : DownloadStatus() // the card is successful at loading
    data class Error(val errorMessage: String) : DownloadStatus() // when something goes wrong
    data class Downloading(val downloadProgress : Float, val metadata : DownloadMetadata) : DownloadStatus() // data for the fucking
// loadingIndicator
} // to extract the metadata. this classes job.


enum class AudioQuality (val musicQuality: String) // audio quality class to pass for ytdlp.
{
    BEST("0"),
    HIGH("192K"),
    MEDIUM("128k")
}

enum class VideoQuality(val videoQuality: String) { // the video quality flags to add for the ytdlp flags.
    BEST("bestvideo+bestaudio/best"),
    HD1080("bestvideo[height<=1080]+bestaudio/best"),
    HD720("bestvideo[height<=720]+bestaudio/best"),
    SD480("bestvideo[height<=480]+bestaudio/best"),
    Q360("bestvideo[height<=360]+bestaudio/best"),
}
// this one is to grab the "duration" from the metadata class and throws
// an actual time format to be displayed. so instead of "195" it's "3:15".




class DownloadWorker(context: Context, params: WorkerParameters ) :

    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val url   = inputData.getString("url") ?: return Result.failure()
        val audioName = inputData.getString("audio")
        val videoName = inputData.getString("video")

        val audio = audioName?.let { AudioQuality.valueOf(it) }
        val video = videoName?.let { VideoQuality.valueOf(it) }

        val settingsTab = SettingsTab(applicationContext)
        val showDetails = settingsTab.ytdlpDetails.first()

        return try {
            downloaderLogic( context = applicationContext, url = url , musicQuality = audio, videoQuality = video, showDetails = showDetails) { progress ->  runBlocking {
                setProgress(workDataOf("progress" to progress)) }
            }
            Result.success()
        } catch (e: Exception) {
            Log.d("downloadWorker", "download failed", e)
            Result.failure()
        }
    }
}