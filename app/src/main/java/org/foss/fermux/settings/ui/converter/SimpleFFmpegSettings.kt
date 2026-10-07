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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.components.settingsComponents.SettingsSwitch
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.rememberNotificationPermissionRequest

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



     val simpleFFmpegSetting = listOf(
          SettingListInfo(
               title = "Audio Bitrate",
               description = "The amount of data processed for each second of sound. Higher is better",
               image = R.drawable.edit_audio,
               trailingContent = {
                    ModularSegmentedButtons(
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
               description = "Audio normalization is uniformly adjusting a recording's overall peak or average loudness in the audio",
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
               description = "Edit the video resolution for the selected media prior to using the converter so it outputs the selected resolution",
               image = R.drawable.video_resolution,
               trailingContent = {
                    ModularSegmentedButtons(
                         optionsList = listOf(
                              "" to "Default",
                              "720" to "HD",
                              "1080" to "FHD",
                              "1440" to "2K"
                         ),
                         selectedOption = resolution,
                         onOptionSelected = { ffmpegSettingsViewModel.setVideoResolution(it) }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Video Compression",
               description = "This re-encodes the video instead of copying it as-is",
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
                    .background(FermuxColors.background)
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
                         trailingContent = option.trailingContent,
                         dialogContent = option.dialogContent
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
                    ffmpegSettingsViewModel = ffmpegSettingsViewModel,
                    snackbarHostState = snackbarHostState
               )

          }
     }
}
