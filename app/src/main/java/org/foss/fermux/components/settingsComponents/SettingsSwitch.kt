package org.foss.fermux.components.settingsComponents


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SettingsSwitch(
     checked: Boolean,
     onCheckedChange: (Boolean) -> Unit,
     modifier: Modifier = Modifier,
     liner: Boolean = false,
     enabled: Boolean = true,
) {
     Row(
          modifier = Modifier
               .height(IntrinsicSize.Min)
               .wrapContentSize()
     ) {
          if (liner) {
               VerticalDivider(
                    modifier = Modifier
                         .fillMaxHeight()
                         .padding(6.dp),
                    thickness = 0.5.dp,
                    color = FermuxColors.fermuxHelperBorder,
               )
          }

          Switch(
               thumbContent = if (checked) {
                    {
                         Icon(
                              imageVector = Icons.Default.Check,
                              contentDescription = null,
                              tint = FermuxColors.downriver,
                              modifier = Modifier.size(SwitchDefaults.IconSize),
                         )
                    }
               } else {
                    null
               },
               checked = checked,
               onCheckedChange = onCheckedChange,
               modifier = modifier,
               enabled = enabled,
               colors = SwitchDefaults.colors(
                    checkedThumbColor = FermuxColors.white,
                    uncheckedThumbColor = FermuxColors.offWhiteTextColor,
                    checkedTrackColor = FermuxColors.fermuxTrackOn,
                    uncheckedTrackColor = FermuxColors.gray,
               ),
          )
     }
}
