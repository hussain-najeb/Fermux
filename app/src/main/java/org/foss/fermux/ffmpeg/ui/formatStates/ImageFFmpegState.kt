package org.foss.fermux.ffmpeg.ui.formatStates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.FormatLists
import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.ui.FormatListItem

@Composable
fun ImageConversionState(ffmpegViewModel: FFmpegViewModel, onBack: () -> Unit) {

     val context = LocalContext.current
     val scrollState = rememberScrollState()


     LaunchedEffect(ffmpegViewModel.inputUri) {
          ffmpegViewModel.updateInputKind(context)
     }


     val imageOptions = listOf(
          FormatListItem(
               title = "GIF",
               description = "it is a short, animated image file that plays on an endless loop without any sound",
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.GIF
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(
                              context,
                              inputUri = uri,
                              targetFormat = FFmpegTargetFormat.GIF
                         )
                    }
               }
          ),
          FormatListItem(
               title = "JPEG",
               description = "JPEG is a compressed digital image format designed to balance small file sizes with high photo quality",
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.JPG
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(
                              context,
                              inputUri = uri,
                              targetFormat = FFmpegTargetFormat.JPG
                         )
                    }
               }
          ),
          FormatListItem(
               title = "PNG",
               description = "PNG is an uncompressed image format that supports sharp detail, crisp text, and transparent backgrounds",
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.PNG
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(
                              context,
                              inputUri = uri,
                              targetFormat = FFmpegTargetFormat.PNG
                         )
                    }
               }
          )
     )
     Column(modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(scrollState)
     ) {
          FormatLists(
               title = "Back",
               description = "Choose a different media kind",
               onClick = onBack
          )
          imageOptions.forEach { option ->
               FormatLists(
                    title = option.title,
                    description = option.description,
                    onClick = option.onClick
               )
          }
     }
}