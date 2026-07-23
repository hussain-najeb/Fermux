package org.foss.fermux.fermuxComponents

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColor

@Composable
fun FermuxSurface(
     modifier: Modifier = Modifier,
     expanded: Boolean = false,
     shape: Shape = RoundedCornerShape(4.dp),
     color: FermuxColor = FermuxColor(),
     padding: PaddingValues = PaddingValues(0.dp),
     content: @Composable ColumnScope.() -> Unit
) {

     AnimatedVisibility(
          visible = expanded,
          enter = expandVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeIn(),
          exit = shrinkVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeOut()
     ) {
          Surface(
               modifier = modifier,
               shape = shape,
               border = BorderStroke(1.5.dp, color.fermuxPrimaryBorder),
               color = color.fermuxSurface,
          ) {
               Column(
                    modifier = Modifier.padding(padding)
               ) {
                    content()
               }
          }
     }
}