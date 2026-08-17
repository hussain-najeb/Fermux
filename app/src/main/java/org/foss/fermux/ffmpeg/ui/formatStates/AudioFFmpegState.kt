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
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.FormatLists
import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel

@Composable
fun AudioConversionState(ffmpegViewModel: FFmpegViewModel, onBack: () -> Unit) {

     val context = LocalContext.current
     val scrollState = rememberScrollState()

     LaunchedEffect(ffmpegViewModel.inputUri) {
          ffmpegViewModel.updateInputKind(context)
     }

     val audioOptions = listOf(
          FormatListItem(
               title = "Back",
               description = "Choose a different media kind",
               image = R.drawable.back_arrow,
               onClick = onBack
          ),
          FormatListItem(
               title = "MP3",
               description = "Best compatible format",
               image = R.drawable.mp3,
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.MP3
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(context, uri, FFmpegTargetFormat.MP3)
                    }
               }
          ),
          FormatListItem(
               title = "FLAC",
               description = "Flac is a lossless audio format with a big files size",
               image = R.drawable.flac,
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.FLAC
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(
                              context,
                              inputUri = uri,
                              targetFormat = FFmpegTargetFormat.FLAC
                         )
                    }
               }
          ),
          FormatListItem(
               title = "WAV",
               description = "A WAV file is like a digital photocopy of a sound, It gives you the highest possible audio quality, but the a MASSIVE file size",
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.WAV
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(
                              context,
                              inputUri = uri,
                              targetFormat = FFmpegTargetFormat.WAV
                         )
                    }
               }
          ),
          FormatListItem(
               title = "OGG",
               description = "Lowest quality, with a much lower file size",
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.OGG
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(
                              context,
                              inputUri = uri,
                              targetFormat = FFmpegTargetFormat.OGG
                         )
                    }
               }
          ),
          FormatListItem(
               title = "M4A",
               description = "M4A is a modern, high-efficiency format that is smaller than WAV and FLAC and better quality than OGG",
               onClick = {
                    ffmpegViewModel.selectedFormat = FFmpegTargetFormat.M4A
                    ffmpegViewModel.inputUri?.let { uri ->
                         ffmpegViewModel.startingConversion(
                              context,
                              inputUri = uri,
                              targetFormat = FFmpegTargetFormat.M4A
                         )
                    }
               }
          ),
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
          audioOptions.forEach { option ->
               FormatLists(
                    title = option.title,
                    description = option.description,
                    onClick = option.onClick
               )
          }
     }
}