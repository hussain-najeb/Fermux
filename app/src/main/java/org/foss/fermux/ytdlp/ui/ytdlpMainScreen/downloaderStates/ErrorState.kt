package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import android.content.ClipData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.components.buttons.ErrorCopyButton
import org.foss.fermux.components.buttons.LogButton
import org.foss.fermux.components.downloaderComponents.DownloaderCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono
import org.foss.fermux.utils.Miscellaneous

@Composable
fun ErrorCard(
     flavourMessage: String,
     rawError: String,
     navController: NavController,
     onCancel: () -> Unit
) {
     val clipboard = LocalClipboard.current
     val scrollState = rememberScrollState()
     val scope = rememberCoroutineScope()

     Column(modifier = Modifier.fillMaxSize()) {

          DownloaderCard(
               errorBackground = true,
               modifier = Modifier.aspectRatio(16f / 9f),
               border = BorderStroke(1.dp, FermuxColors.fermuxWhiteColor)
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
                                   text = flavourMessage,
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
               }
          }
          Row(
               modifier = Modifier.fillMaxWidth(),
               horizontalArrangement = Arrangement.SpaceBetween,
          ) {
               Row {
                    LogButton(
                         modifier = Modifier.padding(start = 15.dp, end = 10.dp),
                         onClick = { navController.navigate(Miscellaneous.DownloaderLogs.route) }
                    )
                    ErrorCopyButton(
                         modifier = Modifier.padding(end = 10.dp),
                         onClick = {
                              scope.launch {
                                   val clipData = ClipData.newPlainText("raw error", rawError)
                                   clipboard.setClipEntry(ClipEntry(clipData))
                              }
                         }
                    )
               }
          }

          CancelButton(
               modifier = Modifier.padding(end = 15.dp),
               onClick = onCancel
          )
     }
}

@Preview
@Composable
fun Test2() {
     val navController = rememberNavController()
     ErrorCard(
          flavourMessage = ".....Something About an error??",
          rawError = ".....Imagine This Is An Error.....Imagine This Is An Error.....Imagine This Is An Error.....Imagine This Is An Error.....Imagine This Is An Error.....Imagine This Is An Error.....Imagine This Is An Error",
          onCancel = {},
          navController = navController
     )
}