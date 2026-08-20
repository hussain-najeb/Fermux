package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import org.foss.fermux.fermuxUIComponents.buttons.CancelButton
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.FFmpegCard
import org.foss.fermux.fermuxUIComponents.generalComponents.AppCard
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.ui.formatStates.FormatList
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun MidConversionProcess(@SuppressLint("ContextCastToActivity") ffmpegViewModel: FFmpegViewModel = viewModel(
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
               .padding(10.dp),
               background = true
               ) {
               if (ffmpegViewModel.inputUri != null) {
                    Box(modifier = Modifier
                         .aspectRatio(16f/9f)
                    ) {
                         AsyncImage(
                              model = ffmpegViewModel.inputUri,
                              contentDescription = null,
                              contentScale = ContentScale.Crop,
                              modifier = Modifier
                                   .fillMaxSize()
                                   .clip(shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp))
                                   .background(FermuxColors.fermuxSurface)
                         )
                        CancelButton(
                         modifier = Modifier
                         .padding(10.dp)
                         .align(Alignment.TopStart) ,
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