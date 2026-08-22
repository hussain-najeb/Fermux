package org.foss.fermux.fermuxUIComponents.settingsComponents

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.fermuxUIComponents.buttons.AppIconButton
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import kotlin.math.roundToInt

@Composable
fun RequestTimeSlider(expanded: Boolean,
     @SuppressLint("ContextCastToActivity") settingsViewModel: DownloaderSettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)) {

     val sleepRequest by settingsViewModel.sleepRequest.collectAsStateWithLifecycle()


     AnimatedVisibility(
          visible = expanded,
          enter = slideInVertically(animationSpec = tween(200)) + fadeIn(initialAlpha = 0.3f),
          exit = slideOutVertically(animationSpec = tween(250)) + fadeOut(targetAlpha = 0.1f)
     ) {
          Surface(modifier = Modifier
               .wrapContentSize()
               .padding(start = 8.dp, end = 8.dp),
               color = FermuxColors.fermuxComponents,
               shape = RoundedCornerShape(8.dp),
               border = BorderStroke(1.dp, color = FermuxColors.fermuxHelperBorder)
               ) {
               Slider(
                    value = sleepRequest.coerceIn(0, 5).toFloat(),
                    onValueChange = { value ->
                         settingsViewModel.setSleepRequest(value.roundToInt())
                    },
                    valueRange = 0f..5f,
                    steps = 4,
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


@Preview
@Composable
fun SliderPreview() {
     var value by remember { mutableFloatStateOf(1f) }
     var sliderAnimation by remember { mutableStateOf(false) }



     Column(modifier = Modifier.fillMaxSize()) {


          AppIconButton(
              icon = Icons.Default.ExpandMore,
               onClick = { sliderAnimation = !sliderAnimation }
          )


          AnimatedVisibility(
               visible = sliderAnimation,
               enter = slideInVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeIn(
                    initialAlpha = 0.1f
               ),
               exit = slideOutVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeOut(
                    targetAlpha = 0.1f
               )
          ) {

               Surface(
                    modifier = Modifier.padding(8.dp),
                    color = FermuxColors.fermuxComponents
               ) {
                    Slider(
                         value = value,
                         onValueChange = { value = it },
                         valueRange = 1f..5f,
                         steps = 3,
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