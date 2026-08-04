package org.foss.fermux.fermuxUIComponents.buttons

import android.graphics.drawable.Icon
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun FermuxErrorCopyButton(
     modifier: Modifier = Modifier,
     icon: ImageVector,
     color: FermuxColor = FermuxColors,
     onClick: () -> Unit
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val containerColor by animateColorAsState(
          targetValue = when {
               isPressed -> color.fermuxActiveButton
               else -> color.fermuxLightErrorTextColor
          },
          animationSpec = tween(200),
          label = "Fermux Button Colors",
     )



}