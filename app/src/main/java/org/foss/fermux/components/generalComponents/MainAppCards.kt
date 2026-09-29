package org.foss.fermux.components.generalComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.foss.fermux.components.buttons.ImageButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.MainScreens

@Composable
fun MainAppCard(
     modifier: Modifier,
     navController: NavController,
     title: String,
     description: String,
     image: Int? = null,
     buttonImage: Int? = null,
     buttonOnClick: (() -> Unit)? = null,
     trailingContent: @Composable (() -> Unit)? = null,
     icon: ImageVector? = null,
     route: MainScreens,
     enabled: Boolean
) {
     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()


     val iconColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxWhiteColor else FermuxColors.fermuxWhiteColor,
          animationSpec = tween(delayMillis = 10),
          label = "color of main page"
     )

     val iconBackground by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.deepBlue else FermuxColors.fermuxComponents,
          animationSpec = tween(delayMillis = 10),
          label = "color of main icons page"
     )

     val titleColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxOffWhiteTextColor else FermuxColors.fermuxWhiteColor,
          label = "color of main title of main page"
     )

     val descriptionBackground by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxSurface else FermuxColors.fermuxSaturatedComponents,
          label = "color of main background of the description surface"
     )

     val size by animateFloatAsState(
          targetValue = if (isPressed) 0.98f else 1f,
          animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
          label = ""
     )


     Surface(
          modifier = modifier
          .fillMaxWidth()
               .graphicsLayer {
                    scaleY = size
                    scaleX = size
               }
          .clickable(
               interactionSource = interactionSource,
               indication = null
          ) {
               navController.navigate(route.route)
          }
          .height(100.dp),
          color = descriptionBackground,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.5.dp, FermuxColors.something2)
     ) {
          Row(
               modifier = Modifier.fillMaxWidth()
          ) {
               Surface(
                    modifier = Modifier
                         .fillMaxHeight()
                         .align(Alignment.CenterVertically),
                    shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                    color = iconBackground
               ) {
                    if (icon != null)
                         Icon(
                              imageVector = icon,
                              contentDescription = null,
                              modifier = Modifier.size(100.dp)
                         )
                    if (image != null)
                         Image(
                              painter = painterResource(id = image),
                              contentDescription = null,
                              colorFilter = ColorFilter.tint(iconColor),
                              contentScale = ContentScale.Crop,
                              modifier = Modifier
                                   .size(100.dp)
                                   .align(Alignment.CenterVertically)
                         )
               }

               Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = descriptionBackground,
               ) {
                    Row(modifier = Modifier.padding(2.dp).fillMaxSize()) {
                         Column(
                              modifier = Modifier
                                   .weight(1f)
                                   .padding(10.dp)
                         ) {
                              Text(
                                   text = title,
                                   fontSize = 18.sp,
                                   fontStyle = FontStyle.Italic,
                                   fontFamily = FontFamily.Default,
                                   fontWeight = FontWeight.SemiBold,
                                   minLines = 1,
                                   maxLines = 5,
                                   overflow = TextOverflow.Ellipsis,
                                   color = titleColor
                              )
                              Text(
                                   text = description,
                                   fontSize = 13.sp,
                                   fontStyle = FontStyle.Normal,
                                   fontFamily = FontFamily.Default,
                                   fontWeight = FontWeight.Normal,
                                   minLines = 1,
                                   maxLines = 5,
                                   overflow = TextOverflow.Ellipsis,
                                   color = titleColor,
                                   modifier = Modifier.padding(top = 5.dp, end = 5.dp)
                              )
                         }
                         if (buttonImage != null)
                         ImageButton(
                              modifier = Modifier
                                   .align(Alignment.CenterVertically)
                                   .padding(end = 10.dp),
                              onClick = { buttonOnClick?.invoke() },
                              image = buttonImage,
                              enabled = enabled
                         )
                    }
               }
          }
     }
     trailingContent?.invoke()
}
