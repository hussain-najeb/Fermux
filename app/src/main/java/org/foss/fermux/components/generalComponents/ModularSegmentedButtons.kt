package org.foss.fermux.components.generalComponents

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun <T> ModularSegmentedButtons(
     expanded: Boolean,
     enabled: Boolean = true,
     optionsList: List<Pair<T, String>>,
     selectedOption: T,
     onOptionSelected: (T) -> Unit
) {

     AnimatedVisibility(
          visible = expanded,
          enter = expandVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeIn(initialAlpha = 0.2f),
          exit = shrinkVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeOut(targetAlpha = 0.1f)
     ) {
          Surface(
               modifier = Modifier
                    .wrapContentSize()
                    .padding(1.dp),
               color = FermuxColors.warmPurple,
               shape = RoundedCornerShape(8.dp),
               border = BorderStroke(1.dp, color = FermuxColors.fermuxHelperBorder)
          ) {
                    SingleChoiceSegmentedButtonRow(
                         modifier = Modifier
                              .padding(8.dp)
                              .fillMaxWidth()
                    ) {
                         optionsList.forEachIndexed { index, (value, name) ->
                              SegmentedButton(
                                   selected = selectedOption == value,
                                   onClick = { onOptionSelected(value) },
                                   shape = SegmentedButtonDefaults.itemShape(
                                        index = index,
                                        count = optionsList.size,
                                        baseShape = RoundedCornerShape(8.dp)
                                   ),
                                   enabled = enabled,
                                   colors = SegmentedButtonDefaults.colors(
                                        activeContainerColor = FermuxColors.skyBlue,
                                        activeContentColor = FermuxColors.downriver,
                                        inactiveContainerColor = FermuxColors.inActiveContainer,
                                        inactiveContentColor = FermuxColors.white,
                                        activeBorderColor = FermuxColors.fermuxSecondaryBorder,
                                        inactiveBorderColor = FermuxColors.fermuxGenericBorder,
                                        disabledActiveContainerColor =
                                             FermuxColors.skyBlue.copy(alpha = 0.4f),
                                        disabledActiveContentColor =
                                             FermuxColors.downriver.copy(alpha = 0.4f),
                                        disabledActiveBorderColor =
                                             FermuxColors.fermuxSecondaryBorder.copy(alpha = 0.4f),
                                        disabledInactiveContainerColor =
                                             FermuxColors.inActiveContainer.copy(alpha = 0.4f),
                                        disabledInactiveContentColor =
                                             FermuxColors.white.copy(alpha = 0.4f),
                                        disabledInactiveBorderColor =
                                             FermuxColors.fermuxGenericBorder.copy(alpha = 0.4f)
                                   )
                              ) {
                                   Text(name)
                              }
                         }
                    }
               }

     }
}