package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.foss.fermux.components.generalComponents.LoggingScreen
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SeqLogs(
    navController: NavController,
    sequentialViewModel: SequentialViewModel

) {
    val logs = sequentialViewModel.currentLogs
    LoggingScreen(
        loggingTitle = "Logs",
        logs = logs,
        expanded = true,
        navController = navController
    )
}