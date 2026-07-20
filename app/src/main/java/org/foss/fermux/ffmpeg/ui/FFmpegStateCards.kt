@file:Suppress("LocalVariableName")

package org.foss.fermux.ffmpeg.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import org.foss.fermux.ffmpeg.logic.FFmpegStatus
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.main.Screen
import org.foss.fermux.ui.theme.FermuxButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.FermuxSurface
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
                containerColor = FermuxColors.fermuxBackground,
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            ),
            border = BorderStroke(1.5.dp, FermuxColors.fermuxPrimaryBorder),
            content = {
                Column {
                    Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {

                        if (viewModel.inputUri == null) {
                            Column(
                                modifier = Modifier.fillMaxSize().background(FermuxColors.fermuxSurface),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

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
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                icon = Icons.Default.ExpandMore,
                                text = if (expanded) "Hide details" else "Show details",
                                clickable = {expanded = !expanded},
                            )
                        }
                    }
                    FermuxSurface(expanded = expanded) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            FermuxButton(
                                text = "Audio",
                                buttonSize = 40.dp,
                                clickable = { navigationController?.navigate(Screen.AudioFormatSheet.route) },
                            )
                            FermuxButton(
                                text = "Video",
                                buttonSize = 40.dp,
                                clickable = { navigationController?.navigate(Screen.VideoFormatSheet.route) },
                            )
                            FermuxButton(
                                text = "Image",
                                buttonSize = 40.dp,
                                clickable = { navigationController?.navigate(Screen.ImageFormatSheet.route) },
                            )
                        }
                    }
                }
            }
        )
    }
}


@Composable
fun ConversionCard(progress: Float? = null, pickedFileUri: Uri?, FFmpegLogs: String) {

    var expanded by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
                .clip(RoundedCornerShape(8.dp)),
            colors = CardColors(
                contentColor = Color.Unspecified,
                containerColor = FermuxColors.fermuxSurface,
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            ),
            border = BorderStroke(1.5.dp, FermuxColors.fermuxPrimaryBorder),
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
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.background(FermuxColors.fermuxSurface)
                        ) {

                            FermuxButton(
                                icon = Icons.Default.ExpandMore,
                                text = if (expanded) "Hide details" else "Show details",
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                isExpanded = expanded,
                                rotation = 180f,
                                clickable = { expanded = !expanded }
                            )
                        }
                    }

                    FermuxSurface(expanded = expanded) {
                        Text(
                            text = FFmpegLogs,
                            modifier = Modifier.padding(3.dp),
                            fontSize = 18.sp,
                            color = Color.White,
                            fontFamily = JetbrainsMono,
                        )
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
                containerColor = FermuxColors.fermuxSurface,
                disabledContentColor = Color.Unspecified,
                disabledContainerColor = Color.Unspecified
            ),
            border = BorderStroke(1.5.dp, FermuxColors.fermuxPrimaryBorder),
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
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(top = 16.dp),
                icon = Icons.Default.Replay,
                clickable = { onTryAgain() }
            )
        }
    }
}