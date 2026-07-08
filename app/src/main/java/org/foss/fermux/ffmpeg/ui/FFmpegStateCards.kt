package org.foss.fermux.ffmpeg.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import org.foss.fermux.ffmpeg.logic.FFmpegStatus
import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ui.theme.JetbrainsMono

@Composable
fun FFmepgState (state: FFmpegStatus, FFmpegLogs: String) {

    when (state) {
        is FFmpegStatus.Idle -> {
            IdleCard()
        }

        is FFmpegStatus.Converting -> {
            ConversionCard(state.progress, state.inputUri, FFmpegLogs)
        }

        is FFmpegStatus.Loaded -> {
            ConversionCard(progress = 100f, state.inputUri, FFmpegLogs)
        }

        is FFmpegStatus.Error -> {
            Text(state.errorMessage)
        }
    }
}


@Composable
fun IdleCard(@SuppressLint("ContextCastToActivity") viewModel: FFmpegViewModel = viewModel(viewModelStoreOwner =
LocalContext.current as ComponentActivity)) {

    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)
    var pickedFile by remember { mutableStateOf<Uri?>(null) }
    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> pickedFile = uri }
    var selectedFormat by remember { mutableStateOf(FFmpegTargetFormat.MP4) }

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF1f2034))
                            .aspectRatio(16 / 9f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        ElevatedButton(
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonColors(
                                contentColor = Color(0xFF1f2038),
                                containerColor = Color(0xFF303258),
                                disabledContentColor = Color.Unspecified,
                                disabledContainerColor = Color.Unspecified
                            ),
                            border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),
                            modifier = Modifier
                                .padding(5.dp)
                                .size(85.dp),
                            onClick = { fileLauncher.launch("*/*") }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Upload,
                                tint = Color(0xFFE7E7E2),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(33.dp)
                            )
                        }
                    }
                }
        )
        pickedFile?.let { uri ->

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
                        // TODO. Add buttons here to specify what format the user picks
                    }
                }

        }
    }
}

@Composable
fun ConversionCard(progress: Float? = null, pickedFileUri: Uri?, FFmpegLogs: String) {

    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
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
                            TextButton({ expanded = !expanded }) {
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

