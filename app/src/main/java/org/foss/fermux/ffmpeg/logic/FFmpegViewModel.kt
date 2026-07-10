package org.foss.fermux.ffmpeg.logic
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.launch
import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.work.WorkInfo
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.text.takeLast


sealed class FFmpegStatus {

    data object Idle: FFmpegStatus()
    data class Loaded (val filePicked: FFmpegTargetFormat, val inputUri: Uri, val FFmpegLogs: String ): FFmpegStatus()
    data class Error(val errorMessage: String) : FFmpegStatus()
    data class Converting(val progress: Float, val duration: Long, val filePicked: FFmpegTargetFormat, val inputUri: Uri, val FFmpegLogs: String): FFmpegStatus()

}

enum class FFmpegTargetFormat(
    val workerFile: String,
    val isVideo: Boolean,
    val isAudio: Boolean,
    val isImage: Boolean,
    val descriptor: String) {

    MP4("mp4",   isVideo = true, isAudio = false, isImage = false,"video(mp4)"),
    MKV("mkv",   isVideo = true, isAudio = false, isImage = false, "video(mkv)"),
    MOV("mov",   isVideo = true, isAudio = false, isImage = false,"video(mov)"),
    AVI("avi",   isVideo = true, isAudio = false, isImage = false,"video(avi)"),
    WEBM("webm", isVideo = true, isAudio = false, isImage = false,"video(webm)"),

    WAV("wav",   isVideo = false, isAudio = true, isImage = false, "audio(wav)"),
    AAC("aac",   isVideo = false, isAudio = true, isImage = false,"audio(aac)"),
    M4A("m4a",   isVideo = false, isAudio = true, isImage = false,"audio(m4a)"),
    FLAC("flac", isVideo = false, isAudio = true, isImage = false, "audio(flac)"),
    OGG("ogg",   isVideo = false, isAudio = true, isImage = false,"audio(ogg)"),

    GIF("gif",   isVideo = false, isAudio = false, isImage = true,"image(gif)"),
    JPG("jpg",   isVideo = false, isAudio = false, isImage = true, "image(jpeg)"),
    PNG("png",   isVideo = false, isAudio = false, isImage = true, "image(png)"),

} // TODO. Video/Audio cutting and effects is planed here as well.
 


class FFmpegViewModel: ViewModel() {

    var inputUri by mutableStateOf<Uri?>(null)
    var FFmpegLogs by mutableStateOf("")
    var state by mutableStateOf<FFmpegStatus>(FFmpegStatus.Idle)
    var selectedFormat by mutableStateOf(FFmpegTargetFormat.entries.first())
    fun startingConversion(context: Context, inputUri: Uri, targetFormat: FFmpegTargetFormat) {
        viewModelScope.launch {
             val inputData = workDataOf(
                 "FFMPEG_URI_FILE" to inputUri.toString(),
                         "TARGET_FORMAT" to targetFormat.name
             )

            val request = OneTimeWorkRequestBuilder<FFmpegWorker>()
                .setInputData(inputData)
                .build()

            val workManager = WorkManager.getInstance(context)
            workManager.enqueue(request)

            workManager.getWorkInfoByIdFlow(request.id).onEach { workInfo ->
                workInfo ?: return@onEach
                when (workInfo.state) {
                    WorkInfo.State.RUNNING -> {

                        val progress = workInfo.progress.getFloat("progress", 0f)
                        val duration = workInfo.progress.getLong("duration", 0)
                        val logs = workInfo.progress.getString("line")

                        if (!logs.isNullOrBlank()) {
                            FFmpegLogs = (FFmpegLogs + "\n" + logs).takeLast(500)
                        }
                        state = FFmpegStatus.Converting(progress, duration, targetFormat, inputUri, FFmpegLogs)
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        state = FFmpegStatus.Loaded(targetFormat, inputUri, FFmpegLogs)
                    }

                    WorkInfo.State.FAILED -> {
                        state = FFmpegStatus.Error("Failed to load anything at all")
                    }
                    else -> {}
                }
            }
                .launchIn(viewModelScope)
        }
    }
}