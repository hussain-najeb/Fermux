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
import org.foss.fermux.fermuxUIComponents.buttons.FermuxCancelButton
import org.foss.fermux.fermuxUIComponents.buttons.FermuxIconButton
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxCard
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxSurface
import org.foss.fermux.fermuxUIComponents.buttons.FermuxTextWithIconButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel
import org.foss.fermux.ytdlp.logic.downloader.DownloadMetadata
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import kotlin.time.Duration.Companion.milliseconds


// TODO. have the download circle be a check
//  mark when the download finished so its apparent
//  to the user the download is done.
// TODO. have the duration of the video with a
//  black/transcperent background behind it, so it doesn't look bad




@Composable
fun WhenCards (state: DownloadStatus,
               downloaderLogs: String,
               viewModel: DownloaderViewModel) {

    val context = LocalContext.current
    when (state) {
        is DownloadStatus.Idle -> {} // Idle state of the card

        is DownloadStatus.Loading -> {
            LoadingCard(state = state,
                onCancel = {viewModel.cancelButton(context)})
        } // while downloading the info to the card

        is DownloadStatus.Downloading -> {
            LoadedCard(state.metadata,
                state.downloadProgress,
                downloaderLogs = downloaderLogs,
                showYtdlpDetails = viewModel.showYtdlpDetails,
                onCancel = {viewModel.cancelButton(context)})
        } // just to get a damn bar to show the progress.

        is DownloadStatus.Loaded -> {
            LoadedCard(state.metadata,
                downloaderLogs = downloaderLogs,
                showYtdlpDetails = viewModel.showYtdlpDetails,
                onCancel = {viewModel.cancelButton(context)})
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
fun LoadingCard(state: DownloadStatus, onCancel: () -> Unit) {

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


    var cancelButton by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        FermuxCard(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(16.dp)
                .aspectRatio(16f/9f)
        ) {
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

                FermuxCancelButton(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart).padding(6.dp),
                    iconRotation = if (cancelButton) 360f else 0f,
                    onClick = { onCancel() },
                )

            }
        }
    }
}
@Composable
fun LoadedCard (
    metadata: DownloadMetadata,
    progress: Float? = null,
    showYtdlpDetails: Boolean = false,
    downloaderLogs: String,
    onCancel: () -> Unit
    )
{

    var cancelButton by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var errorLogs by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        FermuxCard(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

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
                        color = FermuxColors.fermuxGenericBorder,
                        trackColor = FermuxColors.fermuxTertiaryBorder,
                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.BottomEnd)
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

                FermuxCancelButton(
                    modifier = Modifier
                        .align(alignment = Alignment.TopStart).padding(10.dp),
                    iconRotation = if (cancelButton) 360f else 0f,
                    onClick = { onCancel() }
                )
                Box(contentAlignment = Alignment.BottomStart, modifier = Modifier.matchParentSize()) { //TODO. This is two buttons in right buttom, should be left buttom and the time of the video needs to be in the fermux card
                    Column {
                        if (showYtdlpDetails) {
                            FermuxTextWithIconButton(
                                modifier = Modifier.padding(1.dp),
                                icon = Icons.Default.ExpandMore,
                                contentPadding = PaddingValues(8.dp),
                                iconRotation = if (errorLogs) 180f else 0f,
                                text = if (errorLogs) "Hide Logs" else "Show Logs",
                                onClick = { errorLogs = !errorLogs }

                            )
                            FermuxIconButton(
                                modifier = Modifier.padding(1.dp),
                                icon = Icons.Default.ExpandMore,
                                contentPadding = PaddingValues(8.dp),
                                onClick = {  }
                            )
                        }
                    }
                }
            }

            FermuxSurface(expanded = expanded) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = metadata.title,
                        fontFamily = FontFamily.Default,
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(7.dp)
                    )
                    HorizontalDivider(
                        thickness = 1.0.dp,
                        color = FermuxColors.fermuxComponents
                    )
                     metadata.uploader?.let {
                          Text(
                               text = it,
                               fontFamily = FontFamily.Default,
                               fontSize = 12.sp,
                               color = FermuxColors.fermuxTextColorBackground,
                               modifier = Modifier
                                    .padding(7.dp)
                          )
                         HorizontalDivider(
                             thickness = 1.0.dp,
                             color = FermuxColors.fermuxComponents
                         )
                         Text(
                             videoTime(metadata.duration),
                             fontFamily = FontFamily.Default,
                             color = FermuxColors.fermuxTextColorBackground,
                             modifier = Modifier
                                 .padding(8.dp),
                         )
                     }
                }
            }
        }


//        if (showYtdlpDetails) {
//            FermuxSurface(
//                expanded = errorLogs,
//                modifier = Modifier.fillMaxSize().padding(8.dp).height(150.dp)
//            ) {
//                Box {
//                    Text(
//                        downloaderLogs,
//                        modifier = Modifier
//                            .padding(7.dp),
//                        fontSize = 16.sp,
//                        color = Color.White,
//                        fontFamily = JetbrainsMono,
//                    )
//                }
//            }
//        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun ErrorCard(
    errorMessage: String,
    rawError: String,
    onCancel: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var cancelButton by remember { mutableStateOf(false) }

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
                    iconRotation = if (cancelButton) 360f else 0f,
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