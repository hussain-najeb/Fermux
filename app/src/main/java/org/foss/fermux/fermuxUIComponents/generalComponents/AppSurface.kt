package org.foss.fermux.fermuxUIComponents.generalComponents


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
import androidx.compose.ui.graphics.Color
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun AppSurface(
     modifier: Modifier = Modifier,
     shape: Shape = RoundedCornerShape(4.dp),
     expanded: Boolean = false,
     color: Color = FermuxColors.fermuxSurface,
     border: BorderStroke? = BorderStroke(1.5.dp, FermuxColors.fermuxPrimaryBorder),
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
               border = border,
               color = color,
          ) {
               Column(
                    modifier = Modifier.padding(padding)
               ) {
                    content()
               }
          }
     }
}
