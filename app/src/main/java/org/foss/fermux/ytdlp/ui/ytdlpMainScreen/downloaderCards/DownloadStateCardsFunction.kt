package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderCards

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel

@Composable
fun DownloaderCards (state: DownloadStatus, downloaderViewModel: DownloaderViewModel) {

    val context = LocalContext.current
    when (state) {
        is DownloadStatus.Idle -> {} // Idle state of the card

        is DownloadStatus.Loading -> {
            LoadingCard(state = state,
                onCancel = {downloaderViewModel.cancelButton(context)})
        } // while downloading the info to the card

        is DownloadStatus.Downloading -> {
            FinishedCard(state.metadata,
                state.downloadProgress,
//                downloaderLogs = downloaderLogs,
//                showYtdlpDetails = viewModel.showYtdlpDetails,
                onCancel = {downloaderViewModel.cancelButton(context)})
        } // just to get a damn bar to show the progress.

        is DownloadStatus.Loaded -> {
            FinishedCard(state.metadata,
                onCancel = {downloaderViewModel.cancelButton(context)})
        }
        // the card gets loaded to view the damn
        // content when you call state. state here is assigned to "metadata", then to the actual card
        // composable later gets to be assigned to "DownloadMetadata" to fill out the
        // info in that data class.

        is DownloadStatus.Error -> {
            ErrorCard(state.errorMessage, state.rawError, onCancel = {downloaderViewModel.cancelButton(context)})
        } // if god forbids, an error happens; it's seen here.
    }
}

