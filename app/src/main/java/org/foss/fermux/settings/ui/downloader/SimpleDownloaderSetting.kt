package org.foss.fermux.settings.ui.downloader

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.SettingsResetButton
import org.foss.fermux.fermuxUIComponents.downloaderComponents.*
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.fermuxUIComponents.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode


private enum class ExpandableDownloaderSetting {
     YtdlpUpdater,
     SponsorBlock,
     Aria2c,
     SleepRequest,
     ExternalDownloaders,
     ResetDownloader
}

@Composable
fun SimpleDownloaderPage(
     navController: NavHostController,
     @SuppressLint("ContextCastToActivity") downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel(
          viewModelStoreOwner = LocalContext.current as ComponentActivity
     )
) {

     val sleepRequest by downloaderSettingsViewModel.sleepRequest.collectAsStateWithLifecycle()
     val ytdlpDetails by downloaderSettingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()
     val audioHistory by downloaderSettingsViewModel.audioHistory.collectAsStateWithLifecycle()
     val videoHistory by downloaderSettingsViewModel.videoHistory.collectAsStateWithLifecycle()
     val isCheckingForUpdate by downloaderSettingsViewModel.isCheckingForUpdate.collectAsStateWithLifecycle()
     val ytdlpUpdateStatus by downloaderSettingsViewModel.ytdlpUpdateStatus.collectAsStateWithLifecycle()
     val currentVersionName by downloaderSettingsViewModel.currentVersionName.collectAsStateWithLifecycle()
     val sponsorBlock by downloaderSettingsViewModel.sponsorBlock.collectAsStateWithLifecycle()
     val quickJS by downloaderSettingsViewModel.quickJS.collectAsStateWithLifecycle()
     val fingerprint by downloaderSettingsViewModel.fingerprint.collectAsStateWithLifecycle()
     val thumbnail by downloaderSettingsViewModel.embedThumbnail.collectAsStateWithLifecycle()
     val debugLoggingEnabled by downloaderSettingsViewModel.debugLoggingEnabled.collectAsStateWithLifecycle()
//   val notificationState by downloaderSettingsViewModel.notificationState.collectAsStateWithLifecycle() // TODO. Add this at some point.
     val playlist by downloaderSettingsViewModel.playlistState.collectAsStateWithLifecycle()


     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()

     val aria2cMode by downloaderSettingsViewModel.aria2cMode.collectAsStateWithLifecycle()
     val externalDownloadersEnabled = aria2cMode == Aria2cMode.Disabled

     var expandedSetting by remember {
          mutableStateOf<ExpandableDownloaderSetting?>(null)
     }
     fun toggleDownloader(setting: ExpandableDownloaderSetting) {
          expandedSetting =
               if (expandedSetting == setting) null else setting
     }

     val simpleDownloaderSettings = listOf(
          SettingListInfo(
               title = "Update Yt-dlp",
               description = if (isCheckingForUpdate) {
                    ytdlpUpdateStatus ?: "Checking for update..."
               } else {
                    "Current version is $currentVersionName"
               },
               icon = Icons.Default.Update,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.YtdlpUpdater) },
               trailingContent = {
                    DownloaderVersionSwap(
                         downloaderSettingsViewModel = downloaderSettingsViewModel,
                         expanded = expandedSetting == ExpandableDownloaderSetting.YtdlpUpdater
                    )
               },
               position = TilePosition.TOP
          ),
//          SettingListInfo(
//               title = "Download Notifications",
//               description = "Notify me when the downloaded files finish downloading",
//               image = if (notificationState) R.drawable.bell_on else R.drawable.bell_off,
//               content = {
//                    SettingsSwitch(
//                         checked = notificationState,
//                         onCheckedChange = { downloaderSettingsViewModel.setNotificationState(it) }
//                    )
//               }
//          ),
          SettingListInfo(
               title = "Audio History",
               description = "Enable/Disable audio history",
               image = if (audioHistory) R.drawable.library_music_on else R.drawable.library_music_off,
               content = {
                    SettingsSwitch(
                         checked = audioHistory, onCheckedChange = { downloaderSettingsViewModel.setAudioHistory(it) })
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Video History",
               description = "Enable/Disable video history",
               image = if (videoHistory) R.drawable.video_library_on else R.drawable.video_library_off,
               content = {
                    SettingsSwitch(
                         checked = videoHistory, onCheckedChange = { downloaderSettingsViewModel.setVideoHistory(it) })
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = if (playlist) "Playlist On" else "Playlist Off",
               description = if (playlist) "Playlists will be downloaded when the url is copied from a playlist" else "Playlists will not be downloaded when the url is copied from a playlist",
               image = if (playlist) R.drawable.playlist_on else R.drawable.playlist_off,
               content = {
                    SettingsSwitch(
                         checked = playlist, onCheckedChange = { downloaderSettingsViewModel.setPlaylistState(it) })
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = if (ytdlpDetails) "Shown Logs" else "Hidden Logs",
               description = if (ytdlpDetails) "Shown the downloader Logs" else "Hidden the downloader Logs",
               image = if (ytdlpDetails) R.drawable.eye_open else R.drawable.eye_closed,
               content = {
                    SettingsSwitch(
                         checked = ytdlpDetails, onCheckedChange = {
                              downloaderSettingsViewModel.setYtdlpDetails(it)
                         }
                    )
               },
               position = TilePosition.BOTTOM
          ),
     )


     val advancedSettings = listOf(
          SettingListInfo(
               title = "Reset Downloader Settings",
               description = "Reset the downloader settings to there original state",
               icon = Icons.Default.SettingsBackupRestore,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.ResetDownloader) },
               trailingContent = {
                    SettingsResetButton(
                         expanded = expandedSetting == ExpandableDownloaderSetting.ResetDownloader,
                         onClick = {
                              downloaderSettingsViewModel.setClearYtdlp()
                              scope.launch {
                                   snackbarHostState.showSnackbar(
                                        message = "Setting is back to default",
                                        duration = SnackbarDuration.Short
                                   )
                              }
                         } //     TODO. Add a way to undo the action
                    )
               },
               position = TilePosition.TOP
          ),
          SettingListInfo(
               title = if (debugLoggingEnabled) "Debug Logging On" else "Debug Logging Off",
               description = "Write diagnostic messages to Logcat in debug builds", // TODO, make this work!
               icon = Icons.Default.BugReport,
               content = {
                    SettingsSwitch(
                         checked = debugLoggingEnabled,
                         onCheckedChange = downloaderSettingsViewModel::setDebugLoggingEnabled
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "SponsorBlock",
               description = "SponsorBlock API integration for cutting promotions when downloading",
               image = R.drawable.sponsorblock,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.SponsorBlock) },
               content = {
                    SettingsSwitch(
                         checked = sponsorBlock, onCheckedChange = { downloaderSettingsViewModel.setSponsorBlock(it) })
               },
               trailingContent = {
                    SponsorBlockChoices(
                         expanded = expandedSetting == ExpandableDownloaderSetting.SponsorBlock,
                         downloaderSettingsViewModel = downloaderSettingsViewModel
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Aria2c",
               description = "Aria2c Implementation for better download speeds, especially for large files. Use the Edge Case option when downloading on the highest setting in the downloader",
               image = R.drawable.layers,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.Aria2c) },
               trailingContent = {
                    Aria2cModeSelector(
                         expanded = expandedSetting == ExpandableDownloaderSetting.Aria2c ,
                         downloaderSettingsViewModel = downloaderSettingsViewModel
                    )
               },
               position = TilePosition.MIDDLE
          ), 
          SettingListInfo(
               title = "Yt-dlp HLS Options",
               description = "Fallback options instead of Aria2, check the one you like if Aria2 is having issues, especially with m3u8 since yt-dlp prefers it's own options over Aria2 recently over security issues",
               image = R.drawable.hls_on,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.ExternalDownloaders) },
               trailingContent = {
                   ExternalDownloaderSelection(
                         enabled = externalDownloadersEnabled,
                         expanded = expandedSetting == ExpandableDownloaderSetting.ExternalDownloaders,
                         downloaderSettingsViewModel = downloaderSettingsViewModel
                         )
                    },
                    position = TilePosition.MIDDLE
               ),
          SettingListInfo(
               title = if (thumbnail) "Uncut Thumbnail" else "Cut Thumbnail",
               description = if (thumbnail) "The thumbnail of the downloaded media will be embedded and will be saved"
               else "The thumbnail of the downloaded media will be removed and won't be saved",
               image = if (thumbnail) R.drawable.scissors_off else R.drawable.scissors_on,
               content = {
                    SettingsSwitch(
                         checked = thumbnail, onCheckedChange = {
                              downloaderSettingsViewModel.setEmbedThumbnail(it)
                         }
                    )
               },
               position = TilePosition.MIDDLE
          ), 
          SettingListInfo(
               title = "Sleep Request Yt-dlp Flag",
               description = "Sleep request is a flag for delayed download between each request, each number represents a second. 0 means the flag is off",
               icon = if (sleepRequest > 0) Icons.Filled.Flag else Icons.Outlined.Flag,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.SleepRequest) },
               trailingContent = {
                    RequestTimeSlider(
                         expanded = expandedSetting == ExpandableDownloaderSetting.SleepRequest
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Impersonation",
               description = "This setting enables curl_cffi, meaning it makes a request look like a real browser. Note that this is an EXPERIMENTAL feature",
               image = if (fingerprint) R.drawable.fingerprint_on else R.drawable.fingerprint_off,
               content = {
                    SettingsSwitch(
                         checked = fingerprint, onCheckedChange = {
                              downloaderSettingsViewModel.setFingerprint(it)
                         }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Quick JS Framework",
               description = "QuickJS is a JavaScript engine yt-dlp uses to solve YouTube's PO token challenges and bypass Google's anti-bot measures",
               image = if (quickJS) R.drawable.flash_on else R.drawable.flash_off,
               content = {
                    SettingsSwitch(
                         checked = quickJS, onCheckedChange = {
                              downloaderSettingsViewModel.setQuickJS(it)
                         }
                    )
               },
               position = TilePosition.BOTTOM
          ),
     )

     LargeTopBarScaffold(
          title = "Downloader Settings",
          onBack = { navController.popBackStack() },
          snackbarHost = {
               SnackbarHost(hostState = snackbarHostState) { data ->
                    val dismissBehavior = rememberSwipeToDismissBoxState(
                         positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
                    )

                    SwipeToDismissBox(
                         state = dismissBehavior,
                         backgroundContent = {},
                         onDismiss = {
                              data.dismiss()
                         }
                    ) {
                         Snackbar(
                              modifier = Modifier
                                   .padding(12.dp)
                                   .border(1.dp, FermuxColors.fermuxGenericBorder, RoundedCornerShape(8.dp)),
                              shape = RoundedCornerShape(8.dp),
                              containerColor = FermuxColors.something3,
                              contentColor = FermuxColors.fermuxWhiteColor,
                              action = data.visuals.actionLabel?.let { label ->
                                   {
                                        TextButton(onClick = { data.performAction() }) {
                                             Text(
                                                  label,
                                                  color = FermuxColors.fermuxWhiteColor,
                                                  textAlign = TextAlign.Center
                                             )
                                        }
                                   }
                              }
                         ) {
                              Text(
                                   text = data.visuals.message,
                                   fontSize = 14.sp,
                                   fontFamily = FontFamily.Default
                              )

                         }
                    }
               }
          }
     ) { paddingValues ->
          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues),
          ) {
               Text(
                    text = "General",
                    modifier = Modifier.padding(
                         start = 16.dp, top = 20.dp, bottom = 8.dp
                    ),
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               simpleDownloaderSettings.forEach { setting ->
                    TileOptions(
                         title = setting.title,
                         description = setting.description,
                         shape = setting.position.toShape(),
                         icon = setting.icon,
                         image = setting.image,
                         content = setting.content,
                         trailingContent = setting.trailingContent,
                         onClick = {
                              setting.onClick?.invoke()
                              setting.route?.let { navController.navigate(it) }
                         },
                    )
               }

               Text(
                    text = "Advanced",
                    modifier = Modifier.padding(
                         start = 16.dp, top = 20.dp, bottom = 8.dp
                    ),
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               advancedSettings.forEach { setting ->
                    TileOptions(
                         title = setting.title,
                         description = setting.description,
                         shape = setting.position.toShape(),
                         icon = setting.icon,
                         image = setting.image,
                         content = setting.content,
                         trailingContent = setting.trailingContent,
                         onClick = {
                              setting.onClick?.invoke()
                              setting.route?.let { navController.navigate(it) }
                         },
                    )
               }
          }
     }
}
