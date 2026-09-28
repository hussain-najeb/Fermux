package org.foss.fermux.settings.ui.downloader

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.SettingsResetButton
import org.foss.fermux.components.downloaderComponents.ModularSegmentedButtons
import org.foss.fermux.components.downloaderComponents.SponsorBlockChoices
import org.foss.fermux.components.settingsComponents.SettingsSwitch
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode
import org.foss.fermux.ytdlp.logic.downloader.Connectivity
import org.foss.fermux.ytdlp.logic.downloader.ExternalDownloaders
import org.foss.fermux.ytdlp.logic.downloader.IpvConnection

@Composable
fun AdvancedDownloaderSettings(
     downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel(),
     navController: NavController
) {

     // DataStore vals
     val sponsorBlock by downloaderSettingsViewModel.sponsorBlock.collectAsStateWithLifecycle()
     val quickJS by downloaderSettingsViewModel.quickJS.collectAsStateWithLifecycle()
     val fingerprint by downloaderSettingsViewModel.fingerprint.collectAsStateWithLifecycle()
     val logcat by downloaderSettingsViewModel.downloaderDebug.collectAsStateWithLifecycle()

     // ModularSegmentedButtons vals
     val aria2cMode by downloaderSettingsViewModel.aria2cMode.collectAsStateWithLifecycle()
     val externalDownloaders by downloaderSettingsViewModel.externalDownloaders.collectAsStateWithLifecycle()
     val aria2cEnabled = externalDownloaders == ExternalDownloaders.Disabled
     val externalDownloadersEnabled = aria2cMode == Aria2cMode.Disabled
     val wifi by downloaderSettingsViewModel.wifi.collectAsStateWithLifecycle()
     val ipv by downloaderSettingsViewModel.ipv.collectAsStateWithLifecycle()

     // Miscellaneous vals/funs
     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()
     var expandedSetting by remember {
          mutableStateOf<ExpandableDownloaderSetting?>(null)
     }

     fun toggleDownloader(setting: ExpandableDownloaderSetting) {
          expandedSetting = if (expandedSetting == setting) null else setting
     }


     val advancedSettings = listOf(
          SettingListInfo(
               title = "Reset Downloader Settings",
               description = "Reset the downloader settings to there original state",
               onClick = { toggleDownloader(ExpandableDownloaderSetting.ResetDownloader) },
               trailingContent = {
                    SettingsResetButton(
                         expanded = expandedSetting == ExpandableDownloaderSetting.ResetDownloader,
                         settingText = "Reset Downloader Settings",
                         onClick = {
                              downloaderSettingsViewModel.clearYtdlp()
                              scope.launch {
                                   snackbarHostState.showSnackbar(
                                        message = "Setting is back to default",
                                        duration = SnackbarDuration.Short
                                   )
                              }
                         } //     TODO. Add a way to undo the actions
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
               title = "Connection Type",
               description = "Use different connections for the downloader",
               image = R.drawable.network,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.Wifi) },
               trailingContent = {
                    ModularSegmentedButtons(
                         expanded = expandedSetting == ExpandableDownloaderSetting.Wifi,
                         optionsList = listOf(
                              Connectivity.Any to "Default",
                              Connectivity.Wifi to "Wifi",
                              Connectivity.Cellular to "Cellular"
                         ),
                         selectedOption = wifi,
                         onOptionSelected = { downloaderSettingsViewModel.setWifi(it) }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Change IPV settings",
               description = "Change the IPV connection type",
               image = R.drawable.ipv,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.Ipv) },
               trailingContent = {
                    ModularSegmentedButtons(
                         expanded = expandedSetting == ExpandableDownloaderSetting.Ipv,
                         optionsList = listOf(
                              IpvConnection.Disabled to "Default",
                              IpvConnection.Ipv4 to "IPV4",
                              IpvConnection.Ipv6 to "IPV6"
                         ),
                         selectedOption = ipv,
                         onOptionSelected = { downloaderSettingsViewModel.setIpvConnection(it) }
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
                    ModularSegmentedButtons(
                         expanded = expandedSetting == ExpandableDownloaderSetting.Aria2c,
                         enabled = aria2cEnabled,
                         optionsList = listOf(
                              Aria2cMode.Disabled to "Disabled",
                              Aria2cMode.EdgeCaseOnly to "Edge Case",
                              Aria2cMode.Always to "Enabled"
                         ),
                         selectedOption = aria2cMode,
                         onOptionSelected = { downloaderSettingsViewModel.setAria2cMode(it) }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Yt-dlp HLS Options",
               description = "Options instead of Aria2, check any option if Aria2 is having issues, especially with m3u8 since yt-dlp prefers it's own options over Aria2 recently, because of security issues",
               image = R.drawable.hls_on,
               onClick = { toggleDownloader(ExpandableDownloaderSetting.ExternalDownloader) },
               trailingContent = {
                    ModularSegmentedButtons(
                         expanded = expandedSetting == ExpandableDownloaderSetting.ExternalDownloader,
                         enabled = externalDownloadersEnabled,
                         optionsList = listOf(
                              ExternalDownloaders.Disabled to "Disabled",
                              ExternalDownloaders.FFmpegAsExternal to "FFmpeg",
                              ExternalDownloaders.YtdlpNativeDownloader to "hls-native"
                         ),
                         selectedOption = externalDownloaders,
                         onOptionSelected = { downloaderSettingsViewModel.setExternalDownloaders(it) }
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
          )
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