package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderCards

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.fermuxUIComponents.buttons.FermuxCancelButton
import org.foss.fermux.fermuxUIComponents.buttons.FermuxTextWithIconButton
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxCard
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxSurface
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono

@SuppressLint("SuspiciousIndentation")
@Composable
fun ErrorCard(
     errorMessage: String,
     rawError: String,
     onCancel: () -> Unit
) {
     var expanded by remember { mutableStateOf(false) }

     Column(
          modifier = Modifier.fillMaxWidth()
     ) {
          FermuxCard(
               modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
               shape = RoundedCornerShape(12.dp),
          ) {
               Box(
                    modifier = Modifier
                         .fillMaxSize()
                         .aspectRatio(16 / 9f)
                         .background(FermuxColors.fermuxSurface)
               ) {
                    Text(
                         text = errorMessage,
                         fontSize = 14.sp,
                         fontStyle = FontStyle.Normal,
                         fontWeight = FontWeight.SemiBold,
                         fontFamily = FontFamily.Default,
                         color = FermuxColors.fermuxInActiveTextColor,
                         modifier = Modifier
                              .align(Alignment.TopCenter)
                              .padding(18.dp)
                    )
                    FermuxCancelButton(
                         modifier = Modifier.align(Alignment.Center).padding(10.dp),
                         onClick = { onCancel() }
                    )

                    // https://youtu.be/ZFSN40r--zk?si=4dAv5tCwn_Y4NZgF

                    FermuxTextWithIconButton(
                         modifier = Modifier.align(Alignment.BottomStart).padding(10.dp),
                         icon = Icons.Default.ExpandMore,
                         contentPadding = PaddingValues(8.dp),
                         iconRotation = if (expanded) 180f else 0f,
                         text = if (expanded) "Hide error" else "Show error",
                         onClick = { expanded = !expanded }
                    )
               }

               FermuxSurface(
                    expanded = expanded
               ) {
                    Box(
                         modifier = Modifier
                              .fillMaxWidth()
                              .heightIn(max = 200.dp)
                              .verticalScroll(rememberScrollState())
                    ) {
                         Text(
                              text = rawError,
                              modifier = Modifier.padding(13.dp),
                              fontSize = 16.sp,
                              color = FermuxColors.fermuxTextError,
                              fontFamily = JetbrainsMono,
                         )
                    }
               }
          }
     }
}