package org.foss.fermux.ytdlp.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp


@Composable
fun DownloadedAudioScreen() {

    var animationState by remember { mutableStateOf(true) }

    val animationProgress by animateFloatAsState(
        targetValue = if (animationState) 0f else 1f,
        animationSpec = MaterialTheme.motionScheme.defaultSpatialSpec()
    )
    Box(

        modifier = Modifier
            .size(100.dp)
            .graphicsLayer {
                this.scaleX = 1f + animationProgress
                this.scaleY = 1f + animationProgress

            }
            .background(Color.Black)
            .clickable { animationState = !animationState }

    )


}
