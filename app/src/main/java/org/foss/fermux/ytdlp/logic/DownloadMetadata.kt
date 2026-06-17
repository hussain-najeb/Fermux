package org.foss.fermux.ytdlp.logic

import android.annotation.SuppressLint

data class DownloadMetadata (
    val title: String,
    val thumbnail: String,
    val duration: Int,
    val uploader: String?, )

sealed class DownloadStatus {
    data object Idle : DownloadStatus() // no card in view. so its idle and won't display a thing
    data object Loading : DownloadStatus() // loading the damn card.
    data class Loaded(val metadata: DownloadMetadata) : DownloadStatus() // the card is successful at loading
    data class Error(val errorMessage: String) : DownloadStatus() // when something goes wrong
    data class Downloading(val downloadProgress : Float, val metadata : DownloadMetadata) : DownloadStatus() // data for the fucking loadingIndicator
}

// to extract the metadata.



// this one is to grab the "duration" from the metadata class and throws
// an actual time format to be displayed. so instead of "195" its "3:15".
@SuppressLint("DefaultLocale")
fun videoTime (seconds: Int): String {

    val minutes = seconds / 60
    val second = seconds % 60
    return String.format("%02d:%02d", minutes, second)
}