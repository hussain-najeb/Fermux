@file:Suppress("LocalVariableName")

package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import org.foss.fermux.fermuxUIComponents.buttons.ImageButton
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.FFmpegCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.main.Miscellaneous
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.CancelButton
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel


private enum class ProgressState { InProgress, Done }


@Composable
fun ConversionCard(
     progress: Float? = null,
     pickedFileUri: Uri?,
     navController: NavController
) {

val ffmpegViewModel: FFmpegViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity) 

val context = LocalContext.current


val conversionState = progress?.let {
          if (it >= 100f) ProgressState.Done else ProgressState.InProgress
     }

     Column(
          modifier = Modifier.fillMaxWidth()
     ) {
          FFmpegCard(
               modifier = Modifier.padding(10.dp)
               ) {
               Box(
                    modifier = Modifier
                         .fillMaxWidth(),
                    contentAlignment = Alignment.Center
               ) {
                    AsyncImage(
                         model = pickedFileUri,
                         contentDescription = null,
                         contentScale = ContentScale.Crop,
                         modifier = Modifier
                              .fillMaxWidth()
                              .aspectRatio(16f / 9f)
                              .background(FermuxColors.fermuxSurface)
                    )
                    progress?.let {
                         when (conversionState) {
    ProgressState.InProgress -> Column(
        modifier = Modifier
            .align(Alignment.Center)
            .size(50.dp)
            .background(
                color = FermuxColors.fermuxComponents.copy(alpha = 0.48f),
                shape = RoundedCornerShape(10.dp) 
            )
    ) {
        CircularWavyProgressIndicator(
            progress = { progress / 100f },
            color = FermuxColors.fermuxGenericBorder,
            trackColor = FermuxColors.fermuxTertiaryBorder,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }

               ProgressState.Done -> Column(
                    modifier = Modifier
                    .align(Alignment.Center)
                    .background(
                    color = FermuxColors.fermuxComponents.copy(alpha = 0.48f),
                    shape = RoundedCornerShape(10.dp)
                         )
                     ) {
                          Icon(
                              Icons.Default.Check,
                                   contentDescription = "ffmpeg Success",
                                   modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.CenterHorizontally),
                                    tint = FermuxColors.fermuxWhiteColor
                                    )
                              }

                              null -> Unit
                         }
                             ImageButton(
                              modifier = Modifier
                              .align(Alignment.BottomStart)
                              .padding(10.dp),
                              image = R.drawable.logs,
                              onClick = { navController.navigate(Miscellaneous.FFmpegLog.route) }
                              )
                         }
                         CancelButton(
                         modifier = Modifier
                         .padding(10.dp)
                         .align(Alignment.TopStart) ,
                         onClick = { ffmpegViewModel.cancelButton(context) }
                         )
                    }
               }
          }
     }