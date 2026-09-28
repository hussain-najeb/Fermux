package org.foss.fermux.components.buttons

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
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun LogImage(
     modifier: Modifier = Modifier,
     imageModifier: Modifier = Modifier,
     contentDescription: String? = null,
     imageRotation: Float = 0f,
     enabled: Boolean = true,
     componentSize: Dp = 50.dp,
     border: BorderStroke? = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder),
     contentPadding: PaddingValues = PaddingValues(4.dp),
     onClick: () -> Unit
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val containerColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveButton else FermuxColors.fermuxInActiveButton,
          animationSpec = tween(200),
          label = "Fermux Button Colors",
     )

     val contentColor by animateColorAsState(
          targetValue = if (isPressed)  FermuxColors.fermuxActiveTextColor else FermuxColors.fermuxInActiveTextColor,
          animationSpec = tween(200),
          label = "Fermux Text Colors",
     )

     val iconColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveIcon else  FermuxColors.fermuxWhiteColor,
          animationSpec = tween(200),
          label = "Fermux Icon Colors"
     )

     val buttonAnimation by animateFloatAsState(
          targetValue = if (isPressed) 0.90f else 1.0f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Button Animation"
     )

     val iconRotate by animateFloatAsState(
          targetValue = imageRotation,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Icon Rotation"
     )

     ElevatedButton(
          modifier = modifier.graphicsLayer {
               scaleX = buttonAnimation
               scaleY = buttonAnimation
          }
               .size(componentSize),
          shape = RoundedCornerShape(16.dp),
          border = border,
          colors = ButtonDefaults.textButtonColors(
               containerColor = containerColor,
               contentColor = contentColor
          ),
          enabled = enabled,
          contentPadding = contentPadding,
          interactionSource = interactionSource,
          onClick = onClick
     ) {
          Icon(
               painter = painterResource(id = R.drawable.logs),
               tint = iconColor,
               contentDescription = contentDescription,
               modifier = imageModifier.rotate(iconRotate)
          )
     }
}
