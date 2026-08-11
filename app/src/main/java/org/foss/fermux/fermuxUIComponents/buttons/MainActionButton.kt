package org.foss.fermux.fermuxUIComponents.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun MainActionButton(
     modifier: Modifier = Modifier,
     icon: ImageVector? = null,
     image: Painter? = null,
     iconRotation: Float = 0f,
     componentSize: Dp = 32.dp,
     color: FermuxColor = FermuxColors,
     enabled: Boolean = true,
     onClick: () -> Unit
) {
     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val containerColor by animateColorAsState(
          targetValue = if (isPressed) color.fermuxActiveButton else color.fermuxInActiveButton,
          animationSpec = tween(200),
          label = "Fermux Container Color",
     )
     val iconColor by animateColorAsState(
          targetValue = if (isPressed) color.fermuxActiveIcon else color.fermuxInActiveIcon,
          animationSpec = tween(150),
          label = "Fermux Icon Color"
     )
     val buttonAnimation by animateFloatAsState(
          targetValue = if (isPressed) 0.90f else 1.0f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Button Animation"
     )
     val iconRotate by animateFloatAsState(
          targetValue = iconRotation,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Icon Rotation"
     )

     val iconModifier = Modifier.rotate(iconRotate).size(componentSize)

     FilledTonalButton(
          modifier = modifier.graphicsLayer {
               scaleX = buttonAnimation
               scaleY = buttonAnimation
          },
          enabled = enabled,
          colors = ButtonDefaults.filledTonalButtonColors(
               containerColor = containerColor,
               contentColor = iconColor,
          ),
          interactionSource = interactionSource,
          contentPadding = PaddingValues(10.dp),
          border = BorderStroke(width = 1.dp, color = color.fermuxSecondaryBorder),
          shape = RoundedCornerShape(8.dp),
          onClick = onClick,
     ) {
          when {
               icon != null -> Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = iconModifier
               )
               image != null -> Icon(
                    painter = image,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = iconModifier
               )
          }
     }
}
