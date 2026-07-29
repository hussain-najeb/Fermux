package org.foss.fermux.fermuxUIComponents.settingsComponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Switch
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
     modifier: Modifier,
     settingIcon: ImageVector,
     settingTitle: String,
     settingDescription: String,
     settingImage: Painter? = null,
     color: FermuxColor = FermuxColors,
     onChecked: Boolean,
     onCheckedChange: (Boolean) -> Unit
) {
     ListItem(
          modifier = modifier
               .padding(9.dp)
               .border(1.dp, color.fermuxSecondaryBorder, shape = RoundedCornerShape(8.dp)),
          leadingContent = {
               if (settingImage != null) {
                    Image(
                         painter = settingImage,
                         contentDescription = null
                    )
               } else {
                    Icon(
                         imageVector = settingIcon,
                         contentDescription = null
                    )
               }
          },
          supportingContent = {
               Text(
                    text = settingDescription,
                    fontFamily = FontFamily.Default,
                    color = color.fermuxSettingsTextColor
               )
          },
          trailingContent = {
               Switch(
                    checked = onChecked,
                    onCheckedChange = onCheckedChange
               )
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