package org.foss.fermux.fermuxUIComponents.ffmpegComponents

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import kotlin.math.roundToInt

@Composable
fun CrfSlider(
     expanded: Boolean,
     @SuppressLint("ContextCastToActivity") settingsViewModel: FFmpegSettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {

     val videoCrf by settingsViewModel.videoCrf.collectAsStateWithLifecycle()
     val sliderState = rememberSliderState(
          value = videoCrf.coerceIn(18, 28).toFloat(),
          steps = 9,
          trackRange = 18f..28f
     )
     LaunchedEffect(videoCrf) {
          sliderState.value = videoCrf.coerceIn(18, 28).toFloat()
     }

     AnimatedVisibility(
          visible = expanded,
          enter = expandVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeIn(initialAlpha = 0.2f),
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
                         text = "Current Setting: $videoCrf",
                         color = Color.White,
                         fontWeight = W600,
                         modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                    )

                    Slider(
                         state = sliderState,
                         onValueChange = { value -> sliderState.value = value
                              settingsViewModel.setVideoCrf(value.roundToInt())
                         },
                         thumb = {
                              Box(
                                   modifier = Modifier

                                        .size(25.dp)
                                        .background(
                                             color = FermuxColors.fermuxGenericBorder,
                                             shape = RoundedCornerShape(6.dp)
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