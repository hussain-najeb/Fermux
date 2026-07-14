package org.foss.fermux.ffmpeg.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import org.foss.fermux.ffmpeg.logic.FFmpegStatus
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.main.Screen
import org.foss.fermux.ui.theme.FermuxTheme
import org.foss.fermux.ui.theme.JetbrainsMono

@Composable
fun FFmepgState (state: FFmpegStatus, FFmpegLogs: String, navigationController: NavController, viewModel: FFmpegViewModel) {

    when (state) {
        is FFmpegStatus.Idle -> {
            IdleCard(navigationController = navigationController)
        }

        is FFmpegStatus.Converting -> {
            ConversionCard(state.progress, state.inputUri, FFmpegLogs)
        }

        is FFmpegStatus.Loaded -> {
            ConversionCard(progress = 100f, state.inputUri, FFmpegLogs)
        }

        is FFmpegStatus.Error -> {
            FFmpegErrorMassage(state.errorMessage, onTryAgain = { viewModel.state = FFmpegStatus.Idle })
        }
    }
}


@Composable
fun IdleCard(
    @SuppressLint("ContextCastToActivity") viewModel: FFmpegViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity
    ),
    navigationController: NavController? = null,
) {

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        viewModel.inputUri = uri
        if (uri != null) {
            viewModel.updateInputKind(context)
            if (viewModel.inputKind == null) {
                viewModel.fail("Could not determine input kind")
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .clip(RoundedCornerShape(8.dp)),
            colors = CardColors(
                contentColor = Color.Unspecified,
                containerColor = Color(0xFF1f2034),
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            ),
            border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),
            content = {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {

                        if (viewModel.inputUri == null) {
                            Column(
                                modifier = Modifier.fillMaxSize().background(Color(0xFF1f2034)),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ElevatedButton(
                                    onClick = { fileLauncher.launch("*/*") },
                                    modifier = Modifier.size(85.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonColors(
                                        contentColor = Color(0xFF1f2038),
                                        containerColor = Color(0xFF303258),
                                        disabledContentColor = Color.Unspecified,
                                        disabledContainerColor = Color.Unspecified
                                    ),
                                    border = BorderStroke(1.5.dp, Color(0xFF20bf6b))
                                ) {
                                    Icon(Icons.Default.Upload, null, tint = Color(0xFFE7E7E2), modifier = Modifier.size(33.dp))
                                }
                            }
                        } else {}

                        if (viewModel.inputUri != null) {

                            AsyncImage(
                                model = viewModel.inputUri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                                    .background(Color(0xFF1f2034))
                            )

                            FilledTonalButton(
                                onClick = { expanded = !expanded },
                                contentPadding = PaddingValues(8.dp),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF1f2034),
                                    contentColor = Color.White
                                ),
                                modifier = Modifier
                                    .padding(all = 4.dp)
                                    .padding(end = 2.dp)
                                    .align (alignment = Alignment.BottomEnd)
                            ) {
                                Icon(Icons.Default.ExpandMore, null, modifier = Modifier.rotate(rotation))
                                Spacer(Modifier.width(8.dp))
                                Text(if (expanded) "Hide details" else "Show details")
                            }

                        }
                    }

                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeIn(),
                        exit = shrinkVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeOut()
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF181825))
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilledTonalButton(onClick = { navigationController?.navigate(Screen.AudioFormatSheet.route) }) {
                                    Text("Audio")
                                }
                                FilledTonalButton(onClick = { navigationController?.navigate(Screen.VideoFormatSheet.route) }) {
                                    Text("Video")
                                }
                                FilledTonalButton(onClick = { navigationController?.navigate(Screen.ImageFormatSheet.route) }) {
                                    Text("Image")
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun ConversionCard(progress: Float? = null, pickedFileUri: Uri?, FFmpegLogs: String) {

    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    Column(
        modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(RoundedCornerShape(8.dp)),
            colors = CardColors(
                contentColor = Color.Unspecified,
                containerColor = Color(0xFF1f2034),
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            ),
            border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),
            content =
                {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {

                        AsyncImage(
                            model = pickedFileUri,
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
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.background(Color(0xFF2b2a33))
                        ) {
                            FilledTonalButton({ expanded = !expanded }) {
                                Icon(
                                    Icons.Default.ExpandMore,
                                    contentDescription = null,
                                    modifier = Modifier.rotate(rotation)
                                )
                                Text(if (expanded) "Hide details" else "Show details")
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = expanded,
                        enter = expandVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeIn(),
                        exit = shrinkVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeOut()
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(4.dp))
                                .background(color = Color(0xFF181825)),
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {

                                Text(
                                    text = FFmpegLogs,
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
        )
    }
}


@Composable
fun FFmpegErrorMassage(errorMessage: String, onTryAgain: () -> Unit) {

    Column(modifier = Modifier
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(RoundedCornerShape(8.dp)),
            colors = CardColors(
                contentColor = Color.Unspecified,
                containerColor = Color(0xFF1f2034),
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            ),
            border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),

            ) {
            Text(
                text = errorMessage,
                modifier = Modifier
                    .padding(top = 30.dp, start = 16.dp),
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = JetbrainsMono,
            )
            FilledTonalButton(onClick = { onTryAgain() },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(12.dp),
                                colors = ButtonDefaults.filledTonalButtonColors(containerColor = Color(0xFF303258)),
                                border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),

                modifier = Modifier
                    .align (alignment = Alignment.CenterHorizontally)
                    .padding(top = 16.dp)

            ) {
                Icon(imageVector = Icons.Default.Replay,
                    tint = Color(0xFF126ED7),
                    contentDescription = null,
                    modifier = Modifier
                        .size(29.dp)
                    )
            }
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF181825, widthDp = 360, heightDp = 200)
@Composable
fun FFmpegErrorMassagePreview() {
    FermuxTheme(dynamicColor = false) {
        FFmpegErrorMassage(errorMessage = "Conversion failed in the worker", onTryAgain = {})
    }
}