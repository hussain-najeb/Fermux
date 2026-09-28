package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.animation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel

@Composable
fun QuickDownloadsStateMachine(
     state: DownloadStatus,
     downloaderViewModel: DownloaderViewModel,
     snackbarHostState: SnackbarHostState
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

               is DownloadStatus.UserArgs -> {}

               is DownloadStatus.QuickDownload -> {}

               is DownloadStatus.LoadingMetadata -> {
                    QuickDownloadsMetadata()
               }

               is DownloadStatus.Downloading -> {

               }

               is DownloadStatus.Completed -> {

               }

               is DownloadStatus.Error -> {
                    if (downloaderViewModel.downloadUrl.isBlank()) {
                         Text(text = "typs something")
                    }
                    Text(text = downloaderViewModel.flavorError.toString())
               }
          }
     }
}
