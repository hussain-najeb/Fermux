package org.foss.fermux.fermuxUIComponents.downloaderComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode

private val aria2cModeOptions = listOf(
     Aria2cMode.Disabled to "Off",
     Aria2cMode.EdgeCaseOnly to "Edge Case",
     Aria2cMode.Always to "Always"
)

@Composable
fun Aria2cModeSelector(
     expanded: Boolean,
     downloaderSettingsViewModel: DownloaderSettingsViewModel
) {
     val aria2cMode by downloaderSettingsViewModel.aria2cMode.collectAsStateWithLifecycle()

     AnimatedVisibility(
          visible = expanded,
          enter = expandVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeIn(initialAlpha = 0.2f),
          exit = shrinkVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeOut(targetAlpha = 0.1f)
     ) {
          Surface(
               modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 8.dp, end = 8.dp),
               color = FermuxColors.fermuxComponents,
               shape = RoundedCornerShape(8.dp),
               border = BorderStroke(1.dp, color = FermuxColors.fermuxHelperBorder)
          ) {
               SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                         .padding(7.dp)
                         .fillMaxWidth()
               ) {
                    aria2cModeOptions.forEachIndexed { index, (mode, label) ->
                         SegmentedButton(
                              selected = aria2cMode == mode,
                              onClick = { downloaderSettingsViewModel.setAria2cMode(mode) },
                              shape = SegmentedButtonDefaults.itemShape(
                                   index = index,
                                   count = aria2cModeOptions.size,
                                   baseShape = RoundedCornerShape(8.dp)
                              ),
                              colors = SegmentedButtonDefaults.colors(
                                   activeContainerColor = FermuxColors.activeContainer,
                                   activeContentColor = FermuxColors.activeContent,
                                   inactiveContainerColor = FermuxColors.inActiveContainer,
                                   inactiveContentColor = FermuxColors.inActiveContent,
                                   activeBorderColor = FermuxColors.fermuxSecondaryBorder,
                                   inactiveBorderColor = FermuxColors.fermuxGenericBorder,
                                   disabledActiveContainerColor =
                                        FermuxColors.activeContainer.copy(alpha = 0.4f),
                                   disabledActiveContentColor =
                                        FermuxColors.activeContent.copy(alpha = 0.4f),
                                   disabledActiveBorderColor =
                                        FermuxColors.fermuxSecondaryBorder.copy(alpha = 0.4f),
                                   disabledInactiveContainerColor =
                                        FermuxColors.inActiveContainer.copy(alpha = 0.4f),
                                   disabledInactiveContentColor =
                                        FermuxColors.inActiveContent.copy(alpha = 0.4f),
                                   disabledInactiveBorderColor =
                                        FermuxColors.fermuxGenericBorder.copy(alpha = 0.4f)
                              )
                         ) {
                              Text(label)
                         }
                    }
               }
          }
     }
}