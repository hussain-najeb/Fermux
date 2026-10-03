package org.foss.fermux.components.buttons

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SettingsResetButton(
     modifier: Modifier = Modifier,
     expanded: Boolean,
     settingText: String,
     onClick: () -> Unit
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val containerColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxRedDeleteColorActive else FermuxColors.fermuxRedDeleteColorInActive,
          animationSpec = tween(200),
          label = "Fermux Button Colors",
     )


     val buttonAnimation by animateFloatAsState(
          targetValue = if (isPressed) 0.90f else 1.0f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Button Animation"
     )

     AnimatedVisibility(
          visible = expanded,
          enter = expandVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeIn(initialAlpha = 0.2f),
          exit = shrinkVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeOut(targetAlpha = 0.1f)
     ) {
          Surface(
               modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp),
               color = FermuxColors.warmPurple,
               shape = RoundedCornerShape(8.dp),
               border = BorderStroke(1.dp, color = FermuxColors.fermuxHelperBorder)
          ) {
     ElevatedButton(
          modifier = modifier.graphicsLayer {
               scaleX = buttonAnimation
               scaleY = buttonAnimation
          }
               .padding(10.dp)
               .fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.textButtonColors(
               containerColor = containerColor
          ),
          interactionSource = interactionSource,
          onClick = onClick
     ) {
          Box(modifier = Modifier.fillMaxWidth()) {
                         
          Icon(
               imageVector = Icons.Filled.ClearAll,
               tint = FermuxColors.fermuxLightErrorTextColor,
               contentDescription = "Reset button",
               modifier = Modifier.padding(2.dp).align(Alignment.CenterStart)
          )
     
          Text(
               text = settingText,
               textAlign = TextAlign.Center,
               fontSize = 15.sp,
               color = FermuxColors.white,
               modifier = Modifier.padding(5.dp).align(Alignment.Center)
                         )
                    } 
               }
          }
     }
}