package org.foss.fermux.fermuxUIComponents.buttons

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
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.R

@Composable
fun GlobalCancelButton(
     modifier: Modifier = Modifier,
     color: FermuxColor = FermuxColors,
     border: BorderStroke? = BorderStroke(0.8.dp, color.fermuxTertiaryBorder),
     contentPadding: PaddingValues = PaddingValues(4.dp),
     onClick: () -> Unit
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val containerColor by animateColorAsState(
          targetValue = when {
               isPressed -> color.fermuxActiveButton
               else -> color.fermuxInActiveBackButton
          },
          animationSpec = tween(200),
          label = "Fermux Button Colors",
     )

     val contentColor by animateColorAsState(
          targetValue = when {
               isPressed -> color.fermuxActiveTextColor
               else -> color.fermuxInActiveTextColor
          },
          animationSpec = tween(200),
          label = "Fermux Text Colors",
     )

     val iconColor by animateColorAsState(
          targetValue = when {
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



     OutlinedButton(
          modifier = modifier.graphicsLayer {
               scaleX = buttonAnimation
               scaleY = buttonAnimation
          }
               .size(25.dp),
          shape = RoundedCornerShape(8.dp),
          border = border,
          colors = ButtonDefaults.textButtonColors(
               containerColor = containerColor,
               contentColor = contentColor
          ),
          contentPadding = contentPadding,
          interactionSource = interactionSource,
          onClick = onClick
     ) {
          Icon(
               painter = painterResource(id = R.drawable.close),
               tint = iconColor,
               contentDescription = "fermux Cancel Button",
          )
     }
}
