package org.foss.fermux.fermuxUIComponents.downloaderComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun FormatTiles(
     title: String,
     description: String,
     shape: Shape = RoundedCornerShape(1.dp),
     icon: ImageVector? = null,
     image: Int? = null,
     onClick: () -> Unit,
     content: @Composable (() -> Unit)? = null,
     leadingContent: @Composable (() -> Unit)? = null,
     trailingContent: @Composable (() -> Unit)? = null
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val surfaceColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveButton else FermuxColors.fermuxInActiveButton
     )
     val contentColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveIcon else FermuxColors.fermuxWhiteColor,
          animationSpec = tween(400),
          label = "Fermux Container Colors",
     )

     Column(
          modifier = Modifier.fillMaxSize().padding(start = 18.dp, end = 18.dp)
     ) {
          Surface(
               modifier = Modifier.padding(2.dp),
               shape = shape,
               contentColor = contentColor,
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
                              painter = painterResource(id = image),
                              contentDescription = null,
                              modifier = Modifier
                                   .padding(end = 16.dp)
                                   .size(28.dp)
                         )
                    }
                    leadingContent?.invoke()
                    Column(
                         modifier = Modifier
                              .weight(1f)
                              .padding(start = if (icon == null && image == null && leadingContent == null) 12.dp else 5.dp)
                    ) {
                         Text(
                              text = title,
                              maxLines = 1,
                              style = MaterialTheme.typography.titleLarge
                         )
                         Spacer(modifier = Modifier.height(2.dp))

                         Text(
                              text = description,
                              maxLines = 4,
                              style = MaterialTheme.typography.bodyMedium
                         )
                    }
                    content?.invoke()
               }
          }
          trailingContent?.invoke()
     }
}