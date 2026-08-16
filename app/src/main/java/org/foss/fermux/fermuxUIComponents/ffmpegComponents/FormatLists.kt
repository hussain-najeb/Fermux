package org.foss.fermux.fermuxUIComponents.ffmpegComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun FormatLists(
     title: String,
     description: String,
     image: Int? = null,
     onClick: () -> Unit
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
                    if (image != null) {
                         Icon(
                              painter = painterResource(id = image),
                              contentDescription = null,
                              modifier = Modifier
                                   .padding(end = 16.dp)
                                   .size(28.dp)
                         )
                    }
                    Column(
                         modifier = Modifier
                              .weight(1f)
                              .padding(start = 12.dp)
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
               }
          }
     }
}