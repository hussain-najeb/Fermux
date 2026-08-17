package org.foss.fermux.ffmpeg.ui.formatStates

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.logic.MediaKind


data class FormatListItem(
     val title: String,
     val description: String,
     val image: Int? = null,
     val onClick: () -> Unit
)



@Composable
fun FormatList(ffmpegViewModel: FFmpegViewModel) {

     var pickedKind by remember { mutableStateOf(MediaKind.IDLE) }
     val spatialSpec = MaterialTheme.motionScheme

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
               MediaKind.IDLE -> IdleConversionState(
                    onPick = { pickedKind = it }
               )
               MediaKind.AUDIO -> AudioConversionState(
                    ffmpegViewModel,
                    onBack = { pickedKind = MediaKind.IDLE }
               )
               MediaKind.VIDEO -> VideoConversionState(
                    ffmpegViewModel,
                    onBack = { pickedKind = MediaKind.IDLE }
               )
               MediaKind.IMAGE -> ImageConversionState(
                    ffmpegViewModel,
                    onBack = { pickedKind = MediaKind.IDLE }
               )
          }
     }
}