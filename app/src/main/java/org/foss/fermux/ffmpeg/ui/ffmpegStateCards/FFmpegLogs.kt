package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.components.generalComponents.LoggingScreen
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.utils.DebugLogFFmpeg

@Composable
fun FFmpegLogs(
     navController: NavHostController
) {
     val ffmpegViewModel: FFmpegViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
     val debugButton by DebugLogFFmpeg.enabled.collectAsStateWithLifecycle()
     val debugLogs by DebugLogFFmpeg.ffmpegLogcat.collectAsStateWithLifecycle()

     LoggingScreen(
          loggingTitle = "Downloader Logs",
          logs = ffmpegViewModel.FFmpegLogs,
          debugLogs = debugLogs,
          debugSwitch = debugButton,
          expanded = false,
          navController = navController
     )
}