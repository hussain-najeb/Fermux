package org.foss.fermux.ytdlp.logic.downloader

data class DownloadMetadata (
    val title: String,
    val thumbnail: String,
    val duration: Int,
    val uploader: String?, )

sealed class DownloadStatus {
    data object Idle : DownloadStatus()
    data object Loading : DownloadStatus()
    data class MidChoice(val metadata: DownloadMetadata ) : DownloadStatus()
    data class Loaded(val metadata: DownloadMetadata) : DownloadStatus() // Takes the loaded metadata first and lay it for the user
    data class Completed(val metadata: DownloadMetadata) : DownloadStatus()
    data class Error(val errorMessage: String, val rawError: String) : DownloadStatus()
    data class Downloading(val downloadProgress: Float, val metadata : DownloadMetadata) : DownloadStatus()
}

enum class FormatKind { Video, Audio, Idle }

enum class AudioQuality (val musicQuality: String, formatKind: FormatKind) // audio quality class to pass for ytdlp.
{
    BEST("0", formatKind = FormatKind.Audio),   // ~220-260 kbps (V0)
    HIGH("2", formatKind = FormatKind.Audio),   // ~170-210 kbps (V2)
    MEDIUM("5", formatKind = FormatKind.Audio), // ~100-140 kbps (V5 - yt-dlp default)
    LOW("9", formatKind = FormatKind.Audio) // ~65 kbps (V9)
}


// TODO. Add format supoprt for the downloader tab dialog
//enum class AudioFormat (val musicFormat: String) {
//    MP3()
//}

enum class VideoQuality(val videoQuality: String, formatKind: FormatKind) {
    BEST("bestvideo+bestaudio/best", formatKind = FormatKind.Video),
    HD1080("bestvideo[height<=1080]+bestaudio/best", formatKind = FormatKind.Video),
    HD720("bestvideo[height<=720]+bestaudio/best", formatKind = FormatKind.Video),
    SD480("bestvideo[height<=480]+bestaudio/best", formatKind = FormatKind.Video),
    Q360("bestvideo[height<=360]+bestaudio/best", formatKind = FormatKind.Video),
    Q240("bestvideo[height<=240]+bestaudio/best", formatKind = FormatKind.Video),
    Q144("bestvideo[height<=144]+bestaudio/best", formatKind = FormatKind.Video)
}
