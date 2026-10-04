package org.foss.fermux.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun AppTextButton(
     modifier: Modifier = Modifier,
     isError: Boolean? = false,
     text: String,
     onClick: () -> Unit
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val textColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.downriver else FermuxColors.white,
          animationSpec = tween(150)
     )

     val errorColor by animateColorAsState(
          if (isPressed) FermuxColors.downriver else FermuxColors.fermuxTextError,
          animationSpec = tween()
     )

     val containerColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.skyBlue else FermuxColors.mutedBlue,
          animationSpec = tween(150)
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
               .width(90.dp),
          elevation = ButtonDefaults.buttonElevation(
               defaultElevation = 10.dp,
               pressedElevation = 0.dp,
               focusedElevation = 3.dp,
               hoveredElevation = 5.dp,
               disabledElevation = 0.dp
          ),
          shape = RoundedCornerShape(8.dp),
          contentPadding = PaddingValues(0.dp),
          border = BorderStroke(1.dp, FermuxColors.transparent),
          colors = ButtonDefaults.textButtonColors(
               containerColor = containerColor,
               contentColor = FermuxColors.white
          ),
          interactionSource = interactionSource,
          onClick = onClick
     ) {
          Text(
               text = text,
               textAlign = TextAlign.Center,
               fontSize = 15.sp,
               fontWeight = FontWeight.SemiBold,
               color = if (isError != false) errorColor else textColor
          )
     }
}

@Preview (backgroundColor = 0xFF15152e, showBackground = true)
@Composable
fun test13 () {

     Column(
          modifier = Modifier.fillMaxSize(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
     ) {
          AppTextButton(
               modifier = Modifier,
               onClick = {},
               isError = true,
               text = "Delete"
          )
     }
}