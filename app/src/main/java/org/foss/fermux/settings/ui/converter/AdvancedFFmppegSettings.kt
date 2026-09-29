package org.foss.fermux.settings.ui.converter

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Tune
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
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.components.generalComponents.ModularSlider
import org.foss.fermux.components.settingsComponents.SettingsSwitch
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import kotlin.math.roundToInt

@Composable
fun AdvancedFFmpegSettings(
     navController: NavController,
     ffmpegSettingsViewModel: FFmpegSettingsViewModel = viewModel()
) {

     val logcat by ffmpegSettingsViewModel.ffmpegDebug.collectAsStateWithLifecycle()
     val crf by ffmpegSettingsViewModel.videoCrf.collectAsStateWithLifecycle()
     val enableVideoCompression by ffmpegSettingsViewModel.enableVideoCompression.collectAsStateWithLifecycle()
     val threadLimit by ffmpegSettingsViewModel.threadLimit.collectAsStateWithLifecycle()
     val useHardwareEncoder by ffmpegSettingsViewModel.useHardwareEncoder.collectAsStateWithLifecycle()
     val availableCores = Runtime.getRuntime().availableProcessors()
          LaunchedEffect(useHardwareEncoder) {
               if (useHardwareEncoder && threadLimit != 0) {
                    ffmpegSettingsViewModel.setThreadLimit(0)
               }
          }



     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()
     var expandedFFmpegSetting by remember {
          mutableStateOf<ExpandableFFmpegSetting?>(null)
     }
     fun toggleFFmpeg(setting: ExpandableFFmpegSetting) {
          expandedFFmpegSetting =
               if (expandedFFmpegSetting == setting) null else setting
     }

     val advanced = listOf(
          SettingListInfo(
               title = "Reset Converter Settings",
               description = "Reset the converter settings to there original state",
               onClick = { toggleFFmpeg(ExpandableFFmpegSetting.ResetFFmpeg) },
               trailingContent = {
                    SettingsResetButton(
                         expanded = expandedFFmpegSetting == ExpandableFFmpegSetting.ResetFFmpeg,
                         settingText = "Reset FFmpeg Settings",
                         onClick = {
                              ffmpegSettingsViewModel.setClearFFmpeg()
                              scope.launch {
                                   snackbarHostState.showSnackbar(
                                        message = "Setting is back to default",
                                        duration = SnackbarDuration.Short
                                   )
                              }
                         }
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
                    ModularSlider(
                         expanded = expandedFFmpegSetting == ExpandableFFmpegSetting.Crf,
                         sliderKey = crf,
                         trackSteps = 9,
                         trackRange = 18f..28f,
                         onOptionSelected = { ffmpegSettingsViewModel.setVideoCrf(it) }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = if (logcat) "Debug Logging On" else "Debug Logging Off",
               description = "Write diagnostic messages to Logcat in any builds",
               icon = Icons.Default.BugReport,
               content = {
                    SettingsSwitch(
                         checked = logcat,
                         onCheckedChange = { ffmpegSettingsViewModel.setFFmpegDebug(it) }
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
                    ModularSegmentedButtons(
                         expanded = expandedFFmpegSetting == ExpandableFFmpegSetting.ThreadLimit,
                         optionsList = listOf(
                              0 to "Default",
                              (availableCores * 0.25f).roundToInt().coerceAtLeast(1) to "25%",
                              (availableCores * 0.5f).roundToInt().coerceAtLeast(1) to "50%",
                              (availableCores * 0.75f).roundToInt().coerceAtLeast(1) to "75%",
                         ),
                         selectedOption = threadLimit,
                         enabled = !useHardwareEncoder,
                         onOptionSelected = { ffmpegSettingsViewModel.setThreadLimit(it) }
                    )
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Hardware Encoding",
               description = "Uses the hardware chip for encoding instead of CPU. It's much faster and saves battery, but files are slightly larger",
               image = R.drawable.hardware_encoding,
               content = {
                    SettingsSwitch(
                         enabled = enableVideoCompression,
                         checked = useHardwareEncoder,
                         onCheckedChange = { ffmpegSettingsViewModel.setUseHardwareEncoder(it) }
                    )
               },
               position = TilePosition.BOTTOM
          ),
     )

     advanced.forEach { option ->
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
     Spacer(modifier = Modifier.padding(top = 10.dp))

}