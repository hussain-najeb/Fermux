package org.foss.fermux.ytdlp.logic

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import coil3.util.CoilUtils.result
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.foss.fermux.storage.SettingsTab
import org.foss.fermux.ytdlp.logic.downloader.AudioQuality
import org.foss.fermux.ytdlp.logic.downloader.DownloadMetadata
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloadWorker
import org.foss.fermux.ytdlp.logic.downloader.VideoQuality
import org.foss.fermux.ytdlp.logic.downloader.fetchingTheMetadata
import java.net.UnknownHostException
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds

class DownloaderViewModel : ViewModel() {
    var state by mutableStateOf<DownloadStatus>(DownloadStatus.Idle)
    var showFormatSheet by mutableStateOf(false)
    var downloadUrl by mutableStateOf("")
    var downloaderLogs by mutableStateOf("")
    private var activeProcess by mutableStateOf<UUID?>(null)
    val flavorError =
        listOf<String>(
            "Dammit, something must have went REALLY wrong",
            "Could be a connection issue, check your internet connection",
            "You guessed it, it's a network error",
            "Chuck the error log in an LLM and watch the magic happen",
            "Did you paste a URL?"
        )



    fun fetchedMetadata(downloadUrl: String) {
        viewModelScope.launch {
            state = DownloadStatus.Loading
            var lastError: Exception? = null
            try {
                val metadata = withTimeout(7000L.milliseconds) {
                    var result: DownloadMetadata? = null

                    while (result == null) {

                        try {
                            result = fetchingTheMetadata(downloadUrl)
                        } catch (e: UnknownHostException) {
                            throw e
                        } catch (e: Exception) {
                            lastError = e
                            delay(1500.milliseconds)
                        }
                    }
                    result
                }
                state = DownloadStatus.Loaded(metadata)
                showFormatSheet = true
            } catch (e: UnknownHostException) {
                state = DownloadStatus.Error(
                    errorMessage = flavorError.random(),
                    rawError = e.message ?: e.toString()
                )
            } catch (e: TimeoutCancellationException) {
                state = DownloadStatus.Error(errorMessage = flavorError.random(),
                    rawError = e.message ?: "Time out after retries: $lastError"
                )
            } catch (e: Exception) {
                state = DownloadStatus.Error(
                    errorMessage = flavorError.random(),
                    rawError = e.message ?: e.toString(),
                )
            }
        }
    }

    fun startingDownload(context: Context, audio: AudioQuality?, video: VideoQuality?) {

        val settingsTab = SettingsTab(context.applicationContext)

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

        activeProcess = requestedUrls.id

        val workManager = WorkManager
            .getInstance(context)
        workManager.enqueue(requestedUrls)
        workManager.getWorkInfoByIdFlow(requestedUrls.id)
            .onEach { workInfo ->
                workInfo ?: return@onEach
                when (workInfo.state) {
                    WorkInfo.State.RUNNING -> {

                        val ytdlpDetails = settingsTab.ytdlpDetails.first()
                        if (ytdlpDetails) {

                        val logs = workInfo.progress.getString("text")
                        if (!logs.isNullOrBlank()) {
                            downloaderLogs = (downloaderLogs + "\n" + logs).takeLast(200)
                        }
                    }
                        val progress = workInfo.progress.getFloat("progress", 0f)

                        state = DownloadStatus.Downloading(progress, metadata)
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        state = DownloadStatus.Loaded(metadata)
                        activeProcess = null
                    }

                    WorkInfo.State.FAILED -> {
                        state = DownloadStatus.Error(flavorError.random(), rawError = "")
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
    fun cancelButton(context: Context) {
        activeProcess?.let { id ->
            WorkManager.getInstance(context).cancelWorkById(id)
        }
        state = DownloadStatus.Idle
        downloadUrl = ""
        downloaderLogs = ""
        activeProcess = null
    }
}