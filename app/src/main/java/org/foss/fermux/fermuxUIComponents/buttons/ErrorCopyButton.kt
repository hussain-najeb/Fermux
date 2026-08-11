package org.foss.fermux.fermuxUIComponents.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun ErrorCopyButton(
     modifier: Modifier = Modifier,
     onClick: () -> Unit
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val containerColor by animateColorAsState(
          targetValue = when {
               isPressed -> FermuxColors.fermuxActiveButton
               else -> Color.White
          },
          animationSpec = tween(200),
          label = "Fermux Button Colors",
     )
     val iconColor by animateColorAsState(
          targetValue = when {
               isPressed -> FermuxColors.fermuxActiveIcon
               else -> FermuxColors.fermuxInActiveButton
          }
     )
     val buttonAnimation by animateFloatAsState(
          targetValue = if (isPressed) 0.90f else 1.0f,
          animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
          label = "Fermux Button Animation"
     )
     ElevatedButton(
          modifier = modifier.graphicsLayer {
               scaleX = buttonAnimation
               scaleY = buttonAnimation
          }
               .size(60.dp)
               .padding(6.dp),
          shape = RoundedCornerShape(8.dp),
          colors = ButtonDefaults.textButtonColors(
               containerColor = containerColor,
          ),
          contentPadding = PaddingValues(5.dp),
          interactionSource = interactionSource,
          onClick = onClick
     ) {
          Icon(
               imageVector = Icons.Default.ContentCopy ,
               tint = iconColor,
               contentDescription = "Copy Error",
          )
     }



}
