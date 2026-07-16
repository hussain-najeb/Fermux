@file:Suppress("DEPRECATION")

package org.foss.fermux.ytdlp.ui.ytdlpMainScreen
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.ui.theme.FermuxButtons
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ytdlp.logic.DownloaderViewModel


@Composable
fun DownloadContent(
    @SuppressLint("ContextCastToActivity") viewModel: DownloaderViewModel = viewModel(viewModelStoreOwner =
        LocalContext.current as ComponentActivity)) {

    val context = LocalContext.current

    val clipboard = LocalClipboardManager.current

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
                .imePadding()
                .background(Color(0xFF181825))

        )

        {
            WhenCards(viewModel.state, downloaderLogs = viewModel.downloaderLogs, viewModel)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),
                value = viewModel.downloadUrl,
                minLines = 1,
                maxLines = 9,

                // COLORS of the TextField
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF20bf6b),
                    unfocusedBorderColor = Color(0xFF20B161),
                    focusedLabelColor = Color(0xFF20bf6b),
                    unfocusedLabelColor = Color(0xFF727882),
                    cursorColor = Color(0xFF20bf6b),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color(0xFFE7E7E2),
                    unfocusedContainerColor = Color(0xFF303258),
                    focusedContainerColor = Color(0xFF252740)),


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


                // ClipBoard Button
                FermuxButtons(
                    icon = Icons.Default.ContentPaste,
                    color = FermuxColor(),
                    clickable = { clipboard.getText()?.text?.let { viewModel.downloadUrl = it } }
                )


                // DownloadButton
                FermuxButtons(
                    icon = Icons.Default.FileDownload,
                    color = FermuxColor(),
                    iconSize = 36.dp,
                    clickable = { viewModel.fetchedMetadata(viewModel.downloadUrl) }
                )
            }
        }
    }
}