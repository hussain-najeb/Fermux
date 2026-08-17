@file:Suppress("LocalVariableName")

package org.foss.fermux.ffmpeg.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.foss.fermux.ffmpeg.logic.FFmpegStatus
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.ui.ffmpegStateCards.ConversionCard
import org.foss.fermux.ffmpeg.ui.ffmpegStateCards.FFmpegErrorMassage
import org.foss.fermux.ffmpeg.ui.ffmpegStateCards.IdleCard


@Composable
fun FFmepgState (
    state: FFmpegStatus,
    FFmpegLogs: String,
    ffmpegViewModel: FFmpegViewModel) {


    val spatialSpec = MaterialTheme.motionScheme
    val context = LocalContext.current

    AnimatedContent(
        targetState = state,
        transitionSpec = {
            (slideInVertically(
                animationSpec = spatialSpec.slowSpatialSpec(),
                initialOffsetY = { -it }
            ) + fadeIn(initialAlpha = 0.1f))
                .togetherWith(
                    exit = slideOutVertically(
                        animationSpec = spatialSpec.slowSpatialSpec(),
                        targetOffsetY = { -it }
                    ) + fadeOut(targetAlpha = 0.1f)
                )
        },
        label = "ffmpegCardTransition",
        contentKey = { it::class }
    ) { targetState ->
        when (targetState) {
            is FFmpegStatus.Idle -> {
                IdleCard()
            }

            is FFmpegStatus.Converting -> {
                ConversionCard(targetState.progress, targetState.inputUri, FFmpegLogs)
            }

            is FFmpegStatus.Loaded -> {
                ConversionCard(progress = 100f, targetState.inputUri, FFmpegLogs)
            }

            is FFmpegStatus.Error -> {
                FFmpegErrorMassage(
                    errorMessage = targetState.flavourMessage,
                    rawError = targetState.rawError,
                    onCancel = { ffmpegViewModel.cancelButton(context) })
            }
        }
    }
}
