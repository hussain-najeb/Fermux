package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.foss.fermux.components.generalComponents.LoggingScreen
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SeqLogs(
    navController: NavController,
    sequentialViewModel: SequentialViewModel,
) {
    val item = sequentialViewModel.state.items.firstOrNull {
        it.id == sequentialViewModel.selectedLogItemId
    }
    val logs = item?.logs.orEmpty().ifBlank { "No logs were recorded for this download" }
    LoggingScreen(
        loggingTitle = "Logs",
        logs = logs,
        navController = navController
    )
}