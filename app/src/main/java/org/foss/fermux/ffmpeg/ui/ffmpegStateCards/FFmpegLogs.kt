package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.components.generalComponents.LoggingScreen
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel

@Composable
fun FFmpegLogs(
     navController: NavHostController
) {
     val ffmpegViewModel: FFmpegViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)

     LoggingScreen(
          loggingTitle = "Downloader Logs",
          logs = ffmpegViewModel.FFmpegLogs,
          navController = navController
     )
}