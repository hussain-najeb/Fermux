package org.foss.fermux.fermuxUIComponents.downloaderComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
     errorBackground: Boolean = false,
     shape: Shape = RoundedCornerShape(8.dp),
     content: @Composable () -> Unit
) {
     Card(
          modifier = modifier
               .padding(16.dp)
               .aspectRatio(16f/9f),
          shape = shape,
          colors = CardDefaults.cardColors(
               containerColor = if (errorBackground) color.fermuxErrorCardColor else  color.fermuxSurface
          ),
          border = BorderStroke(1.dp, FermuxColors.fermuxWhiteColor)
     ) {
          content()
     }
}