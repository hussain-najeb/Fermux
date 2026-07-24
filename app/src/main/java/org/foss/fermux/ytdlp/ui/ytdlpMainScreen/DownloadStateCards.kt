package org.foss.fermux.ytdlp.ui.ytdlpMainScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import org.foss.fermux.fermuxComponents.FermuxCancelButton
import org.foss.fermux.fermuxComponents.FermuxSurface
import org.foss.fermux.fermuxComponents.FermuxTextWithIconButton
import org.foss.fermux.ui.theme.FermuxCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono
import org.foss.fermux.ytdlp.logic.DownloaderViewModel
import org.foss.fermux.ytdlp.logic.downloader.DownloadMetadata
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
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
        FermuxCard(
            cardPadding = 10.dp,
            cardShape = RoundedCornerShape(8.dp),
            aspectRatio = 16f/9f
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
                    "Something is about to happen...",
                )

            var loadingMessage by remember { mutableStateOf(message.random()) }
            val shuffledMessages = (message.shuffled())
            var index by remember { mutableIntStateOf(0) }


            LaunchedEffect(Unit) {
                while (true) {
                    delay(4500.milliseconds)
                    index = (index + 1) % shuffledMessages.size
                    loadingMessage = shuffledMessages[index]
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxSurface)
                    .aspectRatio(16/9f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    Text(
                        text = loadingMessage ,
                        fontFamily = FontFamily.SansSerif,
                        fontStyle = FontStyle.Italic,
                        color = FermuxColors.fermuxInActiveTextColor,
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

fun LoadedCard (
    metadata: DownloadMetadata,
    progress: Float? = null,
    downloaderLogs: String)
{

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ElevatedCard(
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
                        Text(
                            videoTime(metadata.duration),
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.BottomStart),
                            fontFamily = FontFamily.Default
                        )

                        if (progress == 100f) {

                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Download Complete",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.BottomCenter)
                            )

                        }
                    }
                }
            }

            FermuxSurface {
                Column {
                    Text(
                        metadata.title,
                        modifier = Modifier
                            .padding(8.dp),
                        fontFamily = FontFamily.Default
                    )
                    metadata.uploader?.let {
                        Text(
                            it,
                            modifier = Modifier
                                .padding(8.dp),
                            fontFamily = FontFamily.Default
                        )
                    }
                    // TODO. Duration, add it here.
                }
            }


            FermuxSurface(
                expanded = expanded
            ) {
                Box {
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

        FermuxTextWithIconButton(
            modifier = Modifier.size(50.dp),
            icon = Icons.Default.ExpandMore,
            text = if(expanded) "Hide details" else "Show details",
            onClick = { expanded = !expanded }
        )
    }
}

// https://www.youtube.com/watch?v=RqJkd_ndmgY&t=1s1

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
            modifier = Modifier.fillMaxWidth(),
            cardPadding = 10.dp,
            cardShape = RoundedCornerShape(8.dp),
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
                    modifier = Modifier.align(Alignment.Center),
                    iconRotation = if (expanded) 360f else 0f,
                    onClick = { onCancel() }
                )

                FermuxTextWithIconButton(
                    modifier = Modifier.align(Alignment.BottomStart),
                    icon = Icons.Default.ExpandMore,
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