package org.foss.fermux.ffmpeg.ui.formatStates

import androidx.compose.animation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.logic.MediaKind


@Composable
fun FormatList(
     ffmpegViewModel: FFmpegViewModel
) {

     var pickedKind by remember { mutableStateOf(MediaKind.Idle) }
     val spatialSpec = MaterialTheme.motionScheme
     val context = LocalContext.current

     AnimatedContent(
          targetState = pickedKind,
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
     ) { targetState ->
          when (targetState) {
               MediaKind.Idle -> IdleConversionState(
                    onPick = { pickedKind = it }
               )

               MediaKind.Audio -> AudioConversionState(
                    ffmpegViewModel,
                    onBack = { pickedKind = MediaKind.Idle }
               )

               MediaKind.Video -> VideoConversionState(
                    ffmpegViewModel,
                    onBack = { pickedKind = MediaKind.Idle }
               )

               MediaKind.Image -> ImageConversionState(
                    ffmpegViewModel,
                    onBack = { pickedKind = MediaKind.Idle }
               )

               MediaKind.Cancel -> {
                    ffmpegViewModel.cancelButton(context)
               }
          }
     }
}