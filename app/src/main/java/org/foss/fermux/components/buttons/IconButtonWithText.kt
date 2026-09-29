package org.foss.fermux.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun TextWithIconButton(
     modifier: Modifier = Modifier,
     textModifier: Modifier = Modifier,
     text: String? = null,
     contentPadding: PaddingValues = PaddingValues(8.dp),
     icon: ImageVector? = null,
     buttonRoundness: Dp? = null,
     contentDescription: String? = null,
     iconRotation: Float = 0f,
     color: FermuxColor = FermuxColors,
     enabled: Boolean = true,
     onClick: () -> Unit
) {
     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val containerColor by animateColorAsState(
          targetValue = when {
               !enabled -> color.fermuxBackground
               isPressed -> color.fermuxActiveButton
               else -> color.fermuxInActiveButton
          },
          animationSpec = tween(200),
          label = "Fermux Button Colors",
     )

     val contentColor by animateColorAsState(
          targetValue = when {
               !enabled -> color.fermuxTextColorBackground
               isPressed -> color.fermuxActiveTextColor
               else -> color.fermuxInActiveTextColor
          },
          animationSpec = tween(200),
          label = "Fermux Text Colors",
     )

     val iconColor by animateColorAsState(
          targetValue = when {
               !enabled -> color.fermuxTextColorBackground
               isPressed -> color.fermuxActiveIcon
               else -> color.fermuxInActiveIcon
          },
          animationSpec = tween(durationMillis = 150),
          label = "Fermux Icon Colors"
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

     ElevatedButton(
          modifier = modifier.graphicsLayer {
               scaleX = buttonAnimation
               scaleY = buttonAnimation
          },
          contentPadding = contentPadding,
          enabled = enabled,
          shape = RoundedCornerShape(buttonRoundness ?: 16.dp),
          colors = ButtonDefaults.textButtonColors(
               containerColor = containerColor,
               contentColor = contentColor
          ),
          onClick = onClick,
          interactionSource = interactionSource,
          border = BorderStroke(width = 1.dp, color = color.fermuxGenericBorder)
     ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
               if (icon != null) {
                    Icon(
                         imageVector = icon, contentDescription = contentDescription,
                         tint = iconColor,
                         modifier = Modifier.rotate(iconRotate)
                    )
               }
               if (text != null) {
                    Text(text, modifier = textModifier.padding(start = if (icon != null) 5.dp else 0.dp))
               }
          }
     }
}
