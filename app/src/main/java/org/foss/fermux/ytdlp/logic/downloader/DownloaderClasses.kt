package org.foss.fermux.ytdlp.logic.downloader

const val FERMUX_METADATA_MARKER = "FERMUX_METADATA_JSON:"

/**
 * This class is used as a template class for the metadata shape that later gets used in the [org.foss.fermux.ytdlp.ui.historyPage.HistoryCards] and information on the [org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates.FinishedCard] during download.
 */
data class DownloadMetadata(
     val title: String,
     val thumbnail: String,
     val duration: Int,
     val uploader: String?,
     val size: Long?,
     val audioFormat: String?,
     val audioQuality: Double?,
     val resolution: String?
)

/**
 * Sealed class used to manage the downloader state UI.
 */
sealed class DownloadStatus {
     data object Idle : DownloadStatus()
     data object UserArgs : DownloadStatus()
     data object QuickDownload: DownloadStatus()
     data object LoadingMetadata : DownloadStatus()
     data class Downloading(val downloadProgress: Float, val metadata: DownloadMetadata?) : DownloadStatus()
     data class Completed(val metadata: DownloadMetadata) : DownloadStatus()
     data class Error(val errorMessage: String, val rawError: String) : DownloadStatus()
}

/**
 * Enum class used to handle the types of media to get handled by the UI during download.
 */
enum class FormatKind { Video, Audio, Idle }

/**
 * Enum class for specifying the quality of the audio when used to download audio.
 */
enum class AudioQuality(val audioQuality: String) // audio quality class to pass for ytdlp.
{
     BEST("0"),   // ~220-260 kbps (V0)
     HIGH("2"),   // ~170-210 kbps (V2)
     MEDIUM("5"), // ~100-140 kbps (V5 - yt-dlp default)
     LOW("9") // ~65 kbps (V9)
}



/**
 * Enum class for specifying the quality of the video when used to download video.
 */

enum class VideoQuality(val videoQuality: String) {
     HD1080("bv*[height<=1080]+ba/b[height<=1080]"),
     HD720("bv*[height<=720]+ba/b[height<=720]"),
     SD480("bv*[height<=480]+ba/b[height<=480]"),
     Q360("bv*[height<=360]+ba/b[height<=360]"),
     Q240("bv*[height<=240]+ba/b[height<=240]"),
     Q144("bv*[height<=144]+ba/b[height<=144]")
}

/**
 * Enum for audio formats
 */
enum class AudioFormat(val audioFormats: String) {
     OpusFormat("opus"),
     Mp3Format("mp3"),
     FlacFormat("flac"),
     M4aFormat("m4a")
}

enum class ThumbnailFormat(val thumbnailFormat: String) {
     Png("png"),
     Jpeg("jpg"),
     WebP("webp"),
     Off("")
}

enum class VideoFormat(val videoFormat: String) {
     Mp4Format("mp4"),
     AviFormat("avi"),
     WebMFormat("webm"),
     Mkv("mkv")
}

/**
 * Enum class used by the [org.foss.fermux.settings.ui.downloader.SimpleDownloaderPage] and the [downloaderLogic] to manage aria2.
 */
enum class Aria2cMode {
     Disabled,
     EdgeCaseOnly,
     Always;
}

enum class YtdlpChannel {
     Stable,
     Nightly,
     Master
}

enum class Connectivity {
     Wifi,
     Cellular,
     Any
}

enum class IpvConnection {
     Ipv6,
     Ipv4,
     Disabled
}

enum class ExternalDownloaders {
     Disabled,
     FFmpegAsExternal,
     YtdlpNativeDownloader
}
