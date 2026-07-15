package org.foss.fermux.ytdlp.ui.ytdlpMainScreen

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import org.foss.fermux.ui.theme.JetbrainsMono
import org.foss.fermux.ytdlp.logic.DownloaderViewModel
import org.foss.fermux.ytdlp.logic.downloader.DownloadMetadata
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import kotlin.collections.listOf
import kotlin.time.Duration.Companion.milliseconds


// TODO. have the download circle be a check
//  mark when the download finished so its apparent
//  to the user the download is done.
// TODO. have the duration of the video with a
//  black/transcperent background behind it, so it doesn't look bad




@Composable
fun WhenCards (state: DownloadStatus, downloaderLogs: String, viewModel: DownloaderViewModel) {
    val context = LocalContext.current
    when (state) {
        is DownloadStatus.Idle -> {} // Idle state of the card

        is DownloadStatus.Loading -> {
            LoadingCard(state = state)
        } // while downloading the info to the card

        is DownloadStatus.Downloading -> {
            LoadedCard(state.metadata, state.downloadProgress, downloaderLogs = downloaderLogs )
        } // just to get a damn bar to show the progress.

        is DownloadStatus.Loaded -> {
            LoadedCard(state.metadata, downloaderLogs = downloaderLogs)
        }
        // the card gets loaded to view the damn
        // content when you call state. state here is assigned to "metadata", then to the actual card
        // composable later gets to be assigned to "DownloadMetadata" to fill out the
        // info in that data class.

        is DownloadStatus.Error -> {
            ErrorCard(state.errorMessage, state.rawError, onCancel = {viewModel.cancelButton(context)})
        } // if god forbids, an error happens; it's seen here.
    }
}

@SuppressLint("DefaultLocale")
fun videoTime (seconds: Int): String {

    val minutes = seconds / 60
    val second = seconds % 60
    return String.format("%02d:%02d", minutes, second)
}

@Composable
fun LoadingCard(state: DownloadStatus) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(19.dp)
                .border(1.5.dp, Color(0xFF20B161), RoundedCornerShape(8.dp))
                .aspectRatio(16/9f)
                .background(Color(0xFF181825))
        ) {
            val message =
                listOf(
                    "Fetching Video Info",
                    "Connecting to Server...",
                    "Analyzing Metadata...",
                    "Wrapping things up...",
                    "Stuff is happening...",
                    "Hold your breath...",
                    "Calibrating...",
                    "Something is doing something to another thing...",
                )
            var loadingMessage by remember { mutableStateOf(message.random()) }

            val shuffledMessages = (message.shuffled())

            var index by remember { mutableIntStateOf(0) }

            LaunchedEffect(Unit) {
                while (true) {
                    delay(2500.milliseconds)
                    index = (index + 1) % shuffledMessages.size
                    loadingMessage = shuffledMessages[index]
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFF1f2034))
                    .aspectRatio(16/9f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    Text(
                        text = loadingMessage ,
                        fontFamily = FontFamily.SansSerif,
                        fontStyle = FontStyle.Italic,
                        color = Color(0xFFDAF2FC),
                        fontSize = 18.sp,

                        ) // TODO. have it in the middle of the card to the right more, and change the color, reference from the terminal tab.

                    Spacer(modifier = Modifier.height(40.dp))

                    LoadingIndicator()
            }

            if (state is DownloadStatus.Idle) {

                Icon(imageVector = Icons.Default.Close, contentDescription = null)

            }

        }
    }
}
@Composable

fun LoadedCard (metadata: DownloadMetadata, progress: Float? = null, downloaderLogs: String) {
    // for the fully Loaded card to view on screen

    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    Column( // a column to have both the surface and the card be on the same page
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ElevatedCard( // the video card with the picture of the downloaded video
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .fillMaxWidth()
                .padding(19.dp)
        )

        {
            Box {
                AsyncImage(
                    model = metadata.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,

                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color(0xFF1f2034))
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
                        Text(
                            videoTime(metadata.duration),
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.BottomStart),
                            fontFamily = FontFamily.Default
                        )

                    }
                }
            }


            // the surface to get the damn info on screen from the video metadata
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = Color(0xFF181825))

            ) {

                Column {
                    Text(metadata.title,
                        modifier = Modifier
                            .padding(8.dp),
                        fontFamily = FontFamily.Default
                    )
                    metadata.uploader?.let {

                        Text(it,
                            modifier = Modifier
                                .padding(8.dp),
                            fontFamily = FontFamily.Default
                        )
                    }
                }
            }


                Row(
                    modifier = Modifier.background(Color(0xFF2b2a33))
                ) {
                    TextButton({expanded = !expanded},
                        modifier = Modifier.clip(RoundedCornerShape(8.dp)))

                    {
                        Icon(Icons.Default.ExpandMore, contentDescription = null, modifier = Modifier.rotate(rotation))
                        Text(if (expanded)"Hide details" else "Show details")
                    }
                }
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeIn(),
                    exit = shrinkVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeOut()
                )

                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 200.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            downloaderLogs,
                            modifier = Modifier
                                .padding(3.dp),
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = JetbrainsMono,
                        )
                    }
                }
            }
        }
    }


@SuppressLint("SuspiciousIndentation")
@Composable
fun ErrorCard(errorMessage: String, rawError: String, onCancel: () -> Unit) {

    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    val replayInteractionSource = remember { MutableInteractionSource() }

    val replayIsPressed by replayInteractionSource.collectIsPressedAsState()

    val replayButtonColors by animateColorAsState(
        if (replayIsPressed) Color(0xFFadc6ff) else Color(0xFF303258),
        animationSpec = tween (durationMillis = 150)
    )

    val replayScale by animateFloatAsState(
        targetValue = if (replayIsPressed) 1.15f else 1.0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
    )


        Column( // a column to have both the surface and the card be on the same page
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ElevatedCard( // the video card with the picture of the downloaded video
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(19.dp)
                    .border(1.5.dp, Color(0xFF20B161), RoundedCornerShape(8.dp))
                    .aspectRatio(16/9f)
                    .background(Color(0xFF181825)),
            ) {
                Box(modifier = Modifier
                    .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,)
                    {
                    Text(
                        text = errorMessage,
                        fontSize = 14.sp,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        fontFamily = JetbrainsMono,
                        color = Color(0xFFadc6ff),
                        modifier = Modifier.padding(4.dp)
                    )


                            FilledTonalButton(
                                onClick = { onCancel() },
                                shape = RoundedCornerShape(8.dp),
                                interactionSource = replayInteractionSource,
                                contentPadding = PaddingValues(12.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = replayButtonColors
                                ),
                                border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),

                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .graphicsLayer {
                                        scaleX = replayScale
                                        scaleY = replayScale
                                    }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Replay,
                                    tint = Color(0xFF126ED7),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(29.dp)
                                )
                            }
                        }
                }
            }
        }
// TODO. add the cancel button here to reset the thing when the card fails.
    // TODO. The actual ERROR is not shown here, add it with the animations as well.
}
