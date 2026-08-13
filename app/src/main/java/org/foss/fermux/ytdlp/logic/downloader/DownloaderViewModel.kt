package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import org.foss.fermux.storage.SettingsTab
import java.net.UnknownHostException
import java.util.*
import kotlin.time.Duration.Companion.milliseconds

class DownloaderViewModel : ViewModel() {
    var state by mutableStateOf<DownloadStatus>(DownloadStatus.Idle)
    var showFormatSheet by mutableStateOf(false)
    var downloadUrl by mutableStateOf("")
    var downloaderLogs by mutableStateOf("")
    private var activeProcess by mutableStateOf<UUID?>(null)
    private var downloaderJob: Job? = null

    var showYtdlpDetails by mutableStateOf(false)
    val flavorError = listOf(
            "Dammit, something must have gone wrong",
            "Could be a connection issue, check your internet connection",
            "You guessed it, it's a network error",
            "what does an LLM say about it?",
            "Did you paste a URL?"
        )

    fun fetchedMetadata(downloadUrl: String) {
        downloaderJob = viewModelScope.launch {
            state = DownloadStatus.Loading
            try {
                val metadata = withTimeout(20000L.milliseconds) {
                    fetchingTheMetadata(downloadUrl)
                }

                state = DownloadStatus.Loaded(metadata)
                showFormatSheet = true

            } catch (e: UnknownHostException) {
                downloadErrorHandler(e)
            } catch (e: TimeoutCancellationException) {
                downloadErrorHandler(e)
            } catch (e: Exception) {
                downloadErrorHandler(e)
            }
        }
    }

    private fun downloadErrorHandler (e: Exception) {
        Log.e("MetadataFetch", "Fetch failed: ${e.javaClass.simpleName}", e)
        val raw = when(e) {
            is TimeoutCancellationException -> "Timed out waiting for a response"
            else -> e.message ?: e.toString()
        }
        state = DownloadStatus.Error(flavorError.random(), raw)
    }

    fun startingDownload(context: Context, audio: AudioQuality?, video: VideoQuality?) {
        val settingsTab = SettingsTab(context.applicationContext)
        val metadata = (state as? DownloadStatus.Loaded)?.metadata ?: return
        viewModelScope.launch { showYtdlpDetails = settingsTab.ytdlpDetails.first() }
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
        state = DownloadStatus.Downloading(0f, metadata)

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
                            downloaderLogs = (downloaderLogs + logs)
                        }
                    }
                        val progress = workInfo.progress.getFloat("progress", 0f)
                            .coerceIn(0f, 100f)

                        state = DownloadStatus.Downloading(progress, metadata)
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        state = DownloadStatus.Completed(metadata)
                        activeProcess = null
                    }
                    WorkInfo.State.FAILED -> {
                        val error = workInfo.outputData.getString("error") ?: "Unknown Error!"
                        state = DownloadStatus.Error(flavorError.random(), rawError = error)
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
            YoutubeDL.destroyProcessById(id.toString())
            WorkManager.getInstance(context).cancelWorkById(id)
        }
        downloaderJob?.cancel()
        downloaderJob = null

        state = DownloadStatus.Idle
        downloadUrl = ""
        downloaderLogs = ""
        activeProcess = null
    }
}
