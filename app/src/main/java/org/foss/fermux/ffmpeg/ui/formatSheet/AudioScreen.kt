package org.foss.fermux.ffmpeg.ui.formatSheet


import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.logic.MediaKind


@OptIn(ExperimentalGridApi::class)
@Composable
fun AudioFormatSheet(navHostController: NavHostController, viewModel: FFmpegViewModel) {

    Screens(
        sheet = MediaKind.AUDIO,
        navHostController = navHostController,
        viewModel = viewModel
    )

}
