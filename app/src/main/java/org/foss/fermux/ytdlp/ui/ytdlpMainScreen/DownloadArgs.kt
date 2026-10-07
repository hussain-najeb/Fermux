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
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.SmallActionButton
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.MediumTopBarScaffold
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.components.generalComponents.ModularSlider
import org.foss.fermux.components.settingsComponents.SettingsSwitch
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.AudioFormat
import org.foss.fermux.ytdlp.logic.downloader.ThumbnailFormat
import org.foss.fermux.ytdlp.logic.downloader.VideoFormat


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
     val retries by downloaderSettingsViewModel.retries.collectAsStateWithLifecycle()
     val fragRetries by downloaderSettingsViewModel.fragRetries.collectAsStateWithLifecycle()

     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()

     val args = listOf(
          SettingListInfo(
               title = "Reset Arguments",
               description = "Reset the arguments to their original state",
               image = R.drawable.restor,
               content = {
                    SmallActionButton(
                         modifier = Modifier,
                         image = R.drawable.restor,
                         onClick = {
                              scope.launch {
                                   val oldArg = downloaderSettingsViewModel.resetArguments()
                                   val result = snackbarHostState.showSnackbar(
                                        message = "Settings Reset",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Short
                                   )
                                   if (result == SnackbarResult.ActionPerformed) downloaderSettingsViewModel.restoreArguments(oldArg)
                              }
                         }
                    )
               },
               position = TilePosition.TOP
          ),
          SettingListInfo(
               title = "Set Audio Format",
               description = "This option sets the format of the audio when downloading. current format is $audioFormats",
               image = R.drawable.audio_file,
               dialogContent = {
                    ModularSegmentedButtons(
                         optionsList = listOf(
                              AudioFormat.Mp3Format to "mp3",
                              AudioFormat.OpusFormat to "opus",
                              AudioFormat.FlacFormat to "flac",
                              AudioFormat.M4aFormat to "m4a"
                         ),
                         selectedOption = audioFormats,
                         onOptionSelected = { downloaderSettingsViewModel.setAudioFormat(it) }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Set Video Format",
               description = "This option sets the format of the video when downloading. current is $videoFormats",
               image = R.drawable.file_video,
               dialogContent = {
                    ModularSegmentedButtons(
                         optionsList = listOf(
                              VideoFormat.Mp4Format to "mp4",
                              VideoFormat.AviFormat to "avi",
                              VideoFormat.Mkv to "mkv",
                              VideoFormat.WebMFormat to "webm"
                         ),
                         selectedOption = videoFormats,
                         onOptionSelected = { downloaderSettingsViewModel.setVideoFormat(it) }
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
               dialogContent = {
                    ModularSegmentedButtons(
                         optionsList = listOf(
                              ThumbnailFormat.Off to "off",
                              ThumbnailFormat.Jpeg to "jpeg",
                              ThumbnailFormat.Png to "png",
                              ThumbnailFormat.WebP to "webp",
                         ),
                         selectedOption = thumbnailFormat,
                         onOptionSelected = { downloaderSettingsViewModel.setThumbnailFormat(it) }
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
               title = "Set Retries",
               description = "Set the amount of retries that ytdlp does when downloading",
               image = R.drawable.retry,
               dialogContent = {
                    ModularSlider(
                         sliderKey = retries,
                         trackSteps = 3,
                         trackRange = 10f..50f,
                         onOptionSelected = { downloaderSettingsViewModel.setRetries(it) }
                    )
               }
          ),
          SettingListInfo(
               title = "Set Fragment Retries",
               description = "Set the amount of retries when downloading a fragment when using hls or aria2",
               image = FragImage(
                    fragments = fragRetries,
                    small = R.drawable.fragment_small,
                    smallMid = R.drawable.fragment_midsmall,
                    mid = R.drawable.fragment_mid,
                    midLarge = R.drawable.fragment_midlarge,
                    large = R.drawable.fragment_large
               ),
               dialogContent = {
                    ModularSlider(
                         sliderKey = fragRetries,
                         trackSteps = 3,
                         trackRange = 10f..50f,
                         onOptionSelected = { downloaderSettingsViewModel.setFragRetries(it) }
                    )
               }
          ),
          SettingListInfo(
               title = "Sleep Duration",
               description = "Sleep request is a flag for delayed download between each request, each number is a second",
               icon = if (sleepRequest > 0) Icons.Filled.Flag else Icons.Outlined.Flag,
               dialogContent = {
                    ModularSlider(
                         sliderKey = sleepRequest,
                         trackSteps = 4,
                         trackRange = 0f..5f,
                         onOptionSelected = { downloaderSettingsViewModel.setSleepRequest(it) }
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
                         .background(FermuxColors.background),
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
                              shape = option.position.TileShaper(),
                              dialogShow = option.dialogAppearance
                         ) // TODO. Add in the rest of the dialog work, with the rest of these arguments added to this one and the rest of the seetings and put everything in a dialog!
                    }
                    Spacer(modifier = Modifier.padding(top = 10.dp))
               }
          }
     }

@Composable
fun FragImage(
     fragments: Int,
     small: Int,
     smallMid: Int,
     mid: Int,
     midLarge: Int,
     large: Int
): Int = when (fragments) {
     10 -> small
     20 -> smallMid
     30 -> mid
     40 -> midLarge
     50 -> large
     else -> small
}