package org.foss.fermux.settings.ui.converter

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.components.generalComponents.ModularSlider
import org.foss.fermux.components.settingsComponents.SettingsSwitch
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel
import org.foss.fermux.settings.logic.InfoListClass
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
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
          InfoListClass(
               title = "Reset Converter Settings",
               description = "Reset converter settings",
               image = R.drawable.restor,
               liner = true,
               content = {
                    FilterButton(
                         modifier = Modifier.padding(3.dp),
                         border = true,
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
          InfoListClass(
               title = "Video CRF",
               description = "Set a value for video CRF, default is recommended",
               image = R.drawable.tune,
               dialogAppearance = true,
               dialogTitle = "Set CRF Value",
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("CRF ") }
                    withStyle(SpanStyle(FermuxColors.white)) { append("is the quality target used when compressing videos. Lower is better.") }
               },
               dialogImage = R.drawable.tune,
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
          InfoListClass(
               title = if (logcat) "Debug Logging On" else "Debug Logging Off",
               description = "Write diagnostic messages to Logcat",
               icon = Icons.Default.BugReport,
               liner = true,
               content = {
                    SettingsSwitch(
                         checked = logcat,
                         onCheckedChange = { ffmpegSettingsViewModel.setFFmpegDebug(it) }
                    )
               },
               dialogAppearance = true,
               dialogTitle = "Debugging",
               dialogImage = R.drawable.log,
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.white)) { append("When enabled, this setting makes the logs from ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("WorkManager ") }
                    withStyle(SpanStyle(FermuxColors.white)) { append("appear in the logging screen, which is useful for deep debugging and app inspection.") }
               },
               position = TilePosition.MIDDLE
          ),
          InfoListClass(
               title = "CPU Thread Limit",
               description = "Limits how many CPU cores ffmpeg can use during conversion",
               image = if(threadLimit > 0)R.drawable.thread_limit_on else R.drawable.thread_limit_off,
               dialogAppearance = true,
               dialogTitle = "Set Thread Limit",
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.white)) { append("This setting has no effect when") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append(" Hardware Encoding") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" is on.") }
               },
               dialogImage = R.drawable.thread_limit_on,
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
          InfoListClass(
               title = "Hardware Acceleration",
               description = "Enable/Disable hardware acceleration",
               image = R.drawable.hardware_encoding,
               dialogAppearance = true,
               dialogTitle = "Hardware Acceleration",
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.white)) {
                         append("Uses the hardware chip for encoding instead of the CPU.\n\n")
                         append("This is slower in most cases, especially on")
                    }
                    withStyle(SpanStyle(FermuxColors.fermuxTextError)) { append(" old android devices.") }
               },
               dialogImage = R.drawable.hardware_encoding,
               liner = true,
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

     advanced.forEach { setting ->
          TileOptions(
               title = setting.title,
               description = setting.description,
               shape = setting.position.TileShaper(),
               image = setting.image,
               icon = setting.icon,
               onClick = {
                    setting.onClick?.invoke()
                    setting.route?.let { navController.navigate(it) }
               },
               content = setting.content,
               trailingContent = setting.trailingContent,
               dialogShow = setting.dialogAppearance,
               dialogTitle = setting.dialogTitle,
               dialogDescription = setting.dialogDescription,
               dialogImage = setting.dialogImage,
               dialogContent = setting.dialogContent,
               specialDescription = setting.specialDescription,
               liner = setting.liner
          )
     }
}
