@file:Suppress("DEPRECATION")

package org.foss.fermux.ytdlp.ui.ytdlpMainScreen

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.exoplayer.offline.Download
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import org.foss.fermux.fermuxUIComponents.buttons.AppIconButton
import org.foss.fermux.fermuxUIComponents.buttons.GlobalCancelButton
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
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
 *
 * 
 */


@Composable
fun DownloadContent(
     @SuppressLint("ContextCastToActivity")
     downloaderViewModel: DownloaderViewModel =
          viewModel(
               viewModelStoreOwner =
                    LocalContext.current as ComponentActivity
          ),
     navController: NavController
) {

     val doingTask =
          downloaderViewModel.state is DownloadStatus.Loading || downloaderViewModel.state is DownloadStatus.Downloading
     val clipboard = LocalClipboardManager.current

     Box(modifier = Modifier.fillMaxSize()) {
          Column(
               modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .imePadding()
                    .background(FermuxColors.fermuxBackground)
          ) {

               Text(
                    text = "Note: always update your version of the downloader in the settings",
                    color = FermuxColors.fermuxBackgroundTextColor,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Default,
                    modifier = Modifier.padding(7.dp)
               )

               DownloaderCards(downloaderViewModel.state, downloaderViewModel, navController = navController)

               Spacer(modifier = Modifier.height(10.dp))

               Box(modifier = Modifier.wrapContentSize()) {
                    OutlinedTextField(
                         modifier = Modifier
                              .fillMaxWidth()
                              .padding(15.dp),
                         value = downloaderViewModel.downloadUrl,
                         minLines = 1,
                         maxLines = 7,
                         colors = OutlinedTextFieldDefaults.colors(
                              focusedBorderColor = FermuxColors.fermuxPrimaryBorder,
                              unfocusedBorderColor = FermuxColors.fermuxPrimaryBorder,
                              focusedLabelColor = FermuxColors.fermuxPrimaryBorder,
                              unfocusedLabelColor = FermuxColors.fermuxTextColorBackground,
                              cursorColor = FermuxColors.fermuxGenericBorder,
                              focusedTextColor = Color.White,
                              unfocusedTextColor = Color.White,
                              unfocusedContainerColor = FermuxColors.fermuxComponents,
                              focusedContainerColor = FermuxColors.fermuxSaturatedComponents
                         ),
                         onValueChange = { txt -> downloaderViewModel.downloadUrl = txt },
                         placeholder = {
                              Text(
                                   text = "Type URL here",
                                   fontFamily = FontFamily.Default,
                                   textAlign = TextAlign.Start,
                                   color = FermuxColors.fermuxTextColorBackground,
                                   modifier = Modifier.padding(start = 9.dp, bottom = 5.dp)
                              )
                         },
                         trailingIcon = {

                              androidx.compose.animation.AnimatedVisibility(
                                   visible = downloaderViewModel.downloadUrl.isNotEmpty(),
                                   enter = expandVertically(tween(70)) + fadeIn(tween(100)),
                                   exit = shrinkVertically(tween(70)) + fadeOut(tween(100))
                              ) {
                                   GlobalCancelButton(
                                        modifier = Modifier
                                             .size(40.dp)
                                             .padding(end = 3.dp),
                                        onClick = {
                                             downloaderViewModel.downloadUrl = ""
                                        }
                                   )
                              }
                         },
                         keyboardOptions = KeyboardOptions(
                              imeAction = ImeAction.Send,
                              capitalization = KeyboardCapitalization.None,
                              autoCorrect = false
                         ),
                    )
               }
          }

          Box(
               contentAlignment = Alignment.BottomEnd,
               modifier = Modifier
                    .fillMaxSize()
          ) {

               Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                    // ClipBoard Button
                    AppIconButton(
                         icon = Icons.Default.ContentPaste,
                         modifier = Modifier.size(70.dp).padding(6.dp),
                         onClick = { clipboard.getText()?.text?.let { downloaderViewModel.downloadUrl = it } }
                    )
                    // Download Button
                    AppIconButton(
                         icon = Icons.Default.FileDownload,
                         enabled = !doingTask,
                         modifier = Modifier.size(70.dp).padding(6.dp),
                         onClick = { downloaderViewModel.fetchedMetadata(downloaderViewModel.downloadUrl) }
                    )
               }
          }
     }
}

@Composable
fun DownloaderScreen(navController: NavHostController) {
     var currentPage by remember { mutableStateOf(Page.DownloadPage) }

     LargeTopBarScaffold(
          title = "Downloader",
          onBack = {
               navController.popBackStack()
          },
     ) { innerPadding ->
          Box(
               modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(FermuxColors.fermuxBackground),
          ) {
               when (currentPage) {
                    Page.DownloadPage -> DownloadContent(navController = navController)
                    Page.AudioListPage -> DownloadedAudioScreen()
                    Page.VideoListPage -> DownloadVideoList()
               }

               SideBar(
                    onPageSelected = { currentPage = it }, // TODO. Make the sidebar naviagte to actual pages and not draw on the existing downloader tab
               )
          }
     }
}
