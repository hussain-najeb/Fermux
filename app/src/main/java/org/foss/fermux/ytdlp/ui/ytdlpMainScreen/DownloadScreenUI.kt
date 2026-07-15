@file:Suppress("DEPRECATION")

package org.foss.fermux.ytdlp.ui.ytdlpMainScreen
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.R
import org.foss.fermux.ytdlp.logic.DownloaderViewModel


//Also — imePadding() and verticalScroll
// aren't on this Column yet,
// but you don't need them until the TextField actually opens a
// keyboard and pushes content.
// fix it someday.






@Composable
fun DownloadContent(
    @SuppressLint("ContextCastToActivity") viewModel: DownloaderViewModel = viewModel(viewModelStoreOwner =
        LocalContext.current as ComponentActivity)) {

    val context = LocalContext.current

    val clipboard = LocalClipboardManager.current

    val downloadInteractionSource = remember { MutableInteractionSource() }
    val downloadIsPressed by downloadInteractionSource.collectIsPressedAsState()
    val downloadButtonColors by animateColorAsState(
        if (downloadIsPressed) Color(0xFFadc6ff) else Color(0xFF303258),
        animationSpec = tween (durationMillis = 150)
    )
    val downloadScale by animateFloatAsState(
        targetValue = if (downloadIsPressed) 1.15f else 1.0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
    )

    val copyInteractionSource = remember { MutableInteractionSource() }
    val copyIsPressed by copyInteractionSource.collectIsPressedAsState()
    val copyButtonColors by animateColorAsState(
        if (copyIsPressed) Color(0xFFadc6ff) else Color(0xFF303258),
        animationSpec = tween (durationMillis = 150)
    )
    val copyScale by animateFloatAsState(
        targetValue = if (copyIsPressed) 1.15f else 1.0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
    )

    QualitySheet(
        showSheet = viewModel.showFormatSheet,
        onDismiss = { viewModel.showFormatSheet = false },
        onConfirm = { audio, video ->
            viewModel.showFormatSheet = false
            viewModel.startingDownload(context, audio, video)
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF181825))
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(Color(0xFF181825))

        )

        {

            WhenCards(viewModel.state, downloaderLogs = viewModel.downloaderLogs, viewModel)

            Spacer(modifier = Modifier.height(10.dp))

// TODO. the buttons will disappear if the copied link it too big

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),
                value = viewModel.downloadUrl,
                minLines = 1,
                maxLines = 9,

                // COLORS
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF20bf6b),
                    unfocusedBorderColor = Color(0xFF20B161),
                    focusedLabelColor = Color(0xFF20bf6b),
                    unfocusedLabelColor = Color(0xFF727882),
                    cursorColor = Color(0xFF20bf6b),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color(0xFFE7E7E2),
                    unfocusedContainerColor = Color(0xFF303258),
                    focusedContainerColor = Color(0xFF252740),),


                onValueChange = { txt -> viewModel.downloadUrl = txt },
                placeholder = {
                    Text(
                        text = "Type URL here",
                        fontFamily = FontFamily.Default,
                        textAlign = TextAlign.Start,
                        color = Color(0xFF727882),
                        modifier = Modifier.padding(start = 10.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send,
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false
                ),
            )
        }

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {

            Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {

                FilledTonalButton(
                    modifier = Modifier
                        .padding(10.dp)
                        .graphicsLayer {
                            scaleX = copyScale
                            scaleY = copyScale
                        }
                        .size(74.dp),

                    contentPadding = PaddingValues(0.dp),
                    interactionSource = copyInteractionSource,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = copyButtonColors
                    ),
                    border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),
                    shape = RoundedCornerShape(16.dp),
                    onClick = { clipboard.getText()?.text?.let { viewModel.downloadUrl = it } }

                ) {
                    Icon(
                        imageVector = Icons.Default.ContentPaste,
                        tint = if (copyIsPressed) Color(0xFF102f60) else Color(0xFF727882),
                        contentDescription = null,
                        modifier = Modifier.size(30.dp)
                    )
                }
                FilledTonalButton(
                    modifier = Modifier
                    .padding(10.dp)
                    .graphicsLayer {
                        scaleX = downloadScale
                        scaleY = downloadScale
                    }
                    .size(74.dp),
                    contentPadding = PaddingValues(0.dp),
                    interactionSource = downloadInteractionSource,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = downloadButtonColors
                    ),
                    border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),
                    shape = RoundedCornerShape(16.dp),
                    onClick = { viewModel.fetchedMetadata(viewModel.downloadUrl) })
                {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_file_download_42),
                        tint = if (downloadIsPressed) Color(0xFF102f60) else Color(0xFF727882),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

        }
    }
}