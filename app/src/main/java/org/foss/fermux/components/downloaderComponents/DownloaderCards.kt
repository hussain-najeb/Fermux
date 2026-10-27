package org.foss.fermux.components.downloaderComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun DownloaderCard(
    modifier: Modifier = Modifier,
    color: FermuxColor = FermuxColors,
    shape: Shape = RoundedCornerShape(6.dp),
    border: BorderStroke? = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder),
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.padding(15.dp),
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = color.fermuxSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 15.dp,
            pressedElevation = 4.dp
        ),
        border = border
    ) {
        content()
    }
}