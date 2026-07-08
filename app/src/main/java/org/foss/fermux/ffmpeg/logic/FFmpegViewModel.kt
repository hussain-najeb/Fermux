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



sealed class FFmpegStatus {

    data object Idle: FFmpegStatus()
    data object Loading: FFmpegStatus()
    data class Loaded (val filePicked: FFmpegTargetFormat, val inputUri: Uri ): FFmpegStatus()
    data class Error(val errorMessage: String) : FFmpegStatus()
    data class Converting(val progress: Float, val duration: Long, val filePicked: FFmpegTargetFormat, val inputUri: Uri): FFmpegStatus()

}

enum class FFmpegTargetFormat(val workerFile: String, val descriptor: String) {

    MP4("mp4", "video(mp4)"),
    MKV("mkv", "video(mkv)"),
    MOV("mov", "video(mov)"),
    AVI("avi", "video(avi)"),
    WEBM("webm", "video(webm)"),

    WAV("wav", "audio(wav)"),
    AAC("aac", "audio(aac)"),
    M4A("m4a", "audio(m4a)"),
    FLAC("flac", "audio(flac)"),
    OGG("ogg", "audio(ogg)"),

    GIF("gif", "animation(gif)"),
    JPG("jpg", "image(jpeg)"),
    PNG("png", "image(png)"),

} // TODO. Video/Audio cutting and effects is planed here as well.
 


class FFmpegViewModel: ViewModel() {

    var FFmpegLogs by mutableStateOf("")
    var state by mutableStateOf<FFmpegStatus>(FFmpegStatus.Idle)

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
                        state = FFmpegStatus.Converting(progress, duration,  targetFormat, inputUri)

                        if (!logs.isNullOrBlank()) {
                            FFmpegLogs += "\n$logs"
                        }
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        state = FFmpegStatus.Loaded(targetFormat, inputUri)
                    }

                    WorkInfo.State.FAILED -> {
                        state = FFmpegStatus.Error("")
                    }
                    else -> {}
                }
            }
                .launchIn(viewModelScope)
        }
    }
}