package org.foss.fermux.fermuxUIComponents.settingsComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.Start
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun SettingLists(
     title: String,
     description: String,
     icon: ImageVector? = null,
     image: Painter? = null,
     onClick: () -> Unit,
     content: @Composable (() -> Unit)? = null,
     trailingContent: @Composable (() -> Unit)? = null
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()


     val surfaceColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveButton else FermuxColors.fermuxInActiveButton
     )
     val contentColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveIcon else FermuxColors.fermuxWhiteColor,
          animationSpec = tween(200),
          label = "Fermux Container Colors",
     )
     val borderColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveIcon else FermuxColors.fermuxGenericBorder
     )


     Column(modifier = Modifier.fillMaxSize()
     ) {
          Surface(
               modifier = Modifier.padding(3.dp),
               shape = RoundedCornerShape(8.dp),
               contentColor = contentColor,
               border = BorderStroke(width = 1.dp, color = borderColor),
               interactionSource = interactionSource,
               onClick = onClick,
               color = surfaceColor
          ) {
               Row(
                    modifier = Modifier
                         .fillMaxWidth()
                         .padding(horizontal = 16.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
               ) {
                    icon?.let {
                         Icon(
                              imageVector = icon,
                              contentDescription = null,
                              modifier = Modifier
                                   .padding(end = 16.dp)
                                   .size(28.dp)
                         )
                    }

                    if (image != null) {
                         Icon(
                              painter = image,
                              contentDescription = null,
                              modifier = Modifier
                                   .padding(end = 16.dp)
                                   .size(28.dp)
                         )
                    }
                    Column(
                         modifier = Modifier
                              .weight(1f)
                              .padding(start = if (icon == null) 12.dp else 0.dp)
                    ) {
                         Text(
                              text = title,
                              maxLines = 1,
                              style = MaterialTheme.typography.titleLarge,
                              overflow = TextOverflow.Ellipsis,
                         )
                         Spacer(modifier = Modifier.height(2.dp))
                         Text(
                              text = description,
                              maxLines = 2,
                              style = MaterialTheme.typography.bodyMedium,
                              overflow = TextOverflow.Ellipsis,
                         )
                    }
                    content?.invoke()
               }
          }
          trailingContent?.invoke()
     }
}

@Preview (heightDp = 900, showBackground = true,backgroundColor = 0xFF181825)
@Composable
fun Previews() {
     Column(modifier = Modifier.padding(6.dp)) {
          SettingLists(
               "titel",
               "espikfjosieng",
               icon = Icons.Outlined.Start,
               onClick = {},

          )
     }
}
