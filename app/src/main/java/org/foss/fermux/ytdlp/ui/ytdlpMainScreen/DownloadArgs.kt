package org.foss.fermux.ytdlp.ui.ytdlpMainScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.SettingsResetButton
import org.foss.fermux.components.downloaderComponents.AudioFormatSelector
import org.foss.fermux.components.downloaderComponents.RequestTimeSlider
import org.foss.fermux.components.downloaderComponents.ThumbnailSelector
import org.foss.fermux.components.downloaderComponents.VideoFormatSelector
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.MediumTopBarScaffold
import org.foss.fermux.components.settingsComponents.SettingsSwitch
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors

private enum class ExpandableOptionList {
     ThumbnailFormats,
     AudioFormats,
     VideoFormats,
     SleepRequest,
     ResetArgs,
} // TODO. Had a crazy idea, add a quick "anything" button. for downloads add teh quick download button which just copies your clipboard and downloads a video, add a setting entry that tells the user via a slider that edits the video res and audio qualtity
// TODO. add under the downloads tab card in the home menu a Loading indicator straight horizontal bar, should be easy, like a surface with the loading indicator inside it for.... looking cool
// TODO. add in a button for each tab except settings, as in, just quick access stuff and it looks better... UX!


@Composable
fun DownloaderArgs(navController: NavController) {
     val downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel()


     val playlist by downloaderSettingsViewModel.playlistState.collectAsStateWithLifecycle()
     val sleepRequest by downloaderSettingsViewModel.sleepRequest.collectAsStateWithLifecycle()
     val thumbnail by downloaderSettingsViewModel.embedThumbnail.collectAsStateWithLifecycle()
     val videoFormats by downloaderSettingsViewModel.videoFormats.collectAsStateWithLifecycle()
     val audioFormats by downloaderSettingsViewModel.audioFormats.collectAsStateWithLifecycle()
     val thumbnailFormat by downloaderSettingsViewModel.thumbnailFormat.collectAsStateWithLifecycle()
     val videoComp by downloaderSettingsViewModel.videoComp.collectAsStateWithLifecycle()

     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()


     var expandedSetting by remember { mutableStateOf<ExpandableOptionList?>(null) }


     fun toggleExpansion(setting: ExpandableOptionList) {
          expandedSetting = if (expandedSetting == setting) null else setting
     }

     val args = listOf(
          SettingListInfo(
               title = "Set Audio Format",
               description = "This option sets the format of the audio when downloading. current format is $audioFormats",
               image = R.drawable.audio_file,
               onClick = { toggleExpansion(setting = ExpandableOptionList.AudioFormats) },
               trailingContent = {
                    AudioFormatSelector(
                         expanded = expandedSetting == ExpandableOptionList.AudioFormats
                    )
               },
               position = TilePosition.TOP
          ),
          SettingListInfo(
               title = "Set Video Format",
               description = "This option sets the format of the video when downloading. current is $videoFormats",
               image = R.drawable.file_video,
               onClick = { toggleExpansion(setting = ExpandableOptionList.VideoFormats) },
               trailingContent = {
                    VideoFormatSelector(
                         expanded = expandedSetting == ExpandableOptionList.VideoFormats
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Video Compatibility",
               description = "Re-encodes the media to enforce video formats. It's more reliable but EXTREMELY slow and CPU intensive",
               image = R.drawable.re_encodes, // TODO. resue the AlretDialog you had in the project files and make it launch an alretdialog that says what this means, then in the alert dialog have 3 buttons, one to cancel it and one to enabled the boolean
               content = {
                    SettingsSwitch(
                         checked = videoComp,
                         onCheckedChange = { downloaderSettingsViewModel.setVideoComp(it) }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Set Thumbnail Format",
               description = "This option sets the format of the thumbnail when downloading. current format is $thumbnailFormat",
               image = R.drawable.file_image,
               onClick = { toggleExpansion(setting = ExpandableOptionList.ThumbnailFormats) },
               trailingContent = {
                    ThumbnailSelector(
                         expanded = expandedSetting == ExpandableOptionList.ThumbnailFormats
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


     MediumTopBarScaffold(
          title = "Arguments",
          onBack = { navController.popBackStack() },
          snackbarHost = { AppSnackBar(snackbarHostState) }
     ) { innerPadding ->
               Column(
                    modifier = Modifier
                         .padding(innerPadding)
                         .verticalScroll(rememberScrollState())
                         .fillMaxSize()
                         .background(FermuxColors.fermuxBackground),
               ) {
                    args.forEach { option ->
                         TileOptions(
                              title = option.title,
                              description = option.description,
                              image = option.image,
                              icon = option.icon,
                              onClick = { option.onClick?.invoke() },
                              content = option.content,
                              trailingContent = option.trailingContent ,
                              shape = option.position.TileShaper()
                         )
                    }
                    Spacer(modifier = Modifier.padding(top = 20.dp))
               }
          }
     }