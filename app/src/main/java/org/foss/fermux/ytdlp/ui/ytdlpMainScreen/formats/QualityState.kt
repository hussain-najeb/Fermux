package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.formats


import androidx.compose.animation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.utils.allowNotificationPermission
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel
import org.foss.fermux.ytdlp.logic.downloader.FormatKind


@Composable
fun QualityState(downloaderViewModel: DownloaderViewModel) {

     var pickedFormat by remember { mutableStateOf(FormatKind.Idle) }
     val spatialSpec = MaterialTheme.motionScheme
     val context = LocalContext.current

     AnimatedContent(
          targetState = pickedFormat,
          transitionSpec = {
               (slideInVertically(
                    animationSpec = spatialSpec.slowSpatialSpec(),
                    initialOffsetY = { -it }) + fadeIn(
                    initialAlpha = 0.1f
                    )
               ).togetherWith(
                         exit = slideOutVertically(
                              animationSpec = spatialSpec.slowSpatialSpec(),
                              targetOffsetY = { -it }) + fadeOut(targetAlpha = 0.1f)
                    )
          },
          label = "DownloaderQualityCardTransition",
          contentKey = { it }
     ) { targetState ->
          when (targetState) {
               FormatKind.Idle -> IdleQualityChoices(
                    onPick = { pickedFormat = it },
                    onCancel = { downloaderViewModel.cancelButton(context) }
               )
               FormatKind.Audio -> AudioQualityChoices(
                    onBack = { pickedFormat = FormatKind.Idle },
                    onQualitySelected = { quality ->
                         allowNotificationPermission {
                              downloaderViewModel.startingDownload(context, audio = quality, video = null)
                         }
                    }
               )
               FormatKind.Video -> VideoQualityChoices(
                    onBack = { pickedFormat = FormatKind.Idle },
                    onQualitySelected = { quality ->
                         allowNotificationPermission {
                              downloaderViewModel.startingDownload(context, video = quality, audio = null)
                         }
                    }
               )
          }
     }
}
