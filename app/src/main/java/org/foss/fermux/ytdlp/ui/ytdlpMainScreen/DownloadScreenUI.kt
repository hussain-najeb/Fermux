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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavController
import org.foss.fermux.fermuxUIComponents.buttons.AppIconButton
import org.foss.fermux.fermuxUIComponents.buttons.GlobalCancelButton
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates.DownloaderCards




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
     val isError = downloaderViewModel.state is DownloadStatus.Error
     val clipboard = LocalClipboardManager.current

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

               Box(modifier = Modifier.fillMaxSize()) {
                    Column(
                         modifier = Modifier
                              .verticalScroll(rememberScrollState())
                              .fillMaxSize()
                              .imePadding()
                              .background(FermuxColors.fermuxBackground)
                    ) {

                         Text(
                              text = "Note: always update your version of the downloader in the settings. It's highly recommended to get the nightly version",
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
                                   isError = isError,
                                   shape = RoundedCornerShape(8.dp),
                                   minLines = 1,
                                   maxLines = 7,
                                   colors = OutlinedTextFieldDefaults.colors( // TODO. Add actual good colors here.
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
                                   modifier = Modifier.size(70.dp).padding(3.dp),
                                   onClick = { clipboard.getText()?.text?.let { downloaderViewModel.downloadUrl = it } }
                              )
                              // Download Button
                              AppIconButton(
                                   icon = Icons.Default.FileDownload,
                                   enabled = !doingTask,
                                   modifier = Modifier.size(70.dp).padding(3.dp),
                                   onClick = { downloaderViewModel.fetchedMetadata(downloaderViewModel.downloadUrl) }
                              )
                         }
                         SideBar(navController = navController, modifier = Modifier.padding(3.dp))
                    }
               }
          }
     }
}
