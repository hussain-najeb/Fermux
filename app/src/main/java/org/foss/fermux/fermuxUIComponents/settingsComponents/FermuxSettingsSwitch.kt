package org.foss.fermux.fermuxUIComponents.settingsComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun FermuxSettingsSwitch(
     modifier: Modifier= Modifier,
     imageModifier: Modifier = Modifier,
     iconModifier: Modifier = Modifier,
     settingIcon: ImageVector? = null,
     settingTitle: String,
     settingDescription: String,
     settingImage: Painter? = null,
     color: FermuxColor = FermuxColor(),
     onChecked: Boolean? = null,
     onCheckedChange: ((Boolean) -> Unit)? = null,
     content: @Composable (() -> Unit)? = null
) {
     ListItem(
          modifier = modifier
               .padding(9.dp)
               .border(1.dp, color.fermuxSecondaryBorder, shape = RoundedCornerShape(8.dp)),
          leadingContent = {
               if (settingImage != null) {
                    Icon(
                         painter = settingImage,
                         contentDescription = null,
                         tint = FermuxColor().fermuxSettingsTextColor,
                         modifier = imageModifier
                    )
               } else {
                    if (settingIcon != null) {
                         Icon(
                              imageVector = settingIcon,
                              contentDescription = null,
                              tint = FermuxColors.fermuxSettingsTextColor,
                              modifier = iconModifier
                         )
                    }
               }
          },
          supportingContent = {
               Text(
                    text = settingDescription,
                    fontFamily = FontFamily.Default,
                    color = color.fermuxSettingsTextColor
               )
          },
          trailingContent = { if (onChecked != null && onCheckedChange != null)
               Switch(
                    colors = SwitchDefaults.colors(
                         checkedThumbColor = color.fermuxThumbOn,
                         uncheckedThumbColor = color.fermuxThumbOff,
                         checkedTrackColor = color.fermuxTrackOn,
                         uncheckedTrackColor = color.fermuxTrackOff,
                    ),
                    checked = onChecked,
                    onCheckedChange = onCheckedChange) else content?.invoke()
          },
          colors = ListItemDefaults.colors(
               containerColor = color.fermuxSaturatedComponents
          ),
     ) {
          Text(
               text = settingTitle,
               fontFamily = FontFamily.Default,
               fontSize = 17.sp,
               color = color.fermuxInActiveTextColor
          )
     }
}
