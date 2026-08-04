package org.foss.fermux.fermuxUIComponents.settingsComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
     color: FermuxColor = FermuxColors,
     border: BorderStroke? = BorderStroke(1.dp, color.fermuxSecondaryBorder),
     onChecked: Boolean? = null,
     onCheckedChange: ((Boolean) -> Unit)? = null,
     content: @Composable (() -> Unit)? = null
) {
     ListItem(
          modifier = modifier
               .padding(9.dp)
               .border(border ?: BorderStroke(1.dp, color.fermuxGenericBorder), shape = RoundedCornerShape(8.dp)),
          leadingContent = {
               if (settingImage != null) {
                    Icon(
                         painter = settingImage,
                         contentDescription = null,
                         tint = FermuxColor().fermuxOffWhiteTextColor,
                         modifier = imageModifier
                    )
               } else {
                    if (settingIcon != null) {
                         Icon(
                              imageVector = settingIcon,
                              contentDescription = null,
                              tint = FermuxColors.fermuxOffWhiteTextColor,
                              modifier = iconModifier
                         )
                    }
               }
          },
          supportingContent = {
               Text(
                    text = settingDescription,
                    fontFamily = FontFamily.Default,
                    color = color.fermuxOffWhiteTextColor
               )
          },
          trailingContent = {
               Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
               ) {
                    if (onChecked != null && onCheckedChange != null) {
                         Switch(
                              colors = SwitchDefaults.colors(
                                   checkedThumbColor = color.fermuxThumbOn,
                                   uncheckedThumbColor = color.fermuxThumbOff,
                                   checkedTrackColor = color.fermuxTrackOn,
                                   uncheckedTrackColor = color.fermuxTrackOff,
                              ),
                              checked = onChecked,
                              onCheckedChange = onCheckedChange
                         )
                    }
                    content?.invoke()
               }
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
