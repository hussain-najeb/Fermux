package org.foss.fermux.fermuxComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors





@Composable
fun FermuxCard(
     modifier: Modifier = Modifier,
     color: FermuxColor = FermuxColors,
     shape: Shape = RoundedCornerShape(4.dp),
     border: BorderStroke? = BorderStroke(1.5.dp, color.fermuxPrimaryBorder),
     onClick: (() -> Unit)? = null,
     content: @Composable ColumnScope.() -> Unit
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val cardExpansion by animateFloatAsState(
          targetValue = if (isPressed) 0.95f else 1.0f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "card get bigger"
     )

     val cardColor by animateColorAsState(
          targetValue = if (isPressed) color.fermuxComponents else color.fermuxSurface,
          animationSpec = tween(150),
          label = "Fermux Icon Color"
     )

     Card(
          modifier = modifier
               .graphicsLayer {
                    scaleX = cardExpansion
                    scaleY = cardExpansion
               },
          interactionSource = interactionSource,
          colors = CardDefaults.cardColors(
               containerColor = cardColor,
          ),
          shape = shape,
          border = border,
          onClick = { onClick?.invoke() }
     ) {
       content()
     }
}