package org.foss.fermux.ffmpeg.ui.formatStates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.R
import org.foss.fermux.components.ffmpegComponents.FFmpegTiles
import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition

@Composable
fun ImageConversionState(ffmpegViewModel: FFmpegViewModel, onBack: () -> Unit) {

     val context = LocalContext.current
     val scrollState = rememberScrollState()


     LaunchedEffect(ffmpegViewModel.inputUri) {
          ffmpegViewModel.updateInputKind(context)
     }


     val imageOptions = listOf(
          SettingListInfo(
               title = "Back",
               description = "Choose a different media kind",
               image = R.drawable.back_arrow,
               onClick = onBack,
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "GIF",
               description = "it is a short, animated image file that plays on an endless loop without any sound",
               image = R.drawable.gif,
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.GIF
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(
                              context,
                              inputUri = uri,
                              targetFormat = FFmpegTargetFormat.GIF
                         )
                    }
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "JPEG",
               description = "JPEG is a compressed digital image format designed to balance small file sizes with high photo quality",
               image = R.drawable.jpg,
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.JPG
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(
                              context,
                              inputUri = uri,
                              targetFormat = FFmpegTargetFormat.JPG
                         )
                    }
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "PNG",
               description = "PNG is an uncompressed image format that supports sharp detail, crisp text, and transparent backgrounds",
               image = R.drawable.png,
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.PNG
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(
                              context,
                              inputUri = uri,
                              targetFormat = FFmpegTargetFormat.PNG
                         )
                    }
               },
               position = TilePosition.MIDDLE
          )
     )
     Column(
          modifier = Modifier
               .fillMaxWidth()
               .verticalScroll(scrollState)
     ) {
          imageOptions.forEach { option ->
               FFmpegTiles(
                    title = option.title,
                    description = option.description,
                    image = option.image,
                    onClick = option.onClick,
                    shape = option.position.TileShaper()
               )
          }
     }
}