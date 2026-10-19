package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.foss.fermux.components.generalComponents.LoggingScreen
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialDownloadQueueState

@Composable
fun SeqLogs(
    navController: NavController,
    item: SequentialDownloadQueueState
) {
    LoggingScreen(
        loggingTitle = "Logs",
        logs = item.currentItem?.logs ?: "No logs captured",
        expanded = true,
        navController = navController
    )
}