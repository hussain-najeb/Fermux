@file:Suppress("DEPRECATION")

package org.foss.fermux.ytdlp.ui.ytdlpMainScreen
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
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

    QualitySheet(
        showSheet = viewModel.showFormatSheet,
        onDismiss = { viewModel.showFormatSheet = false },
        onConfirm = { audio, video ->
            viewModel.showFormatSheet = false
            viewModel.startingDownload(context, audio, video)
        }
    )

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .background(Color(0xFF181825))

    )

    {

        WhenCards(viewModel.state, downloaderLogs = viewModel.downloaderLogs)

        Spacer(modifier = Modifier.height(10.dp))


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(9.dp),
            value = viewModel.downloadUrl,
            onValueChange = { txt -> viewModel.downloadUrl = txt },
            label = { Text("Type URL here", fontFamily = FontFamily.Default) },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false
            ),
        )
    }

Box(contentAlignment = Alignment.BottomEnd,
    modifier = Modifier
        .fillMaxSize()
        .padding(3.dp)
    ) {

    Column (verticalArrangement = Arrangement.spacedBy(7.dp)) {

        Button(
            modifier = Modifier
                .padding(10.dp)
                .size(74.dp),

            contentPadding = PaddingValues(0.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = { clipboard.getText()?.text?.let { viewModel.downloadUrl = it } }

        ) {
            Icon(
                imageVector = Icons.Default.ContentPaste,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
            Button(modifier = Modifier
                    .padding(10.dp)
                    .size(74.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(16.dp),
                onClick = { viewModel.fetchedMetadata(viewModel.downloadUrl) })
            {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_file_download_42),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

    }
}