package org.foss.fermux.ffmpeg.logic

import android.net.Uri

sealed class FFmpegStatus {
     data object Idle : FFmpegStatus()
     data class MidConversion(val inputUri: Uri) : FFmpegStatus()
     data class Loaded(val filePicked: FFmpegTargetFormat, val inputUri: Uri, val ffmpegLogs: String) : FFmpegStatus()
     data class Error(val flavourMessage: String, val rawError: String) : FFmpegStatus()
     data class Converting(
          val progress: Float,
          val filePicked: FFmpegTargetFormat,
          val inputUri: Uri,
          val ffmpegLogs: String
     ) : FFmpegStatus()
}

enum class MediaKind { IDLE, VIDEO, AUDIO, IMAGE }

data class VideoEncodingProfile(
     val softwareVideoArgs: List<String>,
     val hardwareVideoArgs: List<String>,
     val audioArgs: List<String>,
     val softwareQualityOption: String? = null,
)

enum class FFmpegTargetFormat(
     val workerFile: String,
     val category: MediaKind,
     val mimeType: String,
     val ffmpegExtraArgs: List<String>,
     val descriptor: String,
     val videoEncodingProfile: VideoEncodingProfile? = null,
) {

     MP4(
          "mp4",
          category = MediaKind.VIDEO,
          mimeType = "video/mp4",
          ffmpegExtraArgs = emptyList(),
          descriptor = "video(mp4)",
          videoEncodingProfile = VideoEncodingProfile(
               softwareVideoArgs = listOf("-c:v", "mpeg4"),
               hardwareVideoArgs = listOf("-c:v", "h264_mediacodec", "-b:v", "4M"),
               audioArgs = listOf("-c:a", "aac"),
               softwareQualityOption = "-q:v",
          ),
     ),
     MKV(
          "mkv",
          category = MediaKind.VIDEO,
          mimeType = "video/x-matroska",
          ffmpegExtraArgs = emptyList(),
          descriptor = "video(mkv)",
          videoEncodingProfile = VideoEncodingProfile(
               softwareVideoArgs = listOf("-c:v", "mpeg4"),
               hardwareVideoArgs = listOf("-c:v", "h264_mediacodec", "-b:v", "4M"),
               audioArgs = listOf("-c:a", "aac"),
               softwareQualityOption = "-q:v",
          ),
     ),
     MOV(
          "mov",
          category = MediaKind.VIDEO,
          mimeType = "video/quicktime",
          ffmpegExtraArgs = emptyList(),
          descriptor = "video(mov)",
          videoEncodingProfile = VideoEncodingProfile(
               softwareVideoArgs = listOf("-c:v", "mpeg4"),
               hardwareVideoArgs = listOf("-c:v", "h264_mediacodec", "-b:v", "4M"),
               audioArgs = listOf("-c:a", "aac"),
               softwareQualityOption = "-q:v",
          ),
     ),
     AVI(
          "avi",
          category = MediaKind.VIDEO,
          mimeType = "video/x-msvideo",
          ffmpegExtraArgs = emptyList(),
          descriptor = "video(avi)",
          videoEncodingProfile = VideoEncodingProfile(
               softwareVideoArgs = listOf("-c:v", "mpeg4"),
               hardwareVideoArgs = listOf("-c:v", "mpeg4_mediacodec", "-b:v", "4M"),
               audioArgs = listOf("-c:a", "libmp3lame"),
               softwareQualityOption = "-q:v",
          ),
     ),
     WEBM(
          "webm",
          category = MediaKind.VIDEO,
          mimeType = "video/webm",
          ffmpegExtraArgs = emptyList(),
          descriptor = "video(webm)",
          videoEncodingProfile = VideoEncodingProfile(
               // This build has MediaCodec VP8 but was not built with libvpx.
               softwareVideoArgs = listOf("-c:v", "vp8_mediacodec", "-b:v", "4M"),
               hardwareVideoArgs = listOf("-c:v", "vp8_mediacodec", "-b:v", "4M"),
               audioArgs = listOf("-c:a", "opus", "-strict", "experimental"),
          ),
     ),

     WAV(
          "wav",
          category = MediaKind.AUDIO,
          mimeType = "audio/wav",
          ffmpegExtraArgs = listOf("-vn", "-c:a", "pcm_s16le"),
          descriptor = "audio(wav)"
     ),
     MP3(
          "mp3",
          category = MediaKind.AUDIO,
          mimeType = "audio/mp3",
          ffmpegExtraArgs = listOf("-vn", "-c:a", "libmp3lame"),
          descriptor = "audio(mp3)"
     ),
     M4A(
          "m4a",
          category = MediaKind.AUDIO,
          mimeType = "audio/mp4",
          ffmpegExtraArgs = listOf("-vn", "-c:a", "aac"),
          descriptor = "audio(m4a)"
     ),
     FLAC(
          "flac",
          category = MediaKind.AUDIO,
          mimeType = "audio/flac",
          ffmpegExtraArgs = listOf("-vn", "-c:a", "flac"),
          descriptor = "audio(flac)"
     ),
     OGG(
          "ogg",
          category = MediaKind.AUDIO,
          mimeType = "audio/ogg",
          ffmpegExtraArgs = listOf("-vn", "-c:a", "vorbis","-strict", "-2"),
          descriptor = "audio(ogg)"
     ),
     GIF(
          "gif",
          category = MediaKind.IMAGE,
          mimeType = "image/gif",
          ffmpegExtraArgs = emptyList(),
          descriptor = "image(gif)"
     ),
     JPG(
          "jpg",
          category = MediaKind.IMAGE,
          mimeType = "image/jpeg",
          ffmpegExtraArgs = listOf("-frames:v", "1"),
          descriptor = "image(jpeg)"
     ),
     PNG(
          "png",
          category = MediaKind.IMAGE,
          mimeType = "image/png",
          ffmpegExtraArgs = listOf("-frames:v", "1"),
          descriptor = "image(png)"
     )
}

data class FFmpegUserPrefs(
     val audioBitrate: String? = null,
     val normalizeAudio: Boolean = false,
     val monoDownmix: Boolean = false,
     val enableVideoCompression: Boolean = false,
     val videoResolution: String? = null,
     val videoCrf: Int? = null,
     val useHardwareEncoder: Boolean = false,
     val threadLimit: Int? = null,
)
