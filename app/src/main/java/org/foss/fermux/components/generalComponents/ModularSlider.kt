package org.foss.fermux.components.generalComponents

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.ui.theme.FermuxColors
import kotlin.math.roundToInt

@Composable
fun ModularSlider(
     expanded: Boolean,
     sliderKey: Int,
     trackSteps: Int,
     trackRange: ClosedFloatingPointRange<Float>,
     onOptionSelected: (Int) -> Unit
) {

     val sliderState = rememberSliderState(
          value = sliderKey.toFloat().coerceIn(trackRange),
          steps = trackSteps,
          trackRange = trackRange
     )
     LaunchedEffect(sliderKey) {
          sliderState.value = sliderKey.toFloat().coerceIn(trackRange)
     }

     AnimatedVisibility(
          visible = expanded,
          enter = expandVertically(animationSpec = MaterialTheme.motionScheme.defaultEffectsSpec()) + fadeIn(initialAlpha = 0.2f),
          exit = shrinkVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeOut(targetAlpha = 0.1f)
     ) {
          Surface(
               modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 8.dp, end = 8.dp),
               color = FermuxColors.fermuxComponents,
               shape = RoundedCornerShape(8.dp),
               border = BorderStroke(1.dp, color = FermuxColors.fermuxHelperBorder)
          ) {

               Column(modifier = Modifier.padding(7.dp)) {

                    Text(
                         text = if (sliderKey > 0) "Current Setting: $sliderKey" else "Current Setting: off",
                         color = Color.White,
                         fontFamily = FontFamily.Default,
                         fontStyle = FontStyle.Normal,
                         fontSize = 18.sp,
                         modifier = Modifier.padding(top = 5.dp, start = 5.dp)
                    )

                    Slider(
                         state = sliderState,
                         onValueChange = { value -> sliderState.value = value
                              onOptionSelected(value.roundToInt())
                         },
                         thumb = {
                              Box(
                                   modifier = Modifier
                                        .size(30.dp)
                                        .background(
                                             color = FermuxColors.fermuxGenericBorder,
                                             shape = RoundedCornerShape(4.dp)
                                        )
                              )
                         },
                         modifier = Modifier.padding(7.dp),
                         colors = SliderColors(
                              activeTrackColor = FermuxColors.activeSliderColor,
                              inactiveTrackColor = FermuxColors.inActiveSliderColor,
                              activeTickColor = Color.White,
                              inactiveTickColor = Color.Gray.copy(alpha = 0.5f),
                              thumbColor = Color.Unspecified,
                              disabledThumbColor = Color.Unspecified,
                              disabledActiveTrackColor = Color.Unspecified,
                              disabledActiveTickColor = Color.Unspecified,
                              disabledInactiveTrackColor = Color.Unspecified,
                              disabledInactiveTickColor = Color.Unspecified,
                         )
                    )
               }
          }
     }
}