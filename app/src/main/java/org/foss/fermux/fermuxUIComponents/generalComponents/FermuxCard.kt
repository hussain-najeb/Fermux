package org.foss.fermux.fermuxUIComponents.generalComponents

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
     situationalCardColor: Boolean? = null,
     shape: Shape = RoundedCornerShape(4.dp),
     border: BorderStroke? = BorderStroke(1.5.dp, color.fermuxPrimaryBorder),
     pressable: Boolean? = null,
     onClick: (() -> Unit)? = null,
     content: @Composable ColumnScope.() -> Unit
) {
     val isCardPressable = pressable == true

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val cardExpansion by animateFloatAsState(
          targetValue = if (isPressed && isCardPressable) 0.96f else 1.0f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Card Get Bigger"
     )

     val targetColor = if (situationalCardColor == true || isPressed) {
          color.fermuxComponents
     } else {
          color.fermuxSurface
     }

     val cardColor by animateColorAsState(
          targetValue = targetColor,
          animationSpec = tween(150),
          label = "Fermux Card Color"
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
          onClick = { if (isCardPressable) onClick?.invoke() }
     ) {
          content()
     }
}