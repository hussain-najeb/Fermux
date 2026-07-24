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
import androidx.compose.foundation.layout.size
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
import org.foss.fermux.fermuxComponents.FermuxImageButton
import org.foss.fermux.fermuxComponents.FermuxMainActionButton
import org.foss.fermux.fermuxComponents.FermuxTextWithIconButton
import org.foss.fermux.ui.theme.FermuxColors
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
        onConfirm = { audio, video -> viewModel.showFormatSheet = false
            viewModel.startingDownload(context, audio, video)
        }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FermuxColors.fermuxBackground)
    ) {

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .imePadding()
                .background(FermuxColors.fermuxBackground)

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
                    focusedBorderColor      = FermuxColors.fermuxPrimaryBorder,
                    unfocusedBorderColor    = FermuxColors.fermuxPrimaryBorder,
                    focusedLabelColor       = FermuxColors.fermuxPrimaryBorder,
                    unfocusedLabelColor     = FermuxColors.fermuxTextColorBackground,
                    cursorColor             = FermuxColors.fermuxPrimaryBorder, // TODO. Have the cursor change color every second.
                    focusedTextColor        = Color.White,
                    unfocusedTextColor      = Color.White,
                    unfocusedContainerColor = FermuxColors.fermuxComponents,
                    focusedContainerColor   = Color(0xFF252740)
                ),


                onValueChange = { txt -> viewModel.downloadUrl = txt },
                placeholder = {
                    Text(
                        text = "Type URL here",
                        fontFamily = FontFamily.Default,
                        textAlign = TextAlign.Start,
                        color = FermuxColors.fermuxTextColorBackground,
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
        ) {

            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {

                // ClipBoard Button
                FermuxMainActionButton(
                    icon = Icons.Default.ContentPaste,
                    modifier = Modifier.size(70.dp).padding(6.dp),
                    onClick = { clipboard.getText()?.text?.let { viewModel.downloadUrl = it } }
                )

                // DownloadButton
                FermuxMainActionButton(
                    icon = Icons.Default.FileDownload,
                    modifier = Modifier.size(70.dp).padding(6.dp),
                    onClick = { viewModel.fetchedMetadata(viewModel.downloadUrl) }
                )
            }
        }
    }
}