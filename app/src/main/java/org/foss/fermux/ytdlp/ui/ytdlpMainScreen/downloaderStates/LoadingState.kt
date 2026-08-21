package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.foss.fermux.fermuxUIComponents.buttons.CancelButton
import org.foss.fermux.fermuxUIComponents.downloaderComponents.DownloaderCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LoadingCard(
     state: DownloadStatus,
     onCancel: () -> Unit
) {
     val message =
          listOf(
               "Fetching Video Info",
               "Connecting To Server...",
               "Analyzing Metadata...",
               "Wrapping Things Up...",
               "Stuff Is Happening...",
               "Hold Your Breath...",
               "Calibrating...",
               "Hopefully This Works..",
               "It's Close...",
               "Just A Second...",
               "Something Is About To Happen...",
          )
     var loadingMessage by remember { mutableStateOf(message.random()) }
     val shuffledMessages = (message.shuffled())
     var index by remember { mutableIntStateOf(0) }

     Column(modifier = Modifier.fillMaxWidth()) {
          DownloaderCard {
               LaunchedEffect(Unit) {
                    while (true) {
                         delay(4500.milliseconds)
                         index = (index + 1) % shuffledMessages.size
                         loadingMessage = shuffledMessages[index]
                    }
               }
               Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                         Text(
                              text = loadingMessage,
                              fontFamily = FontFamily.Default,
                              fontStyle = FontStyle.Italic,
                              color = FermuxColors.fermuxInActiveTextColor,
                              fontSize = 15.sp,
                         )

                         Spacer(modifier = Modifier.height(40.dp))

                         LoadingIndicator(color = FermuxColors.fermuxGenericBorder)
                    }
                    if (state is DownloadStatus.Idle) {

                         Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                    CancelButton(
                         modifier = Modifier
                              .align(alignment = Alignment.TopStart).padding(6.dp),
                         onClick = { onCancel() }
                    )

               }
          }
     }
}
