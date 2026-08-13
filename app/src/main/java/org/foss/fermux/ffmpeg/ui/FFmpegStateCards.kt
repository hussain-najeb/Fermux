@file:Suppress("LocalVariableName")

package org.foss.fermux.ffmpeg.ui

import androidx.compose.runtime.*
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
    navigationController: NavController,
    viewModel: FFmpegViewModel) {

    when (state) {
        is FFmpegStatus.Idle -> {
            IdleCard(navigationController = navigationController)
        }

        is FFmpegStatus.Converting -> {
            ConversionCard(state.progress, state.inputUri, FFmpegLogs)
        }

        is FFmpegStatus.Loaded -> {
            ConversionCard(progress = 100f, state.inputUri, FFmpegLogs)
        }

        is FFmpegStatus.Error -> {
            FFmpegErrorMassage(state.errorMessage, onReset = { viewModel.state = FFmpegStatus.Idle })
        }
    }
}
