package org.foss.fermux.ffmpeg.ui.formatSheet


import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel


@OptIn(ExperimentalGridApi::class)
@Composable
fun AudioFormatSheet(navHostController: NavHostController, viewModel: FFmpegViewModel) {

    Screens(
        isSomething = { it.isAudio },
            navHostController = navHostController,
            viewModel = viewModel

    )

}