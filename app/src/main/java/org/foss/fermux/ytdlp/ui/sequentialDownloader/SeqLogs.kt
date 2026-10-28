package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import org.foss.fermux.components.generalComponents.LoggingScreen
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SeqLogs(
    navController: NavController,
    sequentialViewModel: SequentialViewModel,
    itemId: String
) {
    val item = sequentialViewModel.state.items.firstOrNull { it.toString() == itemId }
    LoggingScreen(
        loggingTitle = "Logs",
        logs = item?.logs.orEmpty(),
        navController = navController
    )
}

// TODO. remember to remove the Debug feature from the app