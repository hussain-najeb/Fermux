package org.foss.fermux.settings.ui.converter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.SmallActionButton
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
     ffmpegSettingsViewModel: FFmpegSettingsViewModel = viewModel(),
     snackbarHostState: SnackbarHostState
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

     val scope = rememberCoroutineScope()

     val advanced = listOf(
          SettingListInfo(
               title = "Reset Converter Settings",
               description = "Reset the converter settings to there original state",
               image = R.drawable.restor,
               content = {
                    SmallActionButton(
                         modifier = Modifier,
                         image = R.drawable.restor,
                         onClick = {
                              scope.launch {
                                   val oldSettings = ffmpegSettingsViewModel.resetFFmpegSettings()
                                   val result = snackbarHostState.showSnackbar(
                                        message = "Settings Reset",
                                        actionLabel = "Undo",
                                        duration = SnackbarDuration.Long
                                   )

                                   if (result == SnackbarResult.ActionPerformed) {
                                        ffmpegSettingsViewModel.restoreFFmpegSettings(oldSettings)
                                   }
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
               dialogContent = {
                    ModularSlider(
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
               description = "Limits how many CPU cores ffmpeg can use during conversion. Has no effect when hardware encoding is on",
               image = if(threadLimit >0)R.drawable.thread_limit_on else R.drawable.thread_limit_off,
               dialogContent = {
                    ModularSegmentedButtons(
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
               description = "Uses the hardware chip for encoding instead of CPU",
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
}
