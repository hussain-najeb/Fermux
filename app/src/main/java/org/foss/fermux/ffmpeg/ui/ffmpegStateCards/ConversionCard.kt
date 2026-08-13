@file:Suppress("LocalVariableName")

package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.fermuxUIComponents.buttons.TextWithIconButton
import org.foss.fermux.fermuxUIComponents.generalComponents.AppCard
import org.foss.fermux.fermuxUIComponents.generalComponents.AppSurface
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono

@Composable
fun ConversionCard(
     progress: Float? = null,
     pickedFileUri: Uri?,
     FFmpegLogs: String
) {
     var expanded by remember { mutableStateOf(true) }

     Column(
          modifier = Modifier.fillMaxWidth()
     ) {

          AppCard(
               shape = RoundedCornerShape(8.dp),
               modifier = Modifier.padding(8.dp)
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
                              if (progress == 100f) { // TODO. Doesnt work fully for some reason.

                                   Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Conversion Complete",
                                        modifier = Modifier
                                             .padding(8.dp)
                                             .align(Alignment.BottomCenter)
                                   )
                              }
                              TextWithIconButton(
                                   icon = Icons.Default.ExpandMore,
                                   contentPadding = PaddingValues(8.dp),
                                   iconRotation = if (expanded)180f else 0f,
                                   text = if (expanded) "Hide logs" else "Show logs",
                                   modifier = Modifier.align(Alignment.BottomStart),
                                   onClick = { expanded = !expanded }
                              )
                         }
                    }
               }
               AppSurface(expanded = true) {
                    Text(
                         text = FFmpegLogs,
                         modifier = Modifier.padding(3.dp),
                         fontSize = 18.sp,
                         color = Color.White,
                         fontFamily = JetbrainsMono,
                    )
               }
          }
     }
}