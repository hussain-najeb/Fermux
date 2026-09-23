package org.foss.fermux.ytdlp.ui.ytdlpMainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Speaker
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.SettingsResetButton
import org.foss.fermux.fermuxUIComponents.downloaderComponents.RequestTimeSlider
import org.foss.fermux.fermuxUIComponents.downloaderComponents.ThumbnailSelector
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxSnackBar
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.fermuxUIComponents.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.AudioFormat

private enum class ExpandableOptionList {
     Formats,
     SleepRequest,
     ResetArgs,
}


@Composable
fun DownloaderArgs(navController: NavController) {
     val downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel()


     val playlist by downloaderSettingsViewModel.playlistState.collectAsStateWithLifecycle()
     val sleepRequest by downloaderSettingsViewModel.sleepRequest.collectAsStateWithLifecycle()
     val thumbnail by downloaderSettingsViewModel.embedThumbnail.collectAsStateWithLifecycle()


     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()

     var audioFormats by remember { mutableStateOf<AudioFormat?>(AudioFormat.Mp3Format) }
     var expandedSetting by remember { mutableStateOf<ExpandableOptionList?>(null) }


     fun toggleExpansion(setting: ExpandableOptionList) {
          expandedSetting = if (expandedSetting == setting) null else setting
     }

     val args = listOf(
          SettingListInfo(
               title = "Set Audio Format",
               description = "This option sets the format of the audio when downloading. current format is $audioFormats",
               icon = Icons.Default.Speaker,
               onClick = { toggleExpansion(setting = ExpandableOptionList.Formats) },
               trailingContent = {
                    ThumbnailSelector(
                         expanded = expandedSetting == ExpandableOptionList.Formats
                    )
               },
               position = TilePosition.TOP
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
               title = "Sleep Request Yt-dlp Flag",
               description = "Sleep request is a flag for delayed download between each request, each number represents a second. 0 means the flag is off",
               icon = if (sleepRequest > 0) Icons.Filled.Flag else Icons.Outlined.Flag,
               onClick = { toggleExpansion(ExpandableOptionList.SleepRequest) },
               trailingContent = {
                    RequestTimeSlider(
                         expanded = expandedSetting == ExpandableOptionList.SleepRequest
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Reset Arguments",
               description = "Reset the arguments to their original state",
               icon = Icons.Default.SettingsBackupRestore,
               onClick = { toggleExpansion(ExpandableOptionList.ResetArgs) },
               trailingContent = {
                    SettingsResetButton(
                         expanded = expandedSetting == ExpandableOptionList.ResetArgs,
                         settingText = "Reset Downloader Settings",
                         onClick = {
                              downloaderSettingsViewModel.clearArgs()
                              scope.launch {
                                   snackbarHostState.showSnackbar(
                                        message = "Setting is back to default",
                                        duration = SnackbarDuration.Short
                                   )
                              }
                         } //     TODO. Add a way to undo the actions
                    )
               },
               position = TilePosition.BOTTOM
          )
     )


     LargeTopBarScaffold(
          title = "Arguments",
          onBack = { navController.popBackStack() },
          snackbarHost = { FermuxSnackBar(snackbarHostState) }
     ) { innerPadding ->
          Box(
               modifier = Modifier.fillMaxSize().padding(innerPadding).background(FermuxColors.fermuxBackground),
          ) {
               Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize().imePadding()
                         .background(FermuxColors.fermuxBackground)
               ) {
                    args.forEach { option ->
                         TileOptions(
                              title = option.title,
                              description = option.description,
                              image = option.image,
                              onClick = { option.onClick?.invoke() },
                              content = option.content,
                              trailingContent = option.trailingContent ,
                              shape = option.position.TileShaper()
                         )
                    }
               }
          }
     }
}