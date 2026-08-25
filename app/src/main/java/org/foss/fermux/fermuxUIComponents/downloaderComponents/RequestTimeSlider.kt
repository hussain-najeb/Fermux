package org.foss.fermux.fermuxUIComponents.downloaderComponents

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.W600
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import kotlin.math.roundToInt

@Composable
fun RequestTimeSlider(
     expanded: Boolean,
     @SuppressLint("ContextCastToActivity") settingsViewModel: DownloaderSettingsViewModel = viewModel(
          viewModelStoreOwner = LocalContext.current as ComponentActivity
     )
) {

     val sleepRequest by settingsViewModel.sleepRequest.collectAsStateWithLifecycle()


     AnimatedVisibility(
          visible = expanded,
          enter = slideInVertically(animationSpec = tween(200)) + fadeIn(initialAlpha = 0.3f),
          exit = slideOutVertically(animationSpec = tween(250)) + fadeOut(targetAlpha = 0.1f)
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
                         text = "Current Time: $sleepRequest",
                         color = Color.White,
                         fontWeight = W600,
                         modifier = Modifier.padding(top = 8.dp, start = 8.dp)
                    )
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
}