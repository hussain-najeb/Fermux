package org.foss.fermux.fermuxUIComponents.ffmpegComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun FFmpegCard(
modifier: Modifier = Modifier,
color: FermuxColor = FermuxColors,
background: Boolean = false,
shape: Shape = RoundedCornerShape(8.dp),
content: @Composable (ColumnScope.() -> Unit)? = null
	){

Card(
modifier = modifier,
shape = shape,
colors = CardDefaults.cardColors(
containerColor = if (background) color.fermuxComponents else color.fermuxFFmpegGreen
		),
border = BorderStroke(1.dp, FermuxColors.fermuxWhiteColor)
	) {
    if (content != null) {
        content()
    	}
	}
}