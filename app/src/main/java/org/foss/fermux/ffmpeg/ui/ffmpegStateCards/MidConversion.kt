package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.fermuxUIComponents.buttons.CancelButton
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.FFmpegCard
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.ui.MediaThumbnailImage
import org.foss.fermux.ffmpeg.ui.formatStates.FormatList
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun MidConversionProcess(
     @SuppressLint("ContextCastToActivity") ffmpegViewModel: FFmpegViewModel = viewModel(
          viewModelStoreOwner = LocalContext.current as ComponentActivity
     )
) {
     val context = LocalContext.current

     Column(
          modifier = Modifier
               .fillMaxSize()
     ) {
          FFmpegCard(
               modifier = Modifier
                    .padding(5.dp),
          ) {
               if (ffmpegViewModel.inputUri != null) {
                    Box(
                         modifier = Modifier.aspectRatio(16f / 9f)
                    ) {
                         MediaThumbnailImage(
                              uri = ffmpegViewModel.inputUri,
                              contentScale = ContentScale.Crop,
                              modifier = Modifier
                                   .fillMaxSize()
                                   .background(FermuxColors.fermuxSurface)
                         )

                         CancelButton(
                              modifier = Modifier
                                   .padding(10.dp)
                                   .align(Alignment.TopStart),
                              onClick = { ffmpegViewModel.cancelButton(context) }
                         )
                    }
               }
               Column(
                    modifier = Modifier
                         .wrapContentSize()
                         .background(FermuxColors.fermuxComponents)
               ) {
                    FormatList(ffmpegViewModel)
               }
          }
     }
}