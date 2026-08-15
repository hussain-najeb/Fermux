package org.foss.fermux.ffmpeg.logic

import android.net.Uri

sealed class FFmpegStatus {

     data object Idle: FFmpegStatus()
     data class Loaded (val filePicked: FFmpegTargetFormat, val inputUri: Uri, val ffmpegLogs: String ): FFmpegStatus()
     data class Error(val flavourMessage: String, val rawError: String) : FFmpegStatus()
     data class Converting(val progress: Float, val duration: Long, val filePicked: FFmpegTargetFormat, val inputUri: Uri, val ffmpegLogs: String): FFmpegStatus()

}

enum class MediaKind { VIDEO, AUDIO, IMAGE }

enum class FFmpegTargetFormat(
     val workerFile: String,
     val category: MediaKind,
     val mimeType: String,
     val ffmpegExtraArgs: List<String>,
     val descriptor: String) {

     MP4("mp4",   category = MediaKind.VIDEO, mimeType = "video/mp4",        ffmpegExtraArgs = listOf("-c:v", "copy", "-c:a", "copy"), descriptor = "video(mp4)"),
     MKV("mkv",   category = MediaKind.VIDEO, mimeType = "video/x-matroska", ffmpegExtraArgs = listOf("-c:v", "copy", "-c:a", "copy"), descriptor = "video(mkv)"),
     MOV("mov",   category = MediaKind.VIDEO, mimeType = "video/quicktime",  ffmpegExtraArgs = listOf("-c:v", "copy", "-c:a", "copy"), descriptor = "video(mov)"),
     AVI("avi",   category = MediaKind.VIDEO, mimeType = "video/x-msvideo",  ffmpegExtraArgs = listOf("-c:v", "copy", "-c:a", "copy"), descriptor = "video(avi)"),
     WEBM("webm", category = MediaKind.VIDEO, mimeType = "video/webm",       ffmpegExtraArgs = listOf("-c:v", "copy", "-c:a", "copy"), descriptor = "video(webm)"),

     WAV("wav",   category = MediaKind.AUDIO, mimeType = "audio/wav",        ffmpegExtraArgs = listOf("-vn", "-c:a", "pcm_s16le"), descriptor = "audio(wav)"),
     MP3("mp3",   category = MediaKind.AUDIO, mimeType = "audio/mp3",        ffmpegExtraArgs = listOf("-vn", "-c:a", "libmp3lame"),        descriptor = "audio(mp3)" ),
     M4A("m4a",   category = MediaKind.AUDIO, mimeType = "audio/mp4",        ffmpegExtraArgs = listOf("-vn", "-c:a", "aac"),         descriptor = "audio(m4a)"),
     FLAC("flac", category = MediaKind.AUDIO, mimeType = "audio/flac",       ffmpegExtraArgs = listOf("-vn", "-c:a", "flac"),        descriptor = "audio(flac)"),
     OGG("ogg",   category = MediaKind.AUDIO, mimeType = "audio/ogg",        ffmpegExtraArgs = listOf("-vn", "-c:a", "libvorbis"),   descriptor = "audio(ogg)"),

     GIF("gif",   category = MediaKind.IMAGE, mimeType = "image/gif",        ffmpegExtraArgs = emptyList(),                          descriptor = "image(gif)"),
     JPG("jpg",   category = MediaKind.IMAGE, mimeType = "image/jpeg",       ffmpegExtraArgs = listOf("-frames:v", "1"),             descriptor = "image(jpeg)"),
     PNG("png",   category = MediaKind.IMAGE, mimeType = "image/png",        ffmpegExtraArgs = listOf("-frames:v", "1"),             descriptor = "image(png)"),

} // TODO. Video/Audio cutting and effects is planned here as well.