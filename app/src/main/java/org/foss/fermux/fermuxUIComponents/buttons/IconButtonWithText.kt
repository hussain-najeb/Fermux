package org.foss.fermux.fermuxUIComponents.buttons

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
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

/**
 * A flexible Fermux-styled text button that supports an optional icon and/or text label.
 *
 * Handles all press animation, color transitions, and icon rotation internally —
 * the caller only needs to provide target values, not animation logic.
 *
 * @param modifier Applied to the outer [TextButton]. Use this to control size and positioning.
 * @param text Optional label rendered to the right of the icon. Pass null to hide.
 * @param contentPadding Internal padding between the button edge and its content.
 * @param icon Optional leading icon. Pass null to hide.
 * @param iconRotation Target rotation angle for the icon in degrees. The button animates
 * to this value automatically — useful for expand/collapse chevrons. Defaults to 0f (no rotation).
 * @param buttonRoundness Corner radius of the button shape. Defaults to 16.dp if not provided.
 * @param color Fermux color scheme. Defaults to [FermuxColors].
 * @param enabled Whether the button is interactive. When false, the button dims and
 * blocks clicks — the caller controls when and how long this lasts.
 * @param textModifier A modifier for the text inside the button
 * @param onClick Called when the button is tapped.
 */

@Composable
fun FermuxTextWithIconButton(
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