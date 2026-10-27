package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import android.content.ClipData
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.IconTextButton
import org.foss.fermux.components.downloaderComponents.DownloaderCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono

@Composable
fun ErrorCard(
    flavourMessage: String,
    rawError: String,
    onCancel: () -> Unit
) {
    val clipboard = LocalClipboard.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        DownloaderCard(
            modifier = Modifier.aspectRatio(16f / 9f)
        ) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Rounded.Error,
                            contentDescription = null,
                            tint = FermuxColors.fermuxLightErrorTextColor,
                            modifier = Modifier
                                .padding(top = 13.dp, start = 6.dp)
                                .size(28.dp)
                        )
                        Text(
                            text = flavourMessage,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Normal,
                            fontFamily = JetbrainsMono,
                            color = FermuxColors.fermuxLightErrorTextColor,
                            modifier = Modifier.padding(top = 22.dp, start = 9.dp)
                        )
                    }
                    Text(
                        text = rawError,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = FontFamily.Default,
                        color = FermuxColors.fermuxLightErrorTextColor,
                        modifier = Modifier.padding(top = 20.dp, start = 12.dp)
                    )
                }
            }
        }
        Box(
             modifier = Modifier.padding(start = 15.dp, end = 15.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                IconTextButton(
                    text = "Return",
                    image = R.drawable.undo,
                    onClick = onCancel
                )
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Column {
                    IconTextButton(
                        modifier = Modifier.padding(bottom = 2.dp),
                        text = "Copy Error",
                        image = R.drawable.copy,
                        onClick = {
                            scope.launch {
                                val clipData = ClipData.newPlainText("raw error", rawError)
                                clipboard.setClipEntry(ClipEntry(clipData))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun Test2() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        ErrorCard(
            flavourMessage = ".....Something About an error??",
            rawError = ".....Imagine This Is An Error.....Imagine This Is An Error.....Imagine This Is An Error.....Imagine This Is An Error.....Imagine This Is An Error.....Imagine This Is An Error.....Imagine This Is An Error",
            onCancel = {},
        )
    }
}