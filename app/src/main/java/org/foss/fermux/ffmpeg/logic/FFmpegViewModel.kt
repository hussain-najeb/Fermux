@file:Suppress("PropertyName")

package org.foss.fermux.ffmpeg.logic
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.launch
import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.work.WorkInfo
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.internal.format
import kotlin.text.takeLast


sealed class FFmpegStatus {

    data object Idle: FFmpegStatus()
    data class Loaded (val filePicked: FFmpegTargetFormat, val inputUri: Uri, val FFmpegLogs: String ): FFmpegStatus()
    data class Error(val errorMessage: String) : FFmpegStatus()
    data class Converting(val progress: Float, val duration: Long, val filePicked: FFmpegTargetFormat, val inputUri: Uri, val FFmpegLogs: String): FFmpegStatus()

}

// Single media kind instead of three exclusive booleans (bug 1)
enum class MediaKind { VIDEO, AUDIO, IMAGE }

enum class FFmpegTargetFormat(
    val workerFile: String,
    val category: MediaKind,
    val mimeType: String,
    val ffmpegExtraArgs: List<String>,
    val descriptor: String) {

    MP4("mp4",   category = MediaKind.VIDEO, mimeType = "video/mp4",       ffmpegExtraArgs = listOf("-c:v", "copy", "-c:a", "copy"), descriptor = "video(mp4)"),
    MKV("mkv",   category = MediaKind.VIDEO, mimeType = "video/x-matroska", ffmpegExtraArgs = listOf("-c:v", "copy", "-c:a", "copy"), descriptor = "video(mkv)"),
    MOV("mov",   category = MediaKind.VIDEO, mimeType = "video/quicktime",  ffmpegExtraArgs = listOf("-c:v", "copy", "-c:a", "copy"), descriptor = "video(mov)"),
    AVI("avi",   category = MediaKind.VIDEO, mimeType = "video/x-msvideo",  ffmpegExtraArgs = listOf("-c:v", "copy", "-c:a", "copy"), descriptor = "video(avi)"),
    WEBM("webm", category = MediaKind.VIDEO, mimeType = "video/webm",       ffmpegExtraArgs = listOf("-c:v", "copy", "-c:a", "copy"), descriptor = "video(webm)"),

    WAV("wav",   category = MediaKind.AUDIO, mimeType = "audio/wav",        ffmpegExtraArgs = listOf("-vn", "-c:a", "pcm_s16le"), descriptor = "audio(wav)"),
    M4A("m4a",   category = MediaKind.AUDIO, mimeType = "audio/mp4",       ffmpegExtraArgs = listOf("-vn", "-c:a", "aac"),         descriptor = "audio(m4a)"),
    FLAC("flac", category = MediaKind.AUDIO, mimeType = "audio/flac",       ffmpegExtraArgs = listOf("-vn", "-c:a", "flac"),        descriptor = "audio(flac)"),
    OGG("ogg",   category = MediaKind.AUDIO, mimeType = "audio/ogg",       ffmpegExtraArgs = listOf("-vn", "-c:a", "libvorbis"),   descriptor = "audio(ogg)"),

    GIF("gif",   category = MediaKind.IMAGE, mimeType = "image/gif",        ffmpegExtraArgs = emptyList(),                          descriptor = "image(gif)"),
    JPG("jpg",   category = MediaKind.IMAGE, mimeType = "image/jpeg",       ffmpegExtraArgs = listOf("-frames:v", "1"),             descriptor = "image(jpeg)"),
    PNG("png",   category = MediaKind.IMAGE, mimeType = "image/png",       ffmpegExtraArgs = listOf("-frames:v", "1"),             descriptor = "image(png)"),

} // TODO. Video/Audio cutting and effects is planed here as well.


class FFmpegViewModel: ViewModel() {

    var inputUri by mutableStateOf<Uri?>(null)
    var FFmpegLogs by mutableStateOf("")
    var state by mutableStateOf<FFmpegStatus>(FFmpegStatus.Idle)
    // Default to audio, not MP4 (bug 5)
    var selectedFormat by mutableStateOf(FFmpegTargetFormat.WAV)
    // Tracks what the picked file actually is (bug 3)
    var inputKind by mutableStateOf<MediaKind?>(null)

    fun fail(message: String) {
        state = FFmpegStatus.Error(message)
    }

    // Guesses audio/video/image from MIME or file extension
    fun updateInputKind(context: Context) {
        inputKind = inputUri?.let { detectInputKind(context, it) }
    }

    // Used by format sheets: right tab + allowed for this input
    fun isSheetFormat(format: FFmpegTargetFormat, sheet: MediaKind): Boolean {
        return format.category == sheet && isConversionAllowed(format)
    }

    private fun isConversionAllowed(target: FFmpegTargetFormat): Boolean {
        val input = inputKind ?: return false
        return when (input) {
            // Audio can only become audio — blocks audio→video crashes (bug 3)
            MediaKind.AUDIO -> target.category == MediaKind.AUDIO
            // Video can be converted, demuxed, or frame-grabbed
            MediaKind.VIDEO -> true
            MediaKind.IMAGE -> target.category == MediaKind.IMAGE
        }
    }

    fun startingConversion(context: Context, inputUri: Uri, targetFormat: FFmpegTargetFormat) {
        updateInputKind(context)
        if (!isConversionAllowed(targetFormat)) {
            fail("This file can't be converted to that format")
            return
        }

        viewModelScope.launch {
             val inputData = workDataOf(
                 "FFMPEG_URI_FILE" to inputUri.toString(),
                 "TARGET_FORMAT" to targetFormat.name,
                 "FFMPEG_EXTRA_ARGS" to targetFormat.ffmpegExtraArgs.toTypedArray(),
                 "OUTPUT_MIME_TYPE" to targetFormat.mimeType,
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
                            FFmpegLogs = (FFmpegLogs + "\n" + logs).takeLast(200)
                        }
                        state = FFmpegStatus.Converting(progress, duration, targetFormat, inputUri, FFmpegLogs)
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        state = FFmpegStatus.Loaded(targetFormat, inputUri, FFmpegLogs)
                    }

                    WorkInfo.State.FAILED -> {
                       fail(message = "Conversion failed in the worker")
                    }
                    else -> {}
                }
            }
                .launchIn(viewModelScope)
        }
    }
}

private fun detectInputKind(context: Context, uri: Uri): MediaKind? {
    val mime = context.contentResolver.getType(uri)
    if (mime != null) {
        return when {
            mime.startsWith("video/") -> MediaKind.VIDEO
            mime.startsWith("audio/") -> MediaKind.AUDIO
            mime.startsWith("image/") -> MediaKind.IMAGE
            else -> null
        }
    }
    // Fallback when the picker doesn't give a MIME type
    val ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString()).lowercase()
    val guessedMime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: return null
    return when {
        guessedMime.startsWith("video/") -> MediaKind.VIDEO
        guessedMime.startsWith("audio/") -> MediaKind.AUDIO
        guessedMime.startsWith("image/") -> MediaKind.IMAGE
        else -> null
    }
}
