package org.foss.fermux.components.downloaderComponents

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.components.generalComponents.LoggingScreen
import org.foss.fermux.utils.DebugLogDownloader
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel

@Composable
fun DownloaderLogs(
     navController: NavHostController
) {
     val downloaderViewModel: DownloaderViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
     val debugButton by DebugLogDownloader.enabled.collectAsStateWithLifecycle()
     val debugLogs by DebugLogDownloader.downloaderLogcat.collectAsStateWithLifecycle()

     LoggingScreen(
          loggingTitle = "Downloader Logs",
          logs = downloaderViewModel.downloaderLogs,
          debugLogs = debugLogs,
          debugSwitch = debugButton,
          expanded = false,
          navController = navController
     )
}
