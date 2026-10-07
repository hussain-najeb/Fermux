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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun FilterButton(
     modifier: Modifier = Modifier,
     iconModifier: Modifier = Modifier,
     image: Int,
     enabled: Boolean = true,
     onClick: () -> Unit,
     border: Boolean? = false
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val iconColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.downriver else FermuxColors.white,
          animationSpec = tween(durationMillis = 200),
          label = "Fermux Icon Colors"
     )

     val containerColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveButton else FermuxColors.mutedBlue,
          animationSpec = tween(250)
     )

     val buttonAnimation by animateFloatAsState(
          targetValue = if (isPressed) 0.90f else 1f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Button Animation"
     )

     OutlinedButton(
          modifier = modifier.graphicsLayer {
               scaleX = buttonAnimation
               scaleY = buttonAnimation
          }
               .size(50.dp)
          ,
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(0.dp),
          elevation = ButtonDefaults.buttonElevation(
               defaultElevation = 10.dp,
               pressedElevation = 0.dp,
               focusedElevation = 3.dp,
               hoveredElevation = 5.dp,
               disabledElevation = 0.dp
          ), // TODO. Add more elevation to buttons, they look better that way
          border = if (border == true) BorderStroke(1.dp, FermuxColors.fermuxHelperBorder)
               else BorderStroke(1.dp, FermuxColors.transparent),
          colors = ButtonDefaults.textButtonColors(
               containerColor = containerColor,
               contentColor = FermuxColors.white
          ),
          enabled = enabled,
          interactionSource = interactionSource,
          onClick = onClick
     ) {
          Icon(
               painter = painterResource(id = image),
               tint = iconColor,
               modifier = iconModifier.size(30.dp),
               contentDescription = "Undo",
          )
     }


}