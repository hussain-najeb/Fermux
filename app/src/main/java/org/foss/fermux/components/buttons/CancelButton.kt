package org.foss.fermux.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun CancelButton(
     modifier: Modifier = Modifier,
     componentSize: Dp = 50.dp,
     color: FermuxColor = FermuxColors,
     allowBorder: Boolean = true,
     border: BorderStroke? = BorderStroke(width = 1.dp, color = FermuxColors.fermuxHelperBorder),
     shape: Shape = RoundedCornerShape(16.dp),
     onClick: () -> Unit,
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val buttonAnimation by animateFloatAsState(
          targetValue = if (isPressed) 0.90f else 1.0f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Button Animation"
     )

     val containerColor by animateColorAsState(
          targetValue = if (isPressed) color.fermuxActiveButton else color.fermuxInActiveButton,
          animationSpec = tween(200),
          label = "Fermux Button Colors",
     )

     val iconColor by animateColorAsState(
          targetValue = if (isPressed) color.downriver else color.fermuxTextError,
          animationSpec = tween(150),
          label = "Fermux Icon Color"
     )

     val iconModifier = Modifier.fillMaxSize()


     OutlinedButton(
          modifier = modifier
               .graphicsLayer {
                    scaleX = buttonAnimation
                    scaleY = buttonAnimation
               }
               .size(componentSize),
          interactionSource = interactionSource,
          contentPadding = PaddingValues(10.dp),
          border = if (!allowBorder) null else border,
          shape = shape,
          onClick = onClick,
          colors = ButtonDefaults.textButtonColors(
               containerColor = containerColor,
          ),
     ) {
          Icon(
               painter = painterResource(id = R.drawable.cancel_buttons),
               contentDescription = null,
               tint = iconColor,
               modifier = iconModifier
          )
     }

}