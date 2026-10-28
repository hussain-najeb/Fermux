package org.foss.fermux.components.downloaderComponents

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.components.generalComponents.LoggingScreen
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.DownloaderViewModel

@Composable
fun DownloaderLogs(
     navController: NavHostController
) {
     val downloaderViewModel: DownloaderViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)

     LoggingScreen(
          loggingTitle = "Downloader Logs",
          logs = downloaderViewModel.downloaderLogs,
          navController = navController
     )
}
