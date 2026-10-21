package org.foss.fermux.components.ffmpegComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun FFmpegCard(
     modifier: Modifier = Modifier,
     color: FermuxColor = FermuxColors,
     shape: Shape = RoundedCornerShape(8.dp),
     content: @Composable (ColumnScope.() -> Unit)? = null
) {
     Card(
          modifier = modifier.aspectRatio(16f/9f),
          shape = shape,
          elevation = CardDefaults.cardElevation(
               defaultElevation = 15.dp,
               pressedElevation = 4.dp
          ),
          colors = CardDefaults.cardColors(
               containerColor = color.darkPurple
          ),
          border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder)
     ) {
          if (content != null) {
               content()
          }
     }
}

@Preview(backgroundColor = 0xFF15152e, showBackground = true)
@Composable
fun test17() {
     Column(modifier = Modifier.fillMaxSize()) {
          FFmpegCard(
               modifier = Modifier.fillMaxWidth().padding(15.dp),
               shape = RoundedCornerShape(5.dp)
          )
     }
}