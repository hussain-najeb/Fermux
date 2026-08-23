package org.foss.fermux.fermuxUIComponents.ffmpegComponents

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import kotlin.math.roundToInt

@Composable
fun ResolutionSelect(
    expanded: Boolean,
    @SuppressLint("ContextCastToActivity") settingsViewModel: FFmpegSettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {
    val resolution by settingsViewModel.videoResolution.collectAsStateWithLifecycle()
    val options = listOf("" to "Original", "480" to "480p", "720" to "720p", "1080" to "1080p")

    AnimatedVisibility(
        visible = expanded,
        enter = slideInVertically(animationSpec = tween(200)) + fadeIn(initialAlpha = 0.3f),
        exit = slideOutVertically(animationSpec = tween(250)) + fadeOut(targetAlpha = 0.1f)
    ) {
        Surface(
            modifier = Modifier.wrapContentSize().padding(start = 8.dp, end = 8.dp),
            color = FermuxColors.fermuxComponents,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, color = FermuxColors.fermuxHelperBorder)
        ) {
            Row(modifier = Modifier.padding(7.dp)) {
                options.forEach { (value, label) ->
                    FilterChip(
                        selected = resolution == value,
                        onClick = { settingsViewModel.setVideoResolution(value) },
                        label = { Text(label) }
                    )
                }
            }
        }
    }
}