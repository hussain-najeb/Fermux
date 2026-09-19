@file:Suppress("unused")
package org.foss.fermux.settings.ui.downloader

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.SettingsResetButton
import org.foss.fermux.fermuxUIComponents.downloaderComponents.*
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxSnackBar
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.fermuxUIComponents.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.rememberNotificationPermissionRequest
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode


private enum class ExpandableDownloaderSetting {
     YtdlpUpdater,
     SponsorBlock,
     Aria2c,
     SleepRequest,
     ExternalDownloader,
     ResetDownloader
}

@Composable
fun SimpleDownloaderPage(
     navController: NavHostController,
     @SuppressLint("ContextCastToActivity")
     downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {

     val sleepRequest by downloaderSettingsViewModel.sleepRequest.collectAsStateWithLifecycle()
     val ytdlpDetails by downloaderSettingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()
     val audioHistory by downloaderSettingsViewModel.audioHistory.collectAsStateWithLifecycle()
     val videoHistory by downloaderSettingsViewModel.videoHistory.collectAsStateWithLifecycle()
     val currentVersionName by downloaderSettingsViewModel.currentVersionName.collectAsStateWithLifecycle()
     val sponsorBlock by downloaderSettingsViewModel.sponsorBlock.collectAsStateWithLifecycle()
     val quickJS by downloaderSettingsViewModel.quickJS.collectAsStateWithLifecycle()
     val fingerprint by downloaderSettingsViewModel.fingerprint.collectAsStateWithLifecycle()
     val thumbnail by downloaderSettingsViewModel.embedThumbnail.collectAsStateWithLifecycle()
     val playlist by downloaderSettingsViewModel.playlistState.collectAsStateWithLifecycle()
     val aria2cMode by downloaderSettingsViewModel.aria2cMode.collectAsStateWithLifecycle()
     val logcat by downloaderSettingsViewModel.downloaderDebug.collectAsStateWithLifecycle()
     val bellState by downloaderSettingsViewModel.bellState.collectAsStateWithLifecycle()

     val isCheckingForUpdate by downloaderSettingsViewModel.isCheckingForUpdate.collectAsStateWithLifecycle()
     val ytdlpUpdateStatus by downloaderSettingsViewModel.ytdlpUpdateStatus.collectAsStateWithLifecycle()

     val externalDownloaderEnabled = aria2cMode == Aria2cMode.Disabled
     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()
     var expandedSetting by remember {
          mutableStateOf<ExpandableDownloaderSetting?>(null)
     }
     fun toggleDownloader(setting: ExpandableDownloaderSetting) {
          expandedSetting =
               if (expandedSetting == setting) null else setting
     }
     val requestNotificationPermission = rememberNotificationPermissionRequest(
          onGranted = {
               scope.launch {
                    snackbarHostState.showSnackbar(
                         message = "Permission already granted",
                         duration = SnackbarDuration.Short
                    )
               }
               downloaderSettingsViewModel.setBellState(true)
          }
     )


     val simpleDownloaderSettings = listOf(
          SettingListInfo(
               title = "Update Yt-dlp",
               description = if (isCheckingForUpdate) ytdlpUpdateStatus ?: "Checking for update..." else "Current version is $currentVersionName",
               icon = Icons.Default.Update,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.YtdlpUpdater)
               },
               trailingContent = {
                    DownloaderVersionSwap(
                         downloaderSettingsViewModel = downloaderSettingsViewModel,
                         expanded = expandedSetting == ExpandableDownloaderSetting.YtdlpUpdater
                    )
               },
               position = TilePosition.TOP
          ),
          SettingListInfo(
               title = "Notifications",
               description = "Press to enable notifications",
               image = if (bellState) R.drawable.bell_on else R.drawable.bell_off,
               onClick = requestNotificationPermission
          ),
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
               title = if (logcat) "Debug Logging On" else "Debug Logging Off",
               description = "Write diagnostic messages to Logcat in any builds",
               icon = Icons.Default.BugReport,
               content = {
                    SettingsSwitch(
                         checked = logcat,
                         onCheckedChange = { downloaderSettingsViewModel.setDownloaderDebug(it) }
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
               onClick = { toggleDownloader(ExpandableDownloaderSetting.ExternalDownloader) },
               trailingContent = {
                   ExternalDownloaderSelection(
                         enabled = externalDownloaderEnabled,
                         expanded = expandedSetting == ExpandableDownloaderSetting.ExternalDownloader,
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
               description = "Enabling curl_cffi, this makes a yt-dlp request look like a real browser. This is EXPERIMENTAL",
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
               description = "QuickJS is a JavaScript engine yt-dlp uses to solve youtube JS challenges",
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
          snackbarHost = { FermuxSnackBar(snackbarHostState) }
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
                         shape = setting.position.TileShaper(),
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
                         shape = setting.position.TileShaper(),
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
