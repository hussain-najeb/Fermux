package org.foss.fermux.fermuxUIComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun FermuxFinishedDownloadCard(
     modifier: Modifier = Modifier,
     shape: Shape = RoundedCornerShape(10.dp),
     colors: CardColors = CardDefaults.elevatedCardColors(
          containerColor = FermuxColors.fermuxComponents // Set your dark color here
     ),
     content: @Composable ColumnScope.() -> Unit
) {

     ElevatedCard(
          modifier = modifier
               .fillMaxWidth()
               .padding(20.dp),
          shape = shape,
          colors = colors,
     ) {
          content()
     }
}