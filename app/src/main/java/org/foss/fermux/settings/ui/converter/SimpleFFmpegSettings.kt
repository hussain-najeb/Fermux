package org.foss.fermux.settings.ui.converter

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import org.foss.fermux.components.downloaderComponents.ModularSegmentedButtons
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.components.settingsComponents.SettingsSwitch
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.rememberNotificationPermissionRequest


enum class ExpandableFFmpegSetting {
     AudioBitrate,
     ThreadLimit,
     Resolution,
     Crf,
     ResetFFmpeg
}


@Composable
fun SimpleFFmpegSetting(
     navController: NavHostController,
     ffmpegSettingsViewModel: FFmpegSettingsViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
) {
     val bellState by ffmpegSettingsViewModel.ffmpegBellState.collectAsStateWithLifecycle()
     val normalizeAudio by ffmpegSettingsViewModel.normalizeAudio.collectAsStateWithLifecycle()
     val monoDownmix by ffmpegSettingsViewModel.monoDownmix.collectAsStateWithLifecycle()
     val enableVideoCompression by ffmpegSettingsViewModel.enableVideoCompression.collectAsStateWithLifecycle()
     val audioBitrate by ffmpegSettingsViewModel.audioBitrate.collectAsStateWithLifecycle()
     val resolution by ffmpegSettingsViewModel.videoResolution.collectAsStateWithLifecycle()


     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()


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
               ffmpegSettingsViewModel.setFFmpegBellState(false)
               scope.launch {
                    snackbarHostState.showSnackbar(
                         message = "Permission denied",
                         duration = SnackbarDuration.Short
                    )
               }
          }
     )

     var expandedFFmpegSetting by remember {
          mutableStateOf<ExpandableFFmpegSetting?>(null)
     }

     fun toggleFFmpeg(setting: ExpandableFFmpegSetting) {
          expandedFFmpegSetting =
               if (expandedFFmpegSetting == setting) null else setting
     }



     val simpleFFmpegSetting = listOf(
          SettingListInfo(
               title = "Audio Bitrate",
               description = "Audio bitrate is the amount of data processed for each second of sound, higher is better",
               image = R.drawable.edit_audio,
               onClick = { toggleFFmpeg(ExpandableFFmpegSetting.AudioBitrate) },
               trailingContent = {
                    ModularSegmentedButtons(
                         expanded = expandedFFmpegSetting == ExpandableFFmpegSetting.AudioBitrate,
                         optionsList = listOf(
                              "64k" to "64k",
                              "128k" to "128k",
                              "192k" to "192k",
                              "256k" to "256k",
                              "320k" to "320k"
                         ),
                         selectedOption = audioBitrate,
                         onOptionSelected = { ffmpegSettingsViewModel.setAudioBitrate(it) }
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
               title = "Normalize Audio",
               description = "Audio normalization is uniformly adjusting a recording's overall volume so its peak or average loudness hits a specific target level",
               image = if (normalizeAudio) R.drawable.normalize_audio else R.drawable.audio_lines_x,
               content = {
                    SettingsSwitch(
                         checked = normalizeAudio,
                         onCheckedChange = {
                              ffmpegSettingsViewModel.setNormalizeAudio(it)
                         }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Mono Downmix",
               description = "Mono downmix blends multi-channel or stereo audio into one single channel",
               image = R.drawable.headphones,
               content = {
                    SettingsSwitch(
                         checked = monoDownmix,
                         onCheckedChange = { ffmpegSettingsViewModel.setMonoDownmix(it) }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Video Resolution",
               description = "Edit the video resolution for the selected media prior to using the converter so it outputs the selected resolution in this setting. Original is recommended",
               image = R.drawable.video_resolution,
               onClick = { toggleFFmpeg(ExpandableFFmpegSetting.Resolution) },
               trailingContent = {
                    ModularSegmentedButtons(
                         expanded = expandedFFmpegSetting == ExpandableFFmpegSetting.Resolution,
                         optionsList = listOf("" to "Normal",
                              "480" to "480p",
                              "720" to "720p",
                              "1080" to "1080p",
                              "1440" to "1440p"
                         ),
                         selectedOption = resolution,
                         onOptionSelected = { ffmpegSettingsViewModel.setVideoResolution(it) }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Video Compression",
               description = "This re-encodes the video instead of copying it as-is, trading speed for a smaller file size",
               image = R.drawable.video_compression,
               content = {
                    SettingsSwitch(
                         checked = enableVideoCompression,
                         onCheckedChange = {
                              ffmpegSettingsViewModel.setEnableVideoCompression(it)
                         }
                    )
               },
               position = TilePosition.BOTTOM
          ),
     )

     LargeTopBarScaffold(
          title = "Converter Settings",
          onBack = { navController.popBackStack() },
          snackbarHost = { AppSnackBar(snackbarHostState) }
     ) { paddingValues ->
          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
          ) {


               Text(
                    text = "General",
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp),
                    fontSize = 18.sp,
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               simpleFFmpegSetting.forEach { option ->
                    TileOptions(
                         title = option.title,
                         description = option.description,
                         shape = option.position.TileShaper(),
                         image = option.image,
                         icon = option.icon,
                         onClick = {
                              option.onClick?.invoke()
                              option.route?.let { navController.navigate(it) }
                         },
                         content = option.content,
                         trailingContent = option.trailingContent
                    )
               }

               Text(
                    text = "Advanced",
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 8.dp),
                    fontSize = 18.sp,
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               AdvancedFFmpegSettings(
                    navController = navController,
                    ffmpegSettingsViewModel = ffmpegSettingsViewModel
               )

          }
     }
}