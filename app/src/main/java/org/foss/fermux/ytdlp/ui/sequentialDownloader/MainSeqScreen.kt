package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.foss.fermux.R
import org.foss.fermux.components.downloaderComponents.SequentialModalSheet
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.components.generalComponents.SettingAlertDialog
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SequentialContent(
    navController: NavController,
    sequentialViewModel: SequentialViewModel
) {
    val downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel()
    val state = sequentialViewModel.state

    var dialogDismiss by remember { mutableStateOf(false) }

    LargeTopBarScaffold(
        title = "Sequential Downloads",
        onBack = navController::popBackStack,
        firstButton = { dialogDismiss = !dialogDismiss },
        firstImage = R.drawable.info,
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            SequentialStateMachine(
                state = state,
                sequentialViewModel = sequentialViewModel
            )
        }
    }

    if (dialogDismiss) SettingAlertDialog(
        onDismissRequest = { dialogDismiss = false },
        title = "Sequentially?",
        settingImage = R.drawable.sequence,
        specialDescription = buildAnnotatedString {
            withStyle(SpanStyle(FermuxColors.white)) { append("Add a URL for each text field, each entry will be downloaded in the chosen sequence one at a time.\n\nConsider the arguments tab, yes, they also apply, and the general downloader settings as well.\n\nThis tab was designed to work in the background, just press the download button and forget about the tab, each video will be downloaded without intervention.") }
        }
    )

    if (sequentialViewModel.showModalSheet) SequentialModalSheet(
        onDismissRequest = sequentialViewModel::hideDeleteDialog,
        downloaderSettingsViewModel = downloaderSettingsViewModel
    )
}