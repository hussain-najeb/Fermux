package org.foss.fermux.components.downloaderComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.foss.fermux.R
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.components.buttons.IconTextButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun SeqMetadataCard(
    modifier: Modifier,
    sequentialViewModel: SequentialViewModel,
    onCancelRequest: () -> Unit
) {

    val context = LocalContext.current

    val message = listOf(
        "Works On My Machine...",
        "HTTP 200 In My Heart...",
        "Teaching JSON Some Manners...",
        "Downloading Metadata...",
    )

    var loadingMessage by remember { mutableStateOf(message.random()) }
    val shuffledMessages = (message.shuffled())
    var index by remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .height(100.dp),
            color = FermuxColors.darkPurple,
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, FermuxColors.warmPurple),
            tonalElevation = 12.dp,
            shadowElevation = 12.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .aspectRatio(4f / 3f)
                        .align(Alignment.CenterVertically),
                    shape = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp),
                    color = FermuxColors.mutedBlue
                ) {
                    Box(
                        modifier = Modifier,
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator(
                            modifier = Modifier.size(60.dp),
                            color = FermuxColors.skyBlue
                        )
                    }
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = FermuxColors.darkPurple,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        LaunchedEffect(Unit) {
                            while (true) {
                                delay(4500.milliseconds)
                                index = (index + 1) % shuffledMessages.size
                                loadingMessage = shuffledMessages[index]
                            }
                        }
                        Text(
                            text = loadingMessage,
                            fontFamily = FontFamily.Default,
                            maxLines = 2,
                            minLines = 1,
                            fontStyle = FontStyle.Italic,
                            color = FermuxColors.offWhiteTextColor,
                            fontSize = 14.sp,
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxSize()
                            .weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        CancelButton(
                            modifier = Modifier.padding(2.dp),
                            transparency = true,
                            onClick = onCancelRequest,
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            IconTextButton(
                modifier = Modifier,
                image = R.drawable.cancel_buttons,
                text = "Cancel Queue",
                onClick = {
                    sequentialViewModel.allDestroyer(context)
                }
            )
        }
    }
}
