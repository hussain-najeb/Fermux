package org.foss.fermux.ffmpeg.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.ffmpeg.logic.FFmpegStatus
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel

@Composable
fun FFmepgState (viewModel: FFmpegViewModel = viewModel(), state: FFmpegStatus) {

    when (state) {
        is FFmpegStatus.Idle -> {}

        is FFmpegStatus.Loading -> {
            LoadingFFmpegCard()
        }

        is FFmpegStatus.Converting -> {
            ConverstionCard(state.progress, state.filePicked)
        }

        is FFmpegStatus.Loaded -> {
            ConverstionCard (state.filePicked)
        }

        is FFmpegStatus.Error -> {
            Text(state.errorMessage)
        }
    }

















}