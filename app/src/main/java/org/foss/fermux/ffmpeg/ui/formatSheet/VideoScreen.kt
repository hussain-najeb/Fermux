package org.foss.fermux.ffmpeg.ui.formatSheet


import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel

@OptIn(ExperimentalGridApi::class)
@Composable
fun VideoFormatSheet(navHostController: NavHostController, viewModel: FFmpegViewModel) {

    Screens(
        isSomething = { it.isVideo },
        navHostController = navHostController,
        viewModel = viewModel

    )

}