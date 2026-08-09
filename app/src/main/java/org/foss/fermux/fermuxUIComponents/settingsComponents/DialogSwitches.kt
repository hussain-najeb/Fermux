package org.foss.fermux.fermuxUIComponents.settingsComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun DialogSwitch(
     modifier: Modifier = Modifier,
     imageModifier: Modifier = Modifier,
     iconModifier: Modifier = Modifier,
     settingIcon: ImageVector? = null,
) {

}



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
     iconTint: Color = FermuxColor().fermuxOffWhiteTextColor,
     border: BorderStroke? = BorderStroke(1.dp, color.fermuxSecondaryBorder),
     onChecked: Boolean? = null,
     onCheckedChange: ((Boolean) -> Unit)? = null,
     content: @Composable (() -> Unit)? = null
) {