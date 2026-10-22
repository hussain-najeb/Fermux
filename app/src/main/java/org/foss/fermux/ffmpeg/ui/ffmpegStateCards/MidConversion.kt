package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.components.ffmpegComponents.FFmpegCard
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.ui.MediaThumbnailImage
import org.foss.fermux.ffmpeg.ui.formatStates.FormatList
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun MidConversionProcess(
     ffmpegViewModel: FFmpegViewModel = viewModel(
          viewModelStoreOwner = LocalActivity.current as ComponentActivity
     )
) {
     val context = LocalContext.current

     Column(
          modifier = Modifier
               .fillMaxWidth()
     ) {
          FFmpegCard(
               modifier = Modifier
                    .padding(15.dp)
          ) {
               if (ffmpegViewModel.inputUri != null) {
                    Box(
                         modifier = Modifier
                              .fillMaxWidth()
                    ) {
                         MediaThumbnailImage(
                              uri = ffmpegViewModel.inputUri,
                              contentScale = ContentScale.None,
                              modifier = Modifier
                                   .fillMaxWidth()
                                   .background(FermuxColors.fermuxSurface)
                                   .aspectRatio(16f/9f)
                         )
                    }
               }
          }
          Column(
               modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
          ) {
               FormatList(ffmpegViewModel)
          }
     }
}