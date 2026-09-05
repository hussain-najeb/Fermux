package org.foss.fermux.ytdlp.logic.downloader


/**
 * This class is used as a template class for the metadata shape that later gets used in the [org.foss.fermux.ytdlp.ui.historyPage.HistoryCards] and information on the [org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates.FinishedCard] during download.
 */
data class DownloadMetadata(
     val title: String,
     val thumbnail: String,
     val duration: Int,
     val uploader: String?,
)

/**
 * Sealed class used to manage the downloader state UI.
 */
sealed class DownloadStatus {
     data object Idle : DownloadStatus()
     data object Loading : DownloadStatus()
     data class MidChoice(val metadata: DownloadMetadata) : DownloadStatus()
     data class Loaded(val metadata: DownloadMetadata) :  DownloadStatus()
     data class Completed(val metadata: DownloadMetadata) : DownloadStatus()
     data class Error(val errorMessage: String, val rawError: String) : DownloadStatus()
     data class Downloading(val downloadProgress: Float, val metadata: DownloadMetadata) : DownloadStatus()
}

/**
 * Enum class used to handle the types of media to get handled by the UI during download.
 */
enum class FormatKind { Video, Audio, Idle }

/**
 * Enum class for specifying the quality of the audio when used to download audio.
 */
enum class AudioQuality(val musicQuality: String) // audio quality class to pass for ytdlp.
{
     BEST("0"),   // ~220-260 kbps (V0)
     HIGH("2"),   // ~170-210 kbps (V2)
     MEDIUM("5"), // ~100-140 kbps (V5 - yt-dlp default)
     LOW("9") // ~65 kbps (V9)
}


// TODO. Add format support for the downloader tab dialog
//enum class AudioFormat (val musicFormat: String) {
//    MP3()
//}

/**
 * Enum class for specifying the quality of the video when used to download video.
 */

enum class VideoQuality(val videoQuality: String) {
     BEST("bestvideo+bestaudio/best"),
     HD1080("bestvideo[height<=1080]+bestaudio/best"),
     HD720("bestvideo[height<=720]+bestaudio/best"),
     SD480("bestvideo[height<=480]+bestaudio/best"),
     Q360("bestvideo[height<=360]+bestaudio/best"),
     Q240("bestvideo[height<=240]+bestaudio/best"),
     Q144("bestvideo[height<=144]+bestaudio/best")
}

/**
 * Enum for audio formats
 */
enum class AudioFormat(val ytdlpFormat: String) {
     OpusFormat("opus"),
     Mp3Format("mp3"),
     FlacFormat("flac"),
     M4aFormat("m4a")
} // TODO. add UI to this

/**
 * Enum class used by the [org.foss.fermux.settings.ui.downloader.SimpleDownloaderPage] and the [downloaderLogic] to manage aria2c.
 */
enum class Aria2cMode {
     Disabled,
     EdgeCaseOnly,
     Always;

     val externalDownloaderState: Boolean
          get() = this == EdgeCaseOnly || this == Always
}

enum class YtdlpChannel {
     Stable,
     Nightly,
     Master
}    

enum class ExternalDownloaders {
     TurnedOff,
     FFmpegAsExternal,
     YtdlpNativeDownloader
}