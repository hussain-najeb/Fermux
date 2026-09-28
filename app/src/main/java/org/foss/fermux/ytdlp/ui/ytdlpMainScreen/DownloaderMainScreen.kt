package org.foss.fermux.ytdlp.ui.ytdlpMainScreen

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.AppIconButton
import org.foss.fermux.components.buttons.GlobalCancelButton
import org.foss.fermux.components.downloaderComponents.SideBar
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.Miscellaneous
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates.DownloaderCards

// TODO. Replace all the mentions of LargeAppTopBar with its small counterpart in not so crucial places
@Composable
fun DownloadContent(
     downloaderViewModel: DownloaderViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity),
     navController: NavController
) {

     val downloaderSettings: DownloaderSettingsViewModel = viewModel()
     val videoConversionWarning by downloaderSettings.videoComp.collectAsStateWithLifecycle()
     val upToDate by downloaderSettings.upToDate.collectAsStateWithLifecycle()
     val currentVersionName by downloaderSettings.currentVersionName.collectAsStateWithLifecycle()

     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()
     val doingTask = downloaderViewModel.state is DownloadStatus.LoadingMetadata || downloaderViewModel.state is DownloadStatus.Downloading || downloaderViewModel.state is DownloadStatus.UserArgs
     val isError = downloaderViewModel.state is DownloadStatus.Error
     val clipboard = LocalClipboard.current



     LargeTopBarScaffold(
          title = "Downloader",
          onBack = { navController.popBackStack() },
          helperButton = { navController.navigate(Miscellaneous.DownloaderArgs.route) },
          helperImage = R.drawable.add,
          snackbarHost = { AppSnackBar(snackbarHostState) }
     ) { innerPadding ->
          Box(
               modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(FermuxColors.fermuxBackground),
          ) {
               Column(
                    modifier = Modifier
                         .verticalScroll(rememberScrollState())
                         .fillMaxSize()
                         .imePadding()
                         .background(FermuxColors.fermuxBackground)
               ) {
                    if (!upToDate) Text(
                              text = "Version $currentVersionName of ytdlp is outdated, update the downloader in the preferences",
                              color = FermuxColors.fermuxOffWhiteTextColor,
                              fontSize = 14.sp,
                              fontStyle = FontStyle.Normal,
                              fontFamily = FontFamily.Default,
                              modifier = Modifier.padding(7.dp)
                    )

                    if (videoConversionWarning) Text(
                         text = "Video Conversion is on, don't cancel the download if it looks stuck.",
                         color = FermuxColors.fermuxWhiteColor,
                         fontSize = 16.sp,
                         fontStyle = FontStyle.Italic,
                         fontFamily = FontFamily.Default,
                         modifier = Modifier.padding(5.dp)
                    )

                    DownloaderCards(
                         downloaderViewModel.state,
                         downloaderViewModel,
                         navController = navController,
                         snackbarHostState = snackbarHostState
                    )

                    Spacer(modifier = Modifier.padding(top = 16.dp))

                    if (!doingTask) Box(modifier = Modifier.wrapContentSize()) {
                         OutlinedTextField(
                              modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp),
                              value = downloaderViewModel.downloadUrl,
                              isError = isError,
                              shape = RoundedCornerShape(8.dp),
                              minLines = 1,
                              maxLines = 7,
                              colors = OutlinedTextFieldDefaults.colors(
                                   focusedBorderColor = FermuxColors.fermuxSecondaryBorder,
                                   unfocusedBorderColor = FermuxColors.fermuxGenericBorder,
                                   focusedLabelColor = FermuxColors.fermuxPrimaryBorder,
                                   unfocusedLabelColor = FermuxColors.fermuxTextColorBackground,
                                   cursorColor = FermuxColors.fermuxGenericBorder,
                                   focusedTextColor = Color.White,
                                   unfocusedTextColor = Color.White,
                                   errorTextColor = FermuxColors.fermuxLightErrorTextColor,
                                   errorBorderColor = FermuxColors.fermuxLightErrorTextColor,
                                   errorLabelColor = FermuxColors.fermuxLightErrorTextColor,
                                   errorCursorColor = FermuxColors.fermuxLightErrorTextColor,
                                   errorContainerColor = FermuxColors.fermuxErrorCardColor,
                                   unfocusedContainerColor = FermuxColors.fermuxComponents,
                                   focusedContainerColor = FermuxColors.inActiveTextField
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
                                             modifier = Modifier.size(40.dp).padding(end = 3.dp), onClick = {
                                                  downloaderViewModel.downloadUrl = ""
                                             }
                                        )
                                   }
                              },
                              keyboardOptions = KeyboardOptions(
                                   imeAction = ImeAction.Send,
                                   capitalization = KeyboardCapitalization.None,
                                   autoCorrectEnabled = false
                              ),
                         )
                    }
               }

               Box(
                    contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxSize()
               ) {

                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                         // ClipBoard Button
                         if (!doingTask) AppIconButton(
                              icon = Icons.Default.ContentPaste,
                              modifier = Modifier.size(60.dp).padding(3.dp),
                              onClick = { scope.launch {
                                   clipboard.getClipEntry()
                                        ?.clipData
                                        ?.getItemAt(0)
                                        ?.text
                                        ?.toString()
                                        .let { text ->
                                             if (text != null) {
                                                  downloaderViewModel.downloadUrl = text
                                             }
                                        }
                                   }
                              }
                         )
                         // Download Button
                         AppIconButton(
                              icon = Icons.Default.FileDownload,
                              enabled = !doingTask,
                              modifier = Modifier.size(60.dp).padding(3.dp),
                              onClick = {
                                   downloaderViewModel.userPickedArgs()
                                   if (downloaderViewModel.downloadUrl.isEmpty()) {
                                        scope.launch {
                                             snackbarHostState.showSnackbar(
                                                  message = "Please enter a URL",
                                                  duration = SnackbarDuration.Short
                                             )
                                        }
                                   }
                              }
                         )
                    }
                    SideBar(navController = navController)
               }
          }
     }
}
