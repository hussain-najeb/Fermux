package org.foss.fermux.components.downloaderComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.R
import org.foss.fermux.components.buttons.SmallActionButton
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun HistoryCard(
     thumbnail: String,
     contentDescription: String,
     title: String,
     uploader: String,
     format: String,
     resolution: String,
     duration: Int
) {


     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     val iconColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.deepBlue else FermuxColors.fermuxWhiteColor,
          animationSpec = tween(delayMillis = 10),
          label = "color of main page"
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
          modifier = Modifier
          .fillMaxWidth()
               .graphicsLayer {
                    scaleY = size
                    scaleX = size
               }
          .clickable(
               interactionSource = interactionSource,
               indication = null
          ) {
               // TODO. Add the dialog here
          }
          .height(70.dp),
          color = descriptionBackground,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.dp, FermuxColors.something2)
     ) {
          Row(
               modifier = Modifier.fillMaxWidth()
          ) {
               Surface(
                    modifier = Modifier
                         .align(Alignment.CenterVertically),
                    shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
               ) {
                    AsyncImage(
                         model = thumbnail,
                         contentDescription = contentDescription,
                         modifier = Modifier.aspectRatio(16f / 9f)
                    )
//                    Text(
//                         text = resolution,
//                         fontSize = 14.sp,
//                         fontStyle = FontStyle.Italic,
//                         fontFamily = FontFamily.Default,
//                         fontWeight = FontWeight.SemiBold,
//                         minLines = 1,
//                         maxLines = 2,
//                         overflow = TextOverflow.Ellipsis,
//                         color = titleColor,
//                         modifier = Modifier.padding(2.dp)
//                    )
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
                                   fontSize = 14.sp,
                                   fontStyle = FontStyle.Italic,
                                   fontFamily = FontFamily.Default,
                                   fontWeight = FontWeight.SemiBold,
                                   minLines = 1,
                                   maxLines = 2,
                                   overflow = TextOverflow.Ellipsis,
                                   color = titleColor,
                                   modifier = Modifier.padding(2.dp)
                              )
                              Row(modifier = Modifier.padding(2.dp)) {
                                   Text(
                                        text = uploader,
                                        fontSize = 11.sp,
                                        fontStyle = FontStyle.Normal,
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.Normal,
                                        minLines = 1,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = titleColor,
                                        modifier = Modifier.padding(2.dp)
                                   )
                                   Text(
                                        text = format,
                                        fontSize = 11.sp,
                                        fontStyle = FontStyle.Normal,
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.Normal,
                                        minLines = 1,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        color = titleColor,
                                        modifier = Modifier.padding(2.dp)
                                   )
                              }
                         }
                         SmallActionButton(
                              modifier = Modifier
                                   .align(Alignment.Bottom)
                                   .padding(5.dp),
                              onClick = {  },
                              image = R.drawable.info_circle
                         )
                    }
               }
          }
     }
}