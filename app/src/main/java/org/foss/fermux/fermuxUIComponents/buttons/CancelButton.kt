package org.foss.fermux.fermuxUIComponents.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun FermuxCancelButton(
     modifier: Modifier = Modifier,
     iconRotation: Float = 0f,
     componentSize: Dp = 32.dp,
     color: FermuxColor = FermuxColors,
     onClick: () -> Unit,
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val buttonAnimation by animateFloatAsState(
          targetValue = if (isPressed) 0.90f else 1.0f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Button Animation"
     )

     val iconColor by animateColorAsState(
          targetValue = if (isPressed) color.fermuxTextError else color.fermuxInActiveIcon,
          animationSpec = tween(150),
          label = "Fermux Icon Color"
     )

     val iconRotate by animateFloatAsState(
          targetValue = iconRotation,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Icon Rotation"
     )

     val iconModifier = Modifier.rotate(iconRotate).size(componentSize)


     OutlinedButton(
          modifier = modifier
               .graphicsLayer {
                    scaleX = buttonAnimation
                    scaleY = buttonAnimation
               },

          interactionSource = interactionSource,
          contentPadding = PaddingValues(10.dp),
          border = BorderStroke(width = 1.dp, color = color.fermuxTertiaryBorder),
          shape = CircleShape,
          onClick = onClick,
          colors = ButtonDefaults.buttonColors(
               containerColor = FermuxColors.fermuxComponents.copy(alpha = 0.40f)
          )
     ) {
          Icon(
               imageVector = Icons.Default.Cancel,
               contentDescription = null,
               tint = iconColor,
               modifier = iconModifier
          )
     }
}