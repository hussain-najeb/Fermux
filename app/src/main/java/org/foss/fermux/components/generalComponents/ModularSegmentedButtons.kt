package org.foss.fermux.components.generalComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun <T> ModularSegmentedButtons(
     enabled: Boolean = true,
     optionsList: List<Pair<T, String>>,
     selectedOption: T,
     onOptionSelected: (T) -> Unit
) {
     SingleChoiceSegmentedButtonRow(
          modifier = Modifier
               .fillMaxWidth()
     ) {
          optionsList.forEachIndexed { index, (value, name) ->
               SegmentedButton(
                    selected = selectedOption == value,
                    onClick = { onOptionSelected(value) },
                    shape = SegmentedButtonDefaults.itemShape(
                         index = index,
                         count = optionsList.size,
                         baseShape = RoundedCornerShape(6.dp)
                    ),
                    icon = {},
                    enabled = enabled,
                    colors = SegmentedButtonDefaults.colors(
                         activeContainerColor = FermuxColors.skyBlue,
                         activeContentColor = FermuxColors.downriver,
                         inactiveContainerColor = FermuxColors.darkPurple,
                         inactiveContentColor = FermuxColors.white,
                         activeBorderColor = FermuxColors.fermuxSecondaryBorder,
                         inactiveBorderColor = FermuxColors.fermuxHelperBorder,
                         disabledActiveContainerColor = FermuxColors.skyBlue.copy(alpha = 0.4f),
                         disabledActiveContentColor = FermuxColors.downriver.copy(alpha = 0.4f),
                         disabledActiveBorderColor = FermuxColors.fermuxSecondaryBorder.copy(alpha = 0.4f),
                         disabledInactiveContainerColor = FermuxColors.inActiveContainer.copy(alpha = 0.4f),
                         disabledInactiveContentColor = FermuxColors.white.copy(alpha = 0.4f),
                         disabledInactiveBorderColor = FermuxColors.fermuxGenericBorder.copy(alpha = 0.4f)
                    )
               ) {
                    Text(name)
               }
          }
     }
}