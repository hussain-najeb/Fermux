package org.foss.fermux.fermuxUIComponents.generalComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.MainScreens

@Composable
fun MainAppCard(
     modifier: Modifier,
     navController: NavController,
     title: String,
     description: String,
     image: Int? = null,
     icon: ImageVector? = null,
     route: MainScreens
) {

     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()


     val pressedColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveButton else FermuxColors.fermuxInActiveButton,
          label = "color of main page"
     )

     val pressedIcon by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxActiveIcon else FermuxColors.fermuxWhiteColor,
          label = "color of main icons page"
     )


     Surface( modifier = modifier
          .fillMaxWidth()
          .clickable(
               interactionSource = interactionSource,
               indication = null
          ) {
               navController.navigate(route.route)
          }
          .height(100.dp),
          color = pressedColor,
          contentColor = FermuxColors.fermuxActiveButton,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.5.dp, FermuxColors.something2)
     ) {
          Row(modifier = Modifier.fillMaxWidth() ) {
               if (icon != null)
                    Icon(
                         imageVector = icon,
                         contentDescription = null,
                         modifier = Modifier.size(100.dp)
                    ) else null

               if (image != null)
                    Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(pressedIcon),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                         .size(70.dp)
                         .align(Alignment.CenterVertically)
               ) else null

               Column(modifier = Modifier.padding(10.dp)
               ) {
                    Text(
                         text = title,
                         fontSize = 18.sp,
                         fontStyle = FontStyle.Italic,
                         fontFamily = FontFamily.Default,
                         fontWeight = FontWeight.SemiBold,
                         color = pressedIcon,
                         modifier = Modifier.padding(2.dp)
                    )
                    Text(
                         text = description,
                         fontSize = 14.sp,
                         fontStyle = FontStyle.Normal,
                         fontFamily = FontFamily.Default,
                         fontWeight = FontWeight.Normal,
                         color = pressedIcon,
                         modifier = Modifier.padding(2.dp)
                    )
               }
          }
     }

}