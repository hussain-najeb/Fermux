package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderQualityFormatState.QualitySheet

@Composable
fun DownloaderCards(
    state: DownloadStatus, 
    downloaderViewModel: DownloaderViewModel,
    navController: NavController
    ) {

    val context = LocalContext.current
    val spatialSpec = MaterialTheme.motionScheme

    AnimatedContent(
        targetState = state,
        transitionSpec = {
            (slideInVertically(
                animationSpec = spatialSpec.slowSpatialSpec(),
                initialOffsetY = { -it }
            ) + fadeIn(initialAlpha = 0.1f))
                .togetherWith(
                exit = slideOutVertically(
                    animationSpec = spatialSpec.slowSpatialSpec(),
                    targetOffsetY = { -it }
                ) + fadeOut(targetAlpha = 0.1f)
            )
        },
        label = "DownloaderCardTransition",
        contentKey = { it::class }
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
                    onCancel = { downloaderViewModel.cancelButton(context) },
                    navController = navController
                )
            }
            is DownloadStatus.Loaded -> {
                FinishedCard(
                    targetState.metadata,
                    onCancel = { downloaderViewModel.cancelButton(context) },
                    navController = navController
                    )
            }
            is DownloadStatus.MidChoice -> {
                    QualitySheet(downloaderViewModel)
            }
            
            is DownloadStatus.Completed -> {
                FinishedCard(
                    targetState.metadata,
                    progress = 100f,
                    onCancel = { downloaderViewModel.cancelButton(context) },
                    navController = navController
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
