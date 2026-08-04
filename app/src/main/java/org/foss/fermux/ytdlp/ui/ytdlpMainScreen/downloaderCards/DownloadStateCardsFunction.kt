package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderCards

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel

@Composable
fun DownloaderCards(state: DownloadStatus, downloaderViewModel: DownloaderViewModel) {
    val context = LocalContext.current
    val spatialSpec = MaterialTheme.motionScheme

    AnimatedContent(
        targetState = state,
        transitionSpec = {
            (slideInVertically(
                animationSpec = spatialSpec.fastSpatialSpec(),
                initialOffsetY = { -it }
            ) + fadeIn()).togetherWith(
                slideOutVertically(
                    animationSpec = spatialSpec.fastSpatialSpec(),
                    targetOffsetY = { -it }
                ) + fadeOut()
            )
        },
        label = "DownloaderCardTransition"
    ) { targetState ->
        when (targetState) {
            is DownloadStatus.Idle -> {}
            is DownloadStatus.Loading -> {
                LoadingCard(
                    state = targetState,
                    onCancel = { downloaderViewModel.cancelButton(context) }
                )
            }
            is DownloadStatus.Downloading -> {
                FinishedCard(
                    targetState.metadata,
                    targetState.downloadProgress,
                    onCancel = { downloaderViewModel.cancelButton(context) }
                )
            }
            is DownloadStatus.Loaded -> {
                FinishedCard(
                    targetState.metadata,
                    onCancel = { downloaderViewModel.cancelButton(context) }
                )
            }
            is DownloadStatus.Error -> {
                ErrorCard(
                    errorMessage = targetState.errorMessage,
                    rawError = targetState.rawError,
                    onCancel = { downloaderViewModel.cancelButton(context) }
                )
            }
        }
    }
}

