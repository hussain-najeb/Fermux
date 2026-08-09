package org.foss.fermux.fermuxUIComponents.downloaderComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun FermuxDownloadDescription(
     modifier: Modifier = Modifier,
     shape: Shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp),
     content: @Composable () -> Unit
     ) {

     Surface(
          modifier = modifier
               .background(FermuxColors.fermuxComponents),
          shape = shape,
     ) {
          content()
     }

}