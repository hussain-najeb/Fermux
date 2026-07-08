package org.foss.fermux.ytdlp.logic

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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.foss.fermux.ytdlp.logic.downloader.AudioQuality
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloadWorker
import org.foss.fermux.ytdlp.logic.downloader.VideoQuality
import org.foss.fermux.ytdlp.logic.downloader.fetchingTheMetadata

class DownloaderViewModel : ViewModel() {
    var state by mutableStateOf<DownloadStatus>(DownloadStatus.Idle)
    var showFormatSheet by mutableStateOf(false)
    var downloadUrl by mutableStateOf("")
    var downloaderLogs by mutableStateOf("")


    fun fetchedMetadata(downloadUrl: String) {
        viewModelScope.launch {
            state = DownloadStatus.Loading
            try {
                val metadata = fetchingTheMetadata(downloadUrl)
                state = DownloadStatus.Loaded(metadata)
                showFormatSheet = true

            } catch (e: Exception) {
                Log.e("fermux", "Download failed for $downloadUrl", e)
                Log.d("fermux", "state failed to get stop the loading bar")
            }
        }
    }

    fun startingDownload(context: Context, audio: AudioQuality?, video: VideoQuality?) {

        val metadata = (state as? DownloadStatus.Loaded)?.metadata ?: return

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

        val workManager = WorkManager
            .getInstance(context)
        workManager.enqueue(requestedUrls)
        workManager.getWorkInfoByIdFlow(requestedUrls.id)
            .onEach { workInfo ->
                workInfo ?: return@onEach
                when (workInfo.state) {
                    WorkInfo.State.RUNNING -> {
                        val logs = workInfo.progress.getString("text")
                        val progress = workInfo.progress.getFloat("progress", 0f)
                        if (!logs.isNullOrBlank()) {
                            downloaderLogs = (downloaderLogs + logs).takeLast(200)
                        }
                        state = DownloadStatus.Downloading(progress, metadata)
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        state = DownloadStatus.Loaded(metadata)
                    }

                    WorkInfo.State.FAILED -> {
                        state = DownloadStatus.Error("Failed to download")
                    }

                    else -> {}
                }
            }
            .launchIn(viewModelScope)
    }
}