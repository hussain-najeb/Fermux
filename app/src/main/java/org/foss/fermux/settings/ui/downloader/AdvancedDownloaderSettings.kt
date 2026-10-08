package org.foss.fermux.settings.ui.downloader

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.FilterButton
import org.foss.fermux.components.downloaderComponents.SponsorBlockChoices
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.components.settingsComponents.SettingsSwitch
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.clearCache
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode
import org.foss.fermux.ytdlp.logic.downloader.Connectivity
import org.foss.fermux.ytdlp.logic.downloader.ExternalDownloaders
import org.foss.fermux.ytdlp.logic.downloader.IpvConnection

@Composable
fun AdvancedDownloaderSettings(
     downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel(),
     navController: NavController,
     snackbarHostState: SnackbarHostState
) {
     val context = LocalContext.current

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
     val scope = rememberCoroutineScope()

     val advancedSettings = listOf(
          SettingListInfo(
               title = "Reset Downloader Settings",
               description = "Reset the downloader settings to there original state",
               image = R.drawable.restor,
               liner = true,
               content = {
                    FilterButton(
                         modifier = Modifier.padding(3.dp),
                         border = true,
                         image = R.drawable.restor,
                         onClick = {
                              scope.launch {
                                   val oldSettings = downloaderSettingsViewModel.resetDownloaderSettings()
                                   val result = snackbarHostState.showSnackbar(
                                        message = "Settings Reset",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Long
                                   )
                                   if (result == SnackbarResult.ActionPerformed) {
                                        downloaderSettingsViewModel.restoreDownloaderSettings(oldSettings)
                                   }
                              }
                         }
                    )
               },
               position = TilePosition.TOP
          ),
          SettingListInfo(
               title = if (logcat) "Debug Logging On" else "Debug Logging Off",
               description = "Write diagnostic messages to Logcat",
               icon = Icons.Default.BugReport,
               liner = true,
               dialogAppearance = true,
               dialogTitle = "Debugging",
               dialogImage = R.drawable.log,
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.white)) { append("When enabled, this setting makes the logs from ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("WorkManager ") }
                    withStyle(SpanStyle(FermuxColors.white)) { append("appear in the logging screen, which is useful for deep debugging and app inspection") }
               },
               content = {
                    SettingsSwitch(
                         modifier = Modifier.padding(2.dp),
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
               dialogAppearance = true,
               dialogTitle = "Connection Types",
               dialogImage = R.drawable.network,
               dialogDescription = "You can change each connection type to the one you prefer or is available to you",
               dialogContent = {
                    ModularSegmentedButtons(
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
               dialogAppearance = true,
               dialogTitle = "IPV Types",
               dialogImage = R.drawable.ipv,
               dialogDescription = "You can change each IPV connection to the one you prefer or is available to you",
               dialogContent = {
                    ModularSegmentedButtons(
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
               description = "SponsorBlock API integration for yt-dlp",
               image = R.drawable.sponsorblock,
               liner = true,
               dialogAppearance = true,
               dialogTitle = "SponsorBlock",
               dialogImage = R.drawable.sponsorblock,
               dialogDescription = "Set any SponsorBlock flag to cut out from the media you will download.",
               content = {
                    SettingsSwitch(
                         modifier = Modifier.padding(2.dp),
                         checked = sponsorBlock,
                         onCheckedChange = { downloaderSettingsViewModel.setSponsorBlock(it) }
                    )
               },
               dialogContent = {
                    SponsorBlockChoices(
                         downloaderSettingsViewModel = downloaderSettingsViewModel
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Aria2c",
               description = "Another option instead of the default fragment downloader",
               image = R.drawable.layers,
               dialogAppearance = true,
               dialogTitle = "Aria2c settings",
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.white)) { append("You") }
                    withStyle(SpanStyle(FermuxColors.fermuxLightErrorTextColor)) { append(" MUST") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" disable your chosen ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("HLS ") }
                    withStyle(SpanStyle(FermuxColors.white)) { append("option, so you can use these options.") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" Use") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append(" 'Edge Case'") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" when using ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("Aria2c ") }
                    withStyle(SpanStyle(FermuxColors.white)) { append("and downloading media at the highest setting!") }
               },
               dialogContent = {
                    ModularSegmentedButtons(
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
               description = "HLS options for fragment download",
               image = R.drawable.aria2_hls,
               dialogAppearance = true,
               dialogTitle = "HLS options",
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("HLS ") }
                    withStyle(SpanStyle(FermuxColors.white)) { append("options are essential for fragment downloading, the native ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("HLS ") }
                    withStyle(SpanStyle(FermuxColors.white)) { append("that comes with ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("yt-dlp") }
                    withStyle(SpanStyle(FermuxColors.white)) { append("is excellent, but there are options such as ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("FFmpeg") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" which comes with ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append(" TLS/HTTP ") }
                    withStyle(SpanStyle(FermuxColors.white)) { append("support, or ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("Aria2c ") }
                    withStyle(SpanStyle(FermuxColors.white)) { append("that comes as a separate setting") }
               },
               dialogImage = R.drawable.aria2_hls,
               dialogContent = {
                    ModularSegmentedButtons(
                         enabled = externalDownloadersEnabled,
                         optionsList = listOf(
                              ExternalDownloaders.Disabled to "Disabled",
                              ExternalDownloaders.FFmpegAsExternal to "FFmpeg",
                              ExternalDownloaders.YtdlpNativeDownloader to "HLS Native"
                         ),
                         selectedOption = externalDownloaders,
                         onOptionSelected = { downloaderSettingsViewModel.setExternalDownloaders(it) }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Impersonation",
               description = "Enabling impersonation makes yt-dlp requests look like a real browser",
               image = if (fingerprint) R.drawable.impersonation_on else R.drawable.impersonation_off,
               liner = true,
               content = {
                    SettingsSwitch(
                         modifier = Modifier.padding(2.dp),
                         checked = fingerprint,
                         onCheckedChange = { downloaderSettingsViewModel.setFingerprint(it) }
                    )
               },
               dialogAppearance = true,
               dialogTitle = "Impersonation?",
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.white)) { append("This setting uses ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("curl_cffi") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" and ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("curl-impersonate") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" to make") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append(" yt-dlp") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" requests seem like a normal browser and not a bot request which is increasingly important in today's internet, This setting supports") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append(" ALL Android ABI's") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(".") }
               },
               dialogImage = R.drawable.impersonation_on,
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Clearing Cache",
               description = "Clearing cache for the app",
               image = R.drawable.eraser,
               liner = true,
               content = {
                    FilterButton(
                         modifier = Modifier.padding(3.dp),
                         image = R.drawable.archive,
                         border = true,
                         onClick = { context.clearCache()
                              scope.launch {
                                   snackbarHostState.showSnackbar(
                                        message = "Cache Cleared",
                                        duration = SnackbarDuration.Short
                                   )
                              }
                         }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Quick JS Engine",
               description = "JavaScript engine for yt-dlp",
               image = if (quickJS) R.drawable.flash_on else R.drawable.flash_off,
               liner = true,
               dialogAppearance = true,
               dialogTitle = "QuickJS NG?",
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.white)) { append("This setting uses ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("QuickJsNG") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(", NG here mean Next Gen, which is an improved version of the old") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" QuickJs") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" .Duo to the nature of Youtube, this JS runtime is recommended by ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("yt-dlp") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" to solve JS challenges, this should make Youtube downloads more reliable.") }
               },
               dialogImage = R.drawable.impersonation_on,
               content = {
                    SettingsSwitch(
                         modifier = Modifier.padding(2.dp),
                         checked = quickJS,
                         onCheckedChange = { downloaderSettingsViewModel.setQuickJS(it) }
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
               liner = setting.liner,
               trailingContent = setting.trailingContent,
               onClick = {
                    setting.onClick?.invoke()
                    setting.route?.let { navController.navigate(it) }
               },
               dialogShow = setting.dialogAppearance,
               dialogTitle = setting.dialogTitle,
               dialogDescription = setting.dialogDescription,
               specialDescription = setting.specialDescription,
               dialogImage = setting.dialogImage,
               dialogContent = setting.dialogContent
          )
     }
}
