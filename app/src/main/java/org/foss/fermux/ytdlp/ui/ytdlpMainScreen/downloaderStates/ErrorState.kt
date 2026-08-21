package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.fermuxUIComponents.buttons.CancelButton
import org.foss.fermux.fermuxUIComponents.buttons.ErrorCopyButton
import org.foss.fermux.fermuxUIComponents.downloaderComponents.DownloaderCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono

@SuppressLint("SuspiciousIndentation")
@Composable
fun ErrorCard(
     errorMessage: String,
     rawError: String,
     onCancel: () -> Unit
) {
     @Suppress("DEPRECATION") val clipboard = LocalClipboardManager.current
     val scrollState = rememberScrollState()

     DownloaderCard(
          errorBackground = true
     ) {
          Box(
               modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 12.dp)
          ) {
               Column(
                    modifier = Modifier
                         .fillMaxSize()
                         .verticalScroll(scrollState)
                         .padding(bottom = 40.dp)
               ) {
                    Row {
                         Icon(
                              imageVector = Icons.Rounded.Error,
                              contentDescription = null,
                              tint = FermuxColors.fermuxLightErrorTextColor,
                              modifier = Modifier
                                   .padding(top = 13.dp, start = 6.dp)
                                   .size(28.dp)
                         )
                         Text(
                              text = errorMessage,
                              fontSize = 12.sp,
                              fontStyle = FontStyle.Normal,
                              fontFamily = JetbrainsMono,
                              color = FermuxColors.fermuxLightErrorTextColor,
                              modifier = Modifier.padding(top = 22.dp, start = 9.dp)
                         )
                    }
                    Text(
                         text = rawError,
                         fontSize = 12.sp,
                         fontStyle = FontStyle.Italic,
                         fontFamily = FontFamily.Default,
                         color = FermuxColors.fermuxLightErrorTextColor,
                         modifier = Modifier.padding(top = 20.dp, start = 12.dp)
                    )
               }
               ErrorCopyButton(
                    modifier = Modifier.align(Alignment.BottomEnd),
                    onClick = { clipboard.setText(AnnotatedString(rawError)) }
               )
               CancelButton(
                    modifier = Modifier
                         .align(Alignment.BottomStart),
                    onClick = {onCancel()}
               )
          }
     }
}
