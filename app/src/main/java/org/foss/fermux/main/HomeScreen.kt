package org.foss.fermux.main


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.components.generalComponents.MainAppCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.MainScreens
import org.foss.fermux.ytdlp.logic.downloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel
import org.foss.fermux.ytdlp.logic.downloader.ScreenInfo
import org.foss.fermux.ytdlp.ui.quickDownloads.QuickDownloadsStateMachine


// TODO. Make the tab itself have an "enabled" state where its off if quick downs are happening and make its color darker,
//  as in, its not on and the user cant press it, also make it so if there is a download in the main tab, have an "enabled"
//  option for the quick downloads to be off, synchronization is key, these CAN NOT happen asynchronously!
//  also add a "cancel" button


@Composable
fun HomeScreen(navigationController: NavHostController) { // TODO. Add in animation between each transition so its smooth.

     val downloaderViewModel: DownloaderViewModel = viewModel()

     val context = LocalContext.current
     val clipboard = LocalClipboard.current
     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()
     val scroll = rememberScrollState()
     val disabled = downloaderViewModel.state is DownloadStatus.Idle || downloaderViewModel.state is DownloadStatus.Error

     val speed = (downloaderViewModel.state as? DownloadStatus.Downloading)?.downloadProgress ?: 0f
     val iconRotate by animateFloatAsState(
          targetValue = speed,
          animationSpec = tween(),
          label = "Fermux Icon Rotation"
     )

     val screens = listOf(
          ScreenInfo(
               screen = MainScreens.Terminal,
               title = "Terminal",
               description = "A terminal shell with UX, UI, and a lot of convenience taken into account",
               image = R.drawable.bash,
               //buttonIcon = ,
               onClick = {},
               enabled = true
          ),
          ScreenInfo(
               screen = MainScreens.Downloader,
               title = "Downloader",
               description = "A modern implementation of ytdlp to android with powerful additions.",
               image = R.drawable.download,
               buttonIcon = R.drawable.speed,
               iconModifier = Modifier.rotate(iconRotate),
               onClick = {
                    scope.launch {
                         clipboard.getClipEntry()?.clipData?.getItemAt(0)?.text?.toString()
                              .let { text ->
                                   if (text != null) {
                                        downloaderViewModel.downloadUrl = text
                                        downloaderViewModel.quickDownloads()
                                        downloaderViewModel.startingDownload(context)
                                   }
                                   if (text.isNullOrBlank() || text.isEmpty()) {
                                        scope.launch {
                                             snackbarHostState.showSnackbar(
                                                  message = "Your url is empty, copy a url",
                                                  duration = SnackbarDuration.Short
                                             )
                                        }
                                   }
                              }
                    }
               },
               trailingContent = {
                    QuickDownloadsStateMachine(
                         downloaderViewModel.state,
                         downloaderViewModel,
                         navController = navigationController
                    )
               },
               enabled = disabled
          ),
          ScreenInfo(
               screen = MainScreens.Converter,
               title = "Converter",
               description = "A hardware accelerated, powerful conversion tab based on FFmpeg",
               image = R.drawable.ffmpeg,
               //buttonIcon = 4,
               onClick = {},
               enabled = true
          ),
          ScreenInfo(
               screen = MainScreens.Settings,
               title = "Preferences",
               description = "An extensive Preferences tab for all your options",
               image = R.drawable.prefs,
               //buttonIcon = 4,
               onClick = {},
               enabled = true
          ),
     )

     LargeTopBarScaffold(
          title = "Home Page",
          snackbarHost = { AppSnackBar(snackbarHostState) }
     ) { innerPadding ->
          Column(
               modifier = Modifier
                    .fillMaxWidth()
                    .background(FermuxColors.background)
                    .verticalScroll(scroll)
                    .padding(top = 10.dp)
                    .padding(innerPadding),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
          ) {
               screens.forEach { screen ->
                    MainAppCard(
                         modifier = Modifier
                              .fillMaxWidth()
                              .padding(start = 5.dp, end = 5.dp, bottom = 3.dp, top = 3.dp),
                         iconModifier = screen.iconModifier,
                         title = screen.title,
                         description = screen.description,
                         image = screen.image,
                         buttonImage = screen.buttonIcon,
                         buttonOnClick = screen.onClick,
                         route = screen.screen,
                         enabled = screen.enabled,
                         trailingContent = screen.trailingContent,
                         navController = navigationController
                    )
               }
          }
     }
}