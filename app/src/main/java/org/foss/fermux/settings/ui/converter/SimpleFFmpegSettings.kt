package org.foss.fermux.settings.ui.converter

import android.annotation.SuppressLint
import android.widget.ExpandableListAdapter
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.SettingsResetButton
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.AudioBitrateSlider
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.CrfSlider
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.ResolutionSelect
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.ThreadLimitSelect
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.fermuxUIComponents.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors


private enum class ExpandableFFmpegSetting {
     AudioBitrate,
     ThreadLimit,
     Resolution,
     Crf,
     ResetFFmpeg
}


@Composable
fun SimpleFFmpegSetting(
     navController: NavHostController,
     @SuppressLint("ContextCastToActivity") ffmpegSettingsViewModel: FFmpegSettingsViewModel = viewModel(
          viewModelStoreOwner = LocalContext.current as ComponentActivity
     )
) {
     val threadLimit by ffmpegSettingsViewModel.threadLimit.collectAsStateWithLifecycle()
     val normalizeAudio by ffmpegSettingsViewModel.normalizeAudio.collectAsStateWithLifecycle()
     val monoDownmix by ffmpegSettingsViewModel.monoDownmix.collectAsStateWithLifecycle()
     val enableVideoCompression by ffmpegSettingsViewModel.enableVideoCompression.collectAsStateWithLifecycle()
     val useHardwareEncoder by ffmpegSettingsViewModel.useHardwareEncoder.collectAsStateWithLifecycle()


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
                    AudioBitrateSlider(
                         expanded = expandedFFmpegSetting == ExpandableFFmpegSetting.AudioBitrate
                    )
               },
               position = TilePosition.TOP
          ),
          SettingListInfo(
               title = "Normalize Audio",
               description = "Audio normalization is uniformly adjusting a recording's overall volume so its peak or average loudness hits a specific target level",
               image = R.drawable.normalize_audio,
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
                    ResolutionSelect(
                         expanded = expandedFFmpegSetting == ExpandableFFmpegSetting.Resolution
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Video Compression",
               description = "Video compression re-encodes the video instead of copying it as-is, trading conversion speed for a smaller file size",
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

     val advanced = listOf(
          SettingListInfo(
               title = "Reset Converter Settings",
               description = "Reset the converter settings to there original state",
               onClick = { toggleFFmpeg(ExpandableFFmpegSetting.ResetFFmpeg) },
               trailingContent = {
                    SettingsResetButton(
                         expanded = expandedFFmpegSetting == ExpandableFFmpegSetting.ResetFFmpeg,
                         onClick = { ffmpegSettingsViewModel.setClearFFmpeg() } // TODO. Add toast here so the user knows its been done
                    )
               },
               position = TilePosition.TOP
          ),
          SettingListInfo(
               title = "Video CRF",
               description = "CRF is the quality target used when compressing videos. Lower is better",
               icon = Icons.Default.Tune,
               onClick = { toggleFFmpeg(ExpandableFFmpegSetting.Crf) },
               trailingContent = {
                    CrfSlider(
                         expanded = expandedFFmpegSetting == ExpandableFFmpegSetting.Crf
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "CPU Thread Limit",
               description = "Limits how many CPU cores ffmpeg can use during conversion, trading speed for less heat and battery drain. Has no effect when hardware encoding is on",
               image = if(threadLimit >0)R.drawable.thread_limit_on else R.drawable.thread_limit_off,
               onClick = { toggleFFmpeg(ExpandableFFmpegSetting.ThreadLimit) },
               trailingContent = {
                    ThreadLimitSelect(
                         expanded = expandedFFmpegSetting == ExpandableFFmpegSetting.ThreadLimit
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Hardware Endcoding",
               description = "Uses the hardware chip for ffmpeg encoding instead of CPU. It's much faster and saves battery, but files are slightly larger",
               image = R.drawable.hardware_encoding,
               content = {
                    SettingsSwitch(
                         checked = useHardwareEncoder,
                         onCheckedChange = {
                              ffmpegSettingsViewModel.setUseHardwareEncoder(it)
                         }
                    )
               },
               position = TilePosition.BOTTOM
          ),
     )

     LargeTopBarScaffold(
          title = "Converter Settings",
          onBack = { navController.popBackStack() }
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
                    modifier = Modifier.padding(
                         start = 16.dp,
                         top = 20.dp,
                         bottom = 8.dp
                    ),
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               simpleFFmpegSetting.forEach { option ->
                    TileOptions(
                         title = option.title,
                         description = option.description,
                         shape = option.position.toShape(),
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
                    modifier = Modifier.padding(
                         start = 16.dp,
                         top = 20.dp,
                         bottom = 8.dp
                    ),
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               advanced.forEach { option ->
                    TileOptions(
                         title = option.title,
                         description = option.description,
                         shape = option.position.toShape(),
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
          }
     }
}