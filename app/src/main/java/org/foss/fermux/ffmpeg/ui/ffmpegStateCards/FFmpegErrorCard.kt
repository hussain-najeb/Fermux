package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.fermuxUIComponents.buttons.CancelButton
import org.foss.fermux.fermuxUIComponents.generalComponents.AppCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono

@Composable
fun FFmpegErrorMassage(errorMessage: String, onReset: () -> Unit) {

     Column(
          modifier = Modifier.fillMaxSize(),
     ) {
          AppCard(
               shape = RoundedCornerShape(8.dp),
               modifier = Modifier.padding(8.dp)
          ) {
               Box(
                    modifier = Modifier
                         .fillMaxWidth()
                         .aspectRatio(16f / 9f),
                    contentAlignment = Alignment.Center
               ) {
                    Column(
                         modifier = Modifier
                              .fillMaxSize()
                              .background(FermuxColors.fermuxSurface),
                         verticalArrangement = Arrangement.Center,
                         horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                         Text(
                              text = errorMessage,
                              modifier = Modifier
                                   .padding(top = 30.dp, start = 16.dp),
                              fontSize = 16.sp,
                              color = Color.White,
                              fontFamily = JetbrainsMono,
                         )

                         CancelButton(
                              modifier = Modifier
                                   .padding(top = 16.dp),
                              onClick = { onReset() }
                         )
                    }
               }
          }
     }
}