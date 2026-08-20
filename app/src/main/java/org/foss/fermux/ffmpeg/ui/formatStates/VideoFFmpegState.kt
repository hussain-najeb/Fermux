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
fun VideoConversionState(ffmpegViewModel: FFmpegViewModel, onBack: () -> Unit) {

     val context = LocalContext.current
     val scrollState = rememberScrollState()


     LaunchedEffect(ffmpegViewModel.inputUri) {
          ffmpegViewModel.updateInputKind(context)
     }

     val videoOption = buildList {
          add(
               FormatListItem(
                    title = "Back",
                    description = "Choose a different media kind",
                    image = R.drawable.back_arrow,
                    onClick = onBack
               )
          )
          if (ffmpegViewModel.isVideoTargetSupported(FFmpegTargetFormat.MP4)) {
               add(
                    FormatListItem(
                         title = "MP4",
                         description = "Best over all format for video and size",
                         image = R.drawable.mp4,
                         onClick = {
                              ffmpegViewModel.selectedFormat = FFmpegTargetFormat.MP4
                              ffmpegViewModel.inputUri?.let { uri ->
                                   ffmpegViewModel.startingConversion(
                                        context,
                                        inputUri = uri,
                                        targetFormat = FFmpegTargetFormat.MP4
                                   )
                              }
                         }
                    )
               )
          }
          add(
               FormatListItem(
                    title = "MKV",
                    description = "MKV is a video container that holds movies, tv shows, and all their audio tracks together",
                    image = R.drawable.mkv,
                    onClick = {
                         ffmpegViewModel.selectedFormat = FFmpegTargetFormat.MKV
                         ffmpegViewModel.inputUri?.let { uri ->
                              ffmpegViewModel.startingConversion(
                                   context,
                                   inputUri = uri,
                                   targetFormat = FFmpegTargetFormat.MKV
                              )
                         }
                    }
               )
          )
          if (ffmpegViewModel.isVideoTargetSupported(FFmpegTargetFormat.MOV)) {
               add(
                    FormatListItem(
                         title = "MOV",
                         description = "MOV is Apple’s equivalent to MKV, it is a video container",
                         image = R.drawable.mov,
                         onClick = {
                              ffmpegViewModel.selectedFormat = FFmpegTargetFormat.MOV
                              ffmpegViewModel.inputUri?.let { uri ->
                                   ffmpegViewModel.startingConversion(
                                        context,
                                        inputUri = uri,
                                        targetFormat = FFmpegTargetFormat.MOV
                                   )
                              }
                         }
                    )
               )
          }
          if (ffmpegViewModel.isVideoTargetSupported(FFmpegTargetFormat.AVI)) {
               add(
                    FormatListItem(
                         title = "AVI",
                         description = "AVI is Microsoft's old-school equivalent to MKV and MOV, it is a video container from 1992",
                         image = R.drawable.avi,
                         onClick = {
                              ffmpegViewModel.selectedFormat = FFmpegTargetFormat.AVI
                              ffmpegViewModel.inputUri?.let { uri ->
                                   ffmpegViewModel.startingConversion(
                                        context,
                                        inputUri = uri,
                                        targetFormat = FFmpegTargetFormat.AVI
                                   )
                              }
                         }
                    )
               )
          }
          if (ffmpegViewModel.isVideoTargetSupported(FFmpegTargetFormat.WEBM)) {
               add(
                    FormatListItem(
                         title = "WEBM",
                         description = "WEBM is a modern, royalty-free video container created by Google specifically for the web",
                         image = R.drawable.webm,
                         onClick = {
                              ffmpegViewModel.selectedFormat = FFmpegTargetFormat.WEBM
                              ffmpegViewModel.inputUri?.let { uri ->
                                   ffmpegViewModel.startingConversion(
                                        context,
                                        inputUri = uri,
                                        targetFormat = FFmpegTargetFormat.WEBM
                                   )
                              }
                         }
                    )
               )
          }
     }

     Column(modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(scrollState)
     ) {
          videoOption.forEach { option ->
               FormatLists(
                    title = option.title,
                    description = option.description,
                    image = option.image,
                    onClick = option.onClick
               )
          }
     }
}