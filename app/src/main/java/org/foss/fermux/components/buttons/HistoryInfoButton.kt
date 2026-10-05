package org.foss.fermux.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
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
import org.foss.fermux.R
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun InfoHistoryButton(
     modifier: Modifier = Modifier,
     onClick: () -> Unit
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val iconColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.downriver else FermuxColors.white,
          animationSpec = tween(durationMillis = 200),
     )

     val containerColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveButton else FermuxColors.mutedBlue,
          animationSpec = tween(250)
     )

     val buttonAnimation by animateFloatAsState(
          targetValue = if (isPressed) 0.90f else 1f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
     )

     OutlinedButton(
          modifier = modifier.graphicsLayer {
               scaleX = buttonAnimation
               scaleY = buttonAnimation
          }
               .size(50.dp)
               .padding(5.dp),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(0.dp),
          elevation = ButtonDefaults.buttonElevation(
               defaultElevation = 7.dp,
               pressedElevation = 1.dp,
               focusedElevation = 4.dp,
               hoveredElevation = 5.dp,
               disabledElevation = 2.dp
          ),
          border = BorderStroke(1.dp, FermuxColors.transparent),
          colors = ButtonDefaults.textButtonColors(
               containerColor = containerColor,
               contentColor = FermuxColors.white
          ),
          interactionSource = interactionSource,
          onClick = onClick
     ) {
          Icon(
               painter = painterResource(id = R.drawable.info_circle),
               tint = iconColor,
               modifier = Modifier.size(30.dp),
               contentDescription = "Undo",
          )
     }
}