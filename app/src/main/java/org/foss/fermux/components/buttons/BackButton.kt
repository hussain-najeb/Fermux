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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun BackButton(
     modifier: Modifier = Modifier,
     border: BorderStroke? = BorderStroke(1.dp, Color.Transparent),
     contentPadding: PaddingValues = PaddingValues(4.dp),
     onClick: () -> Unit
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()
     var isClickable by remember { mutableStateOf(true) }

     val iconColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.downriver else FermuxColors.white,
          animationSpec = tween(durationMillis = 150),
          label = "Fermux Icon Colors"
     )

     val buttonAnimation by animateFloatAsState(
          targetValue = if (isPressed) 0.95f else 1.0f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Button Animation"
     )

     OutlinedButton(
          modifier = modifier.graphicsLayer {
               scaleX = buttonAnimation
               scaleY = buttonAnimation
          }
               .size(25.dp),
          shape = RoundedCornerShape(8.dp),
          border = border,
          colors = ButtonDefaults.textButtonColors(
               containerColor = Color.Transparent,
               contentColor = Color.Transparent
          ),
          contentPadding = contentPadding,
          interactionSource = interactionSource,
          onClick = {
               if (isClickable) {
                    isClickable = false
                    onClick()
               }
          }
     ) {
          Icon(
               painter = painterResource(id = R.drawable.back_arrow),
               tint = iconColor,
               contentDescription = "Back Button",
          )
     }
}
