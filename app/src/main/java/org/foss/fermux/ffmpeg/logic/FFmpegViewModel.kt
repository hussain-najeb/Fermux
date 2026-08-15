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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.UUID
import kotlin.text.takeLast


sealed class FFmpegStatus {

    data object Idle: FFmpegStatus()
    data class Loaded (val filePicked: FFmpegTargetFormat, val inputUri: Uri, val FFmpegLogs: String ): FFmpegStatus()
    data class Error(val flavourMessage: String, val rawError: String) : FFmpegStatus()
    data class Converting(val progress: Float, val duration: Long, val filePicked: FFmpegTargetFormat, val inputUri: Uri, val FFmpegLogs: String): FFmpegStatus()

}

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

} // TODO. Video/Audio cutting and effects is planned here as well.


class FFmpegViewModel: ViewModel() {

    var inputUri by mutableStateOf<Uri?>(null)
    var FFmpegLogs by mutableStateOf("")
    var state by mutableStateOf<FFmpegStatus>(FFmpegStatus.Idle)
    var selectedFormat by mutableStateOf(FFmpegTargetFormat.WAV)
    var inputKind by mutableStateOf<MediaKind?>(null)

    private var activeProcess by mutableStateOf<UUID?>(null)

    private var ffmpegJob: Job? = null

    val flavourMessage = listOf(
        "Oh no, did you convert audio to video?",
        "This has always been problematic",
        "Good luck solving it"
    )

    private fun fail(flavourFailMessage: String, rawError: String) {
        state = FFmpegStatus.Error(flavourFailMessage, rawError)
    }

    fun typeErrorClarification(context: Context) {
        val uri = inputUri
        val mime = uri?.let { context.contentResolver.getType(it) }
        val extension = uri?.let { MimeTypeMap.getFileExtensionFromUrl(it.toString()) }

        fail(
            flavourFailMessage = flavourMessage.random(),
            rawError = "This input is unsupported, check this info:\n" +
                    "uri = $uri\n" + "mime = ${mime ?: "Unknown"}\n" + "extension = ${extension?.takeIf { it.isNotBlank() } ?: "Unknown"}"
        )
    }

    fun updateInputKind(context: Context) {
        inputKind = inputUri?.let { detectInputKind(context, it) }
    }

    fun isSheetFormat(format: FFmpegTargetFormat, sheet: MediaKind): Boolean {
        return format.category == sheet && isConversionAllowed(format)
    }

    private fun isConversionAllowed(target: FFmpegTargetFormat): Boolean {
        val input = inputKind ?: return false
        return when (input) {
            MediaKind.AUDIO -> target.category == MediaKind.AUDIO
            MediaKind.VIDEO -> true
            MediaKind.IMAGE -> target.category == MediaKind.IMAGE
        }
    }

    fun startingConversion(context: Context, inputUri: Uri, targetFormat: FFmpegTargetFormat) {
        updateInputKind(context)

        if (!isConversionAllowed(targetFormat)) {
            fail(
                flavourFailMessage = flavourMessage.random(),
                rawError = "Cannot convert $inputKind input to ${targetFormat.descriptor}"
            )
            return
        }

       ffmpegJob = viewModelScope.launch {
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

           activeProcess = request.id

           state = FFmpegStatus.Converting(0f, 0L, targetFormat, inputUri, FFmpegLogs)

            workManager.getWorkInfoByIdFlow(request.id).onEach { workInfo ->
                workInfo ?: return@onEach
                when (workInfo.state) {
                    WorkInfo.State.RUNNING -> {

                        val progress = workInfo.progress.getFloat("progress", 0f)
                        val duration = workInfo.progress.getLong("duration", 0)
                        val logs = workInfo.progress.getString("line")

                        if (!logs.isNullOrBlank()) {
                            FFmpegLogs = (FFmpegLogs + "\n" + logs).takeLast(900)
                        }
                        state = FFmpegStatus.Converting(progress, duration, targetFormat, inputUri, FFmpegLogs)
                    }

                    WorkInfo.State.SUCCEEDED -> {
                        state = FFmpegStatus.Loaded(targetFormat, inputUri, FFmpegLogs)
                        activeProcess = null
                    }

                    WorkInfo.State.FAILED -> {
                        val rawError = workInfo.outputData.getString("error") ?: "Unknown error"
                        fail(
                            flavourFailMessage = flavourMessage.random(),
                            rawError = rawError
                        )
                        activeProcess = null
                    }

                    WorkInfo.State.CANCELLED -> {
                        state = FFmpegStatus.Idle
                        activeProcess = null
                    }
                    else -> {}
                }
            }
                .launchIn(viewModelScope)
        }
    }


    fun cancelButton(context: Context) {
        activeProcess?.let { id ->
            WorkManager.getInstance(context).cancelWorkById(id)
        }

        ffmpegJob?.cancel()
        ffmpegJob = null
        state = FFmpegStatus.Idle
        inputUri = null
        FFmpegLogs = ""
        selectedFormat = FFmpegTargetFormat.WAV
        activeProcess = null
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
    val ext = MimeTypeMap.getFileExtensionFromUrl(uri.toString()).lowercase()
    val guessedMime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: return null
    return when {
        guessedMime.startsWith("video/") -> MediaKind.VIDEO
        guessedMime.startsWith("audio/") -> MediaKind.AUDIO
        guessedMime.startsWith("image/") -> MediaKind.IMAGE
        else -> null
    }
}