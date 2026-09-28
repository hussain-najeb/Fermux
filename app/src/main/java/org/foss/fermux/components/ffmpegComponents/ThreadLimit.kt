package org.foss.fermux.components.ffmpegComponents

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import kotlin.math.roundToInt

@Composable
fun ThreadLimitSelect(
     expanded: Boolean,
     @SuppressLint("ContextCastToActivity") settingsViewModel: FFmpegSettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {
     val threadLimit by settingsViewModel.threadLimit.collectAsStateWithLifecycle()
     val useHardwareEncoder by settingsViewModel.useHardwareEncoder.collectAsStateWithLifecycle()

     val availableCores = Runtime.getRuntime().availableProcessors()
     val options = listOf(
          0 to "Default",
          (availableCores * 0.25f).roundToInt().coerceAtLeast(1) to "25%",
          (availableCores * 0.5f).roundToInt().coerceAtLeast(1) to "50%",
          (availableCores * 0.75f).roundToInt().coerceAtLeast(1) to "75%",
     )

     LaunchedEffect(useHardwareEncoder) {
          if (useHardwareEncoder && threadLimit != 0) {
               settingsViewModel.setThreadLimit(0)
          }
     }

     AnimatedVisibility(
          visible = expanded,
          enter = expandVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeIn(initialAlpha = 0.2f),
          exit = shrinkVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeOut(targetAlpha = 0.1f)
     ) {
          Surface(
               modifier = Modifier.wrapContentSize().padding(start = 8.dp, end = 8.dp),
               color = FermuxColors.fermuxComponents,
               shape = RoundedCornerShape(8.dp),
               border = BorderStroke(1.dp, color = FermuxColors.fermuxHelperBorder)
          ) {
               SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                         .padding(7.dp)
                         .fillMaxWidth()
               ) {
                    options.forEachIndexed { position, (selectedChoice, label) ->
                         SegmentedButton(
                              selected = threadLimit == selectedChoice,
                              enabled = !useHardwareEncoder,
                              onClick = { settingsViewModel.setThreadLimit(selectedChoice) },
                              icon = {},
                              shape = SegmentedButtonDefaults.itemShape(
                                   index = position,
                                   count = options.size,
                                   baseShape = RoundedCornerShape(8.dp)
                              ),
                              colors = SegmentedButtonDefaults.colors(
                                   activeContainerColor = FermuxColors.activeContainer,
                                   activeContentColor = FermuxColors.activeContent,
                                   inactiveContainerColor = FermuxColors.inActiveContainer,
                                   inactiveContentColor = FermuxColors.inActiveContent,
                                   activeBorderColor = FermuxColors.fermuxSecondaryBorder,
                                   inactiveBorderColor = FermuxColors.fermuxGenericBorder
                              ),
                         ) {
                              Text(label)
                         }
                    }
               }
          }
     }
}