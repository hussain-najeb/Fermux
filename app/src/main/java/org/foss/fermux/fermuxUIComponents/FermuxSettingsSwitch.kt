package org.foss.fermux.fermuxUIComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
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

     FermuxCard(
          modifier = Modifier.fillMaxWidth().height(60.dp).padding(5.dp),
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.dp, color.fermuxSecondaryBorder),
     ) {
          Column {
               Row(
                    modifier = modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
               ) {

                    if (settingImage != null) {
                         Image(
                              painter = settingImage,
                              contentDescription = "Setting image",
                         )
                    }
                    Icon(
                         imageVector = settingIcon,
                         modifier = Modifier.padding(8.dp),
                         contentDescription = "Setting icon",
                    )

                    Text(
                         text = settingTitle,
                         fontFamily = FontFamily.Default,
                         fontSize = 17.sp,
                         color = color.fermuxInActiveTextColor,
                         fontStyle = FontStyle.Normal,
                    )

                    Switch(
                         checked = onChecked,
                         onCheckedChange = onCheckedChange,
                    )

               }
               Text(
                    text = settingDescription,
                    fontFamily = FontFamily.Default,
                    fontSize = 13.sp,
                    color = color.fermuxActiveTextColor,
                    fontStyle = FontStyle.Normal,
                    modifier = Modifier.padding(start = 8.dp)
               )
          }
     }
}
//@Composable
//fun SettingsListItemSwitches (
//    title: String,
//    subtitle: String,
//    color: Color = Color(0xFF1f2034),
//    image: ImageVector,
//    onCheck: Boolean? = null,
//    onChange: ((Boolean?) -> Unit) = {}
//    )
//{
//    val listItemModifier = Modifier
//        .padding(5.dp)
//        .clip(RoundedCornerShape(8.dp))
//        .border(1.5.dp, Color(0xFF17DB6F), RoundedCornerShape(8.dp))
//        .border(1.5.dp, Color(0xFF20B161), RoundedCornerShape(8.dp))
//        .border(0.8.dp, Color(0xFF20bf6b), RoundedCornerShape(8.dp))
//
//    ListItem(
//        modifier = listItemModifier,
//        headlineContent = { Text(title, fontFamily = FontFamily.Default, fontSize = 17.sp) },
//        supportingContent = { Text(subtitle, fontFamily = FontFamily.Default) },
//        colors = ListItemDefaults.colors(color),
//        leadingContent = { Icon(image, contentDescription = null) },
//        trailingContent = {
//                Switch(
//                    checked = onCheck ?: false,
//                    onCheckedChange = onChange
//                      )
//        }
//    )
//}