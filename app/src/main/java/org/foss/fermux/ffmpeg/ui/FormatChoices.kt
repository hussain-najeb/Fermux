package org.foss.fermux.ffmpeg.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.FormatLists
import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
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

@SuppressLint("SuspiciousIndentation")
@Composable
fun IdleConversionState(onPick: (MediaKind) -> Unit) {

     val formatOptions = listOf(
          FormatListItem(
               title = "Audio",
               description = "Convert the selected media to audio",
               image = R.drawable.audio,
               onClick = { onPick(MediaKind.AUDIO) }
          ),
          FormatListItem(
               title = "Video",
               description = "Convert the selected media to video",
               image = R.drawable.video,
               onClick = { onPick(MediaKind.VIDEO) }
          ),
          FormatListItem(
               title = "Image",
               description = "Convert selected media to image",
               image = R.drawable.image,
               onClick = { onPick(MediaKind.IMAGE) }
          )
     )
     Column(modifier = Modifier.fillMaxSize()) {
          formatOptions.forEach { option ->
               FormatLists(
                    title = option.title,
                    description = option.description,
                    onClick = option.onClick
               )
          }
     }
}

@Composable
fun AudioConversionState(ffmpegViewModel: FFmpegViewModel, onBack: () -> Unit) {

     val context = LocalContext.current

     LaunchedEffect(ffmpegViewModel.inputUri) {
          ffmpegViewModel.updateInputKind(context)
     }

     val audioOptions = listOf(
          FormatListItem(
               title = "MP3",
               description = "Best compatible format",
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
     Column(modifier = Modifier.fillMaxSize()) {
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

@Composable
fun VideoConversionState(ffmpegViewModel: FFmpegViewModel, onBack: () -> Unit) {

     val context = LocalContext.current

     LaunchedEffect(ffmpegViewModel.inputUri) {
          ffmpegViewModel.updateInputKind(context)
     }

     val videoOption = listOf(
          FormatListItem(
               title = "MP4",
               description = "Best over all format for video",
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
          ),
          FormatListItem(
               title = "MKV",
               description = "MKV is a video container that holds movies, tv shows, and all their audio tracks together",
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
          ),
          FormatListItem(
               title = "MOV",
               description = "MOV is Apple’s equivalent to MKV, it is a video container",
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
          ),
          FormatListItem(
               title = "AVI",
               description = "AVI is Microsoft's old-school equivalent to MKV and MOV, it is a video container from 1992",
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
          ),
          FormatListItem(
               title = "WEBM",
               description = "WEBM is a modern, royalty-free video container created by Google specifically for the web",
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
          ),
     )
     Column(modifier = Modifier.fillMaxSize()) {
          FormatLists(
               title = "Back",
               description = "Choose a different media kind",
               onClick = onBack
          )
          videoOption.forEach { option ->
               FormatLists(
                    title = option.title,
                    description = option.description,
                    onClick = option.onClick
               )
          }
     }
}


@Composable
fun ImageConversionState(ffmpegViewModel: FFmpegViewModel, onBack: () -> Unit) {

     val context = LocalContext.current

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
     Column(modifier = Modifier.fillMaxSize()) {
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