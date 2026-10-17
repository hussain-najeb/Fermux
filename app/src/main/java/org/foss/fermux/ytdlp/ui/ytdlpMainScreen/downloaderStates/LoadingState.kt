package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.components.downloaderComponents.DownloaderCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.DownloadStatus
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun LoadingCard(
    state: DownloadStatus,
    onCancel: () -> Unit
) {
    val message =
        listOf(
            "Connecting To Some Server...",
            "Fetching Video Info...",
            "Getting Closer...",
            "Analyzing Metadata...",
            "Wrapping Things Up...",
            "Stuff Is Happening...",
            "Hold Your Breath...",
            "Calibrating...",
            "Hopefully This Works...",
            "It's So Close...",
            "Just A Second...",
            "Something Is About To Happen...",
            "Negotiating With The Server...",
            "Asking Nicely...",
            "Convincing Pixels...",
            "Summoning Metadata...",
            "Consulting The Algorithms...",
            "Trust The Process...",
            "Pretending To Be Busy...",
            "Making Some Magic Happen...",
            "The Server Is Thinking...",
            "Almost There... Probably...",
            "Don't Panic...",
            "Still Not Broken...",
            "This Is Completely Normal...",
            "Loading More Loading Messages...",
            "The Bits Are Bit-ing...",
            "Teaching The Server Patience...",
            "Some Assembly Required...",
            "One More Tiny Thing...",
            "Making Progress... Allegedly...",
            "Please Don't Close It...",
            "Blame The Network...",
            "Good Things Take Time...",
            "Something Something Metadata...",
            "Let yt-dlp Cook...",
            "I Swear It's Working...",
            "Any Second Now...",
            "Well, This Is Awkward...",
        )
     var loadingMessage by remember { mutableStateOf(message.random()) }
     val shuffledMessages = (message.shuffled())
     var index by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxWidth()) {

        DownloaderCard(
            modifier = Modifier.aspectRatio(16f / 9f)
        ) {
            LaunchedEffect(Unit) {
                while (true) {
                    delay(4500.milliseconds)
                    index = (index + 1) % shuffledMessages.size
                    loadingMessage = shuffledMessages[index]
                }
            }
            Box(
                modifier =
                    Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = loadingMessage,
                        fontFamily = FontFamily.Default,
                        fontStyle = FontStyle.Italic,
                        color = FermuxColors.offWhiteTextColor,
                        fontSize = 15.sp,
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    LoadingIndicator(color = FermuxColors.fermuxGenericBorder)
                }
                if (state is DownloadStatus.Idle) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = null)
                }
            }
        }
        CancelButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(end = 15.dp),
            onClick = { onCancel() }
        )
    }
}

@Preview
@Composable
fun Test() {
    LoadingCard(
        state = DownloadStatus.LoadingMetadata,
        onCancel = {}
    )
}