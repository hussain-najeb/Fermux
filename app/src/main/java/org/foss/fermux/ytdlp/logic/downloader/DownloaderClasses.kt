package org.foss.fermux.ytdlp.logic.downloader

data class DownloadMetadata (
    val title: String,
    val thumbnail: String,
    val duration: Int,
    val uploader: String?, )

sealed class DownloadStatus {
    data object Idle : DownloadStatus()
    data object Loading : DownloadStatus()
    data class Loaded(val metadata: DownloadMetadata) : DownloadStatus()
    data class Error(val errorMessage: String, val rawError: String) : DownloadStatus()
    data class Downloading(val downloadProgress: Float, val metadata : DownloadMetadata) : DownloadStatus()
}


enum class AudioQuality (val musicQuality: String) // audio quality class to pass for ytdlp.
{
    BEST("0"),
    HIGH("192K"),
    MEDIUM("128k")
}

enum class VideoQuality(val videoQuality: String) {
    BEST("bestvideo+bestaudio/best"),
    HD1080("bestvideo[height<=1080]+bestaudio/best"),
    HD720("bestvideo[height<=720]+bestaudio/best"),
    SD480("bestvideo[height<=480]+bestaudio/best"),
    Q360("bestvideo[height<=360]+bestaudio/best"),
}