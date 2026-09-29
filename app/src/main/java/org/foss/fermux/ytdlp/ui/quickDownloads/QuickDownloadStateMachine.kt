package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.animation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel

@Composable
fun QuickDownloadsStateMachine(
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

               is DownloadStatus.UserArgs -> null

               is DownloadStatus.QuickDownload -> null

               is DownloadStatus.LoadingMetadata -> {
                    QuickDownloadsMetadata()
               }

               is DownloadStatus.Downloading -> {
                    QuickDownloading(
                         progress = targetState.downloadProgress,
                         onCancel = { downloaderViewModel.cancelButton(context) }
                    )
               }

               is DownloadStatus.Completed -> {
                    FinalQuickDownload()
               }

               is DownloadStatus.Error -> {
                    QuickdownloadError(navController = navController)
               }
          }
     }
}
