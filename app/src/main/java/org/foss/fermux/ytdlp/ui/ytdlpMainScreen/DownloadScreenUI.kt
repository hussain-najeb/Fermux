@file:Suppress("DEPRECATION")

package org.foss.fermux.ytdlp.ui.ytdlpMainScreen
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.fermuxUIComponents.buttons.FermuxMainActionButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel
import org.foss.fermux.ytdlp.ui.historyPage.DownloadVideoList
import org.foss.fermux.ytdlp.ui.historyPage.DownloadedAudioScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates.DownloaderCards


enum class Page(val image: ImageVector, val descriptor: String) {
    DownloadPage(Icons.Default.Download, "Download Page"),
    AudioListPage(Icons.Filled.LibraryMusic, "Audio Page"),
    VideoListPage(Icons.Filled.VideoLibrary, "Video Page"),
}

/**
 * The download tab's content: URL input, download/clipboard actions, and the
 * status cards. This is plain content — it fills whatever [Box] the caller
 * (e.g. [DownloaderScreen]) gives it, and does not draw its own background
 * or manage scaffold/sidebar concerns.
 */
@Composable
fun DownloadContent(
    @SuppressLint("ContextCastToActivity") viewModel: DownloaderViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current

    QualitySheet(
        showSheet = viewModel.showFormatSheet,
        onDismiss = { viewModel.showFormatSheet = false },
        onConfirm = { audio, video ->  viewModel.showFormatSheet = false
            viewModel.startingDownload(context, audio, video)
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .imePadding()
                .background(FermuxColors.fermuxBackground)
        ) {

            DownloaderCards(viewModel.state, viewModel)

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(9.dp),
                value = viewModel.downloadUrl,
                minLines = 1,
                maxLines = 2,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor      = FermuxColors.fermuxPrimaryBorder,
                    unfocusedBorderColor    = FermuxColors.fermuxPrimaryBorder,
                    focusedLabelColor       = FermuxColors.fermuxPrimaryBorder,
                    unfocusedLabelColor     = FermuxColors.fermuxTextColorBackground,
                    cursorColor             = FermuxColors.fermuxGenericBorder, // TODO. Have the cursor change color every second.
                    focusedTextColor        = Color.White,
                    unfocusedTextColor      = Color.White,
                    unfocusedContainerColor = FermuxColors.fermuxComponents,
                    focusedContainerColor   = FermuxColors.fermuxSaturatedComponents
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
                    onClick = { viewModel.fetchedMetadata(viewModel.downloadUrl) }  // TODO. Add a way in the fetchMetadata function a try and catch error Log.E
                )
            }
        }
    }
}

@Composable
fun DownloaderScreen(navController: NavHostController) {
    var currentPage by remember { mutableStateOf(Page.DownloadPage) }

    FermuxLargeTopBarScaffold(
        title = "Downloader",
        onBack = { navController.popBackStack() },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(FermuxColors.fermuxBackground),
        ) {
            when (currentPage) {
                Page.DownloadPage -> DownloadContent()
                Page.AudioListPage -> DownloadedAudioScreen()
                Page.VideoListPage -> DownloadVideoList()
            }

            SideBar(
                currentPage = currentPage,
                onPageSelected = { currentPage = it },
            )
        }
    }
}