package org.foss.fermux.ytdlp.ui

import androidx.compose.runtime.Composable
import org.foss.fermux.ytdlp.logic.DownloadStatus

@Composable
fun CardWhenUsage (state: DownloadStatus) {
    when (state) {
        is DownloadStatus.Idle -> {} // Idle state of the card
        is DownloadStatus.Loading -> {LoadingCard()} // while downloading the info to the card
        is DownloadStatus.Loaded -> {LoadedCard (state.metadata)}
        // the card gets loaded to view the damn
        // content when you call state. state here is assigned to "metadata", then to the actual card
        // composable later gets to be assgined to "DownloadMetadata" to fill out the
        // info in that data class.
        is DownloadStatus.Downloading -> {ProgressBar()} // just to get a damn bar to show the progress.
        is DownloadStatus.Error -> {state.errorMessage} // if god forbids, an error happens, its seen here.

    }
}