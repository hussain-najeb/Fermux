@file:Suppress("LocalVariableName")

package org.foss.fermux.ffmpeg.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import org.foss.fermux.ffmpeg.logic.FFmpegStatus
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.main.Screen
import org.foss.fermux.ui.theme.*

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

        FermuxCard(
            cardPadding = 10.dp,
            cardShape = RoundedCornerShape(8.dp),
        ) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {

                    if (viewModel.inputUri == null) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(FermuxColors.fermuxSurface),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                           Text(text = "Upload a file to convert",
                               fontSize = 19.sp,
                               fontFamily = FontFamily.Monospace,
                               fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
                               color = FermuxColors.fermuxTextColorInActive,
                               modifier = Modifier.padding(bottom = 8.dp)
                               )

                            FermuxButton(
                                icon = Icons.Default.Upload,
                                clickable = {fileLauncher.launch("*/*")}
                            )
                        }
                    }

                    if (viewModel.inputUri != null) {

                        AsyncImage(
                            model = viewModel.inputUri, //TODO. THIS DOES NOT WORK!
                            contentDescription = null,
                            contentScale = ContentScale.Crop,

                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(16f / 9f)
                                    .background(FermuxColors.fermuxSurface)
                        )

                        FermuxButton(
                            isExpanded = expanded,
                            rotation = 180f,
                            buttonSize = 20.dp,
                            hasMainBorder = false,
                            modifier = Modifier.align(Alignment.BottomStart),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            icon = Icons.Default.ExpandMore,
                            text = if (expanded) "Hide formats" else "Show formats",
                            clickable = {expanded = !expanded},
                            )
                        }
                    }
                    FermuxSurface(expanded = expanded) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalArrangement = Arrangement.SpaceEvenly,
                        ) {
                            Row {
                                FermuxButton(
                                    text = "Audio",
                                    buttonSize = 40.dp,
                                    hasMainBorder = false,
                                    clickable = { navigationController?.navigate(Screen.AudioFormatSheet.route) },
                                )
                                Text(":   Convert selected file to Audio",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Default,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
                                    color = FermuxColors.fermuxTextColorInActive,
                                    modifier = Modifier.padding(top = 27.dp)
                                )
                            }

                            HorizontalDivider(
                                thickness = 1.2.dp,
                                color = FermuxColors.fermuxComponents
                            )

                            Row {
                                FermuxButton(
                                    text = "Video",
                                    buttonSize = 40.dp,
                                    hasMainBorder = false,
                                    clickable = { navigationController?.navigate(Screen.VideoFormatSheet.route) },
                                )
                                Text(":   Convert selected file to Video",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Default,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
                                    color = FermuxColors.fermuxTextColorInActive,
                                    modifier = Modifier.padding(top = 27.dp)
                                )
                            }

                            HorizontalDivider(
                                thickness = 1.2.dp,
                                color = FermuxColors.fermuxComponents
                            )

                            Row {
                                FermuxButton(
                                    text = "Image",
                                    buttonSize = 40.dp,
                                    hasMainBorder = false,
                                    clickable = { navigationController?.navigate(Screen.ImageFormatSheet.route) },
                                )
                                Text(":   Convert selected file to Image",
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Default,
                                    fontStyle = androidx.compose.ui.text.font.FontStyle.Normal,
                                    color = FermuxColors.fermuxTextColorInActive,
                                    modifier = Modifier.padding(top = 27.dp)
                                )
                            }
                        }
                    }
            }
        }
    }


@Composable
fun ConversionCard(progress: Float? = null, pickedFileUri: Uri?, FFmpegLogs: String) {

    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        FermuxCard(
            cardShape = RoundedCornerShape(8.dp),
            cardPadding = 10.dp,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {

                AsyncImage(
                    model = pickedFileUri,
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
                        if (progress == 100f) {

                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Conversion Complete",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.BottomCenter)
                            )
                        }
                        FermuxButton(
                            icon = Icons.Default.ExpandMore,
                            rotation = 180f,
                            iconSize = 18.dp,
                            hasMainBorder = false,
                            text = if (expanded) "Hide logs" else "Show logs",
                            modifier = Modifier.align(Alignment.BottomStart),
                            clickable = { expanded = !expanded }
                        )
                    }
                }
            }

            FermuxSurface(expanded = true) {
                Text(
                    text = FFmpegLogs,
                    modifier = Modifier.padding(3.dp),
                    fontSize = 18.sp,
                    color = Color.White,
                    fontFamily = JetbrainsMono,
                )
            }
        }
    }
}

        @Composable
        fun FFmpegErrorMassage(errorMessage: String, onTryAgain: () -> Unit) {

            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                FermuxCard(
                    cardShape = RoundedCornerShape(8.dp),
                    cardPadding = 10.dp,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(FermuxColors.fermuxSurface),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = errorMessage,
                                modifier = Modifier
                                    .padding(top = 30.dp, start = 16.dp),
                                fontSize = 16.sp,
                                color = Color.White,
                                fontFamily = JetbrainsMono,
                            )

                            FermuxButton(
                                buttonSize = 20.dp,
                                modifier = Modifier
                                    .padding(top = 16.dp),
                                icon = Icons.Default.Replay,
                                clickable = { onTryAgain() }
                            )
                        }
                    }
                }
            }
        }