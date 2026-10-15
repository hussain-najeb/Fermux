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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun IconTextButton(
     modifier: Modifier,
     image: Int,
     text: String,
     enabled: Boolean? = true,
     border: BorderStroke? = BorderStroke(1.5.dp, FermuxColors.transparent),
     rotation1: Float = 0f,
     rotation2: Float = 0f,
     contentPadding: PaddingValues = PaddingValues(0.dp),
     onClick: () -> Unit,
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()
     val containerColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveButton else FermuxColors.fermuxInActiveButton,
          animationSpec = tween(200),
          label = "Fermux Button Colors",
     )

     val contentColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.downriver else FermuxColors.white,
          animationSpec = tween(200),
          label = "Fermux Text Colors",
     )

     val iconColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.downriver else FermuxColors.white,
          animationSpec = tween(durationMillis = 200),
          label = "Fermux Icon Colors"
     )

     val iconRotate by animateFloatAsState(
          targetValue = if (isPressed) rotation1 else rotation2,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Icon Rotation"
     )

     val textColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.downriver else FermuxColors.white,
          animationSpec = tween(200),

     )

     val buttonAnimation by animateFloatAsState(
          targetValue = if (isPressed) 0.90f else 1.0f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Button Animation"
     )

     OutlinedButton(
          modifier = modifier
               .graphicsLayer {
                    scaleX = buttonAnimation
                    scaleY = buttonAnimation
               }
               .size(40.dp),
          enabled = enabled == true,
          shape = RoundedCornerShape(8.dp),
          colors = ButtonColors(
               containerColor = containerColor,
               contentColor = contentColor,
               disabledContainerColor = FermuxColors.skyBlue,
               disabledContentColor = FermuxColors.downriver
          ),
          elevation = ButtonDefaults.buttonElevation(
               defaultElevation = 10.dp,
               pressedElevation = 0.dp,
               focusedElevation = 3.dp,
               hoveredElevation = 5.dp,
               disabledElevation = 0.dp
          ),
          border = border,
          contentPadding = contentPadding,
          interactionSource = interactionSource,
          onClick = onClick
     ) {
          Icon(
               painter = painterResource(id = image),
               contentDescription = null,
               tint = iconColor,
               modifier = Modifier.rotate(iconRotate)
          )
          Text(
               text = text,
               fontSize = 15.sp,
               color = textColor
          )
     }


}