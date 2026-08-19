@file:Suppress("LocalVariableName")

package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import org.foss.fermux.fermuxUIComponents.buttons.ImageButton
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.FFmpegCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.R
import org.foss.fermux.main.Miscellaneous

@Composable
fun ConversionCard(
     progress: Float? = null,
     pickedFileUri: Uri?,
     navController: NavController
) {


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
                         CircularWavyProgressIndicator(
                              progress = { progress / 100f },
                              color = Color(0xFF2e36aa),
                              trackColor = Color(0xff999bb5),
                              modifier = Modifier
                                   .padding(8.dp)
                                   .align(Alignment.BottomEnd)
                         ).also {
                              if (progress == 100f) { // TODO. make it  look like the damn download card, although, even that one needs work
                                   Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Conversion Complete",
                                        modifier = Modifier
                                             .padding(8.dp)
                                             .align(Alignment.BottomCenter)
                                   )
                              }
                             ImageButton(
                              image = R.drawable.logs,
                              onClick = { navController.navigate(Miscellaneous.FFmpegLog.route)}
                              )
                         }
                    }
               }
          }
     }
}