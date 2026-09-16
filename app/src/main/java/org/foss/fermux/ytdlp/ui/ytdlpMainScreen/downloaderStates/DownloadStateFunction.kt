package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import androidx.compose.animation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.formats.QualityState

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
                    FinishedDownloadCard(
                         targetState.metadata,
                         targetState.downloadProgress,
                         onCancel = { downloaderViewModel.cancelButton(context) },
                         navController = navController
                    )
               }

               is DownloadStatus.Loaded -> {
                    FinishedDownloadCard(
                         targetState.metadata,
                         onCancel = { downloaderViewModel.cancelButton(context) },
                         navController = navController
                    )
               }

               is DownloadStatus.MidChoice -> {
                    QualityState(downloaderViewModel)
               }

               is DownloadStatus.Completed -> {
                    FinishedDownloadCard(
                         targetState.metadata,
                         progress = 100f,
                         onCancel = { downloaderViewModel.cancelButton(context) },
                         navController = navController
                    )
               }

               is DownloadStatus.Error -> {
                    ErrorCard(
                         flavourMessage = targetState.errorMessage,
                         navController = navController,
                         rawError = targetState.rawError,
                         onCancel = { downloaderViewModel.cancelButton(context) }
                    )
               }
          }
     }
}
