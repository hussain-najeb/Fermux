package org.foss.fermux.components.settingsComponents


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SettingsSwitch(
     checked: Boolean,
     onCheckedChange: (Boolean) -> Unit,
     modifier: Modifier = Modifier,
     enabled: Boolean = true,
) {
     Row(
          modifier = Modifier
               .height(IntrinsicSize.Min)
               .wrapContentSize()
     ) {
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
