package org.foss.fermux.settings.ui.downloader

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.SettingsResetButton
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.MediumTopBarScaffold
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.components.settingsComponents.SettingsSwitch
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.rememberNotificationPermissionRequest
import org.foss.fermux.ytdlp.logic.downloader.YtdlpChannel


enum class ExpandableDownloaderSetting {
     YtdlpUpdater,
     SponsorBlock,
     Wifi,
     Ipv,
     Aria2c,
     ExternalDownloader,
     ResetHistory,
     ResetDownloader
}

@Composable
fun SimpleDownloaderPage(
     navController: NavHostController,
     downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
) {
     // DataStore vals
     val ytdlpDetails by downloaderSettingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()
     val audioHistory by downloaderSettingsViewModel.audioHistory.collectAsStateWithLifecycle()
     val videoHistory by downloaderSettingsViewModel.videoHistory.collectAsStateWithLifecycle()
     val currentVersionName by downloaderSettingsViewModel.currentVersionName.collectAsStateWithLifecycle()
     val bellState by downloaderSettingsViewModel.downloaderBellState.collectAsStateWithLifecycle()
     val isCheckingForUpdate by downloaderSettingsViewModel.isCheckingForUpdate.collectAsStateWithLifecycle()
     val ytdlpUpdateStatus by downloaderSettingsViewModel.ytdlpUpdateStatus.collectAsStateWithLifecycle()



     // ModularSegmentedButtons vals
     val updateChannel by downloaderSettingsViewModel.ytdlpChannel.collectAsStateWithLifecycle()

     // Miscellaneous vals/funs
     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()
     var expandedSetting by remember {
          mutableStateOf<ExpandableDownloaderSetting?>(null)
     }

     fun toggleDownloader(setting: ExpandableDownloaderSetting) {
          expandedSetting = if (expandedSetting == setting) null else setting
     }

     val requestNotificationPermission = rememberNotificationPermissionRequest(
          onGranted = {
               scope.launch {
                    snackbarHostState.showSnackbar(
                         message = "Notifications enabled",
                         duration = SnackbarDuration.Short
                    )
               }
          },
          onPermissionDenied = {
               downloaderSettingsViewModel.setDownloaderBellState(false)
               scope.launch {
                    snackbarHostState.showSnackbar(
                         message = "Permission denied",
                         duration = SnackbarDuration.Short
                    )
               }
          }
     )

     val simpleDownloaderSettings = listOf(
          SettingListInfo(
               title = "Update Yt-dlp",
               description = if (isCheckingForUpdate) ytdlpUpdateStatus
                    ?: "Checking for update..." else "Current version is $currentVersionName",
               icon = Icons.Default.Update,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.YtdlpUpdater) },
               trailingContent = {
                    ModularSegmentedButtons(
                         expanded = expandedSetting == ExpandableDownloaderSetting.YtdlpUpdater,
                         enabled = !isCheckingForUpdate,
                         optionsList = listOf(
                              YtdlpChannel.Stable to "Stable",
                              YtdlpChannel.Nightly to "Nightly",
                              YtdlpChannel.Master to "Master"
                         ),
                         selectedOption = updateChannel,
                         onOptionSelected = { downloaderSettingsViewModel.checkYtdlpUpdate(it) }
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
               title = "Reset History",
               description = "Reset both of the history cards",
               icon = Icons.Default.SettingsBackupRestore,
               onClick = {
                    toggleDownloader(setting = ExpandableDownloaderSetting.ResetHistory)
               },
               trailingContent = {
                    SettingsResetButton(
                         expanded = expandedSetting == ExpandableDownloaderSetting.ResetHistory,
                         settingText = "Reset History Cards",
                         onClick = {
                              downloaderSettingsViewModel.clearHistory()
                              scope.launch {
                                   snackbarHostState.showSnackbar(
                                        message = "History settings cleared",
                                        duration = SnackbarDuration.Short
                                   )
                              }
                         }
                    )
               }
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




     MediumTopBarScaffold(
          title = "Downloader Settings",
          onBack = { navController.popBackStack() },
          snackbarHost = { AppSnackBar(snackbarHostState) }
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
                    fontSize = 18.sp,
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
                    fontSize = 18.sp,
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               AdvancedDownloaderSettings(
                    downloaderSettingsViewModel,
                    navController
               )

               Spacer(modifier = Modifier.padding(top = 10.dp))
          }
     }
}
