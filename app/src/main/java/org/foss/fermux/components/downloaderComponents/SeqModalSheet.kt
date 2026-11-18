package org.foss.fermux.components.downloaderComponents

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindowProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SeqFormat
import org.foss.fermux.ytdlp.ui.sequentialDownloader.stateMachine.SeqAudioButtons
import org.foss.fermux.ytdlp.ui.sequentialDownloader.stateMachine.SeqVideoButtons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SequentialModalSheet(
    onDismissRequest: () -> Unit,
    downloaderSettingsViewModel: DownloaderSettingsViewModel
) {

    val format by downloaderSettingsViewModel.seqFormat.collectAsStateWithLifecycle()


    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(
            SheetValue.Hidden,
            SheetValue.Expanded
        )
    )
    val spatialSpec = MaterialTheme.motionScheme


    ModalBottomSheet(
        modifier = Modifier
            .fillMaxWidth(),
        onDismissRequest = onDismissRequest,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = FermuxColors.white
            )
        },
        sheetState = sheetState,
        containerColor = FermuxColors.darkPurple,
        contentColor = FermuxColors.white,
        contentWindowInsets = { WindowInsets(0) },
        properties = ModalBottomSheetProperties(
            isAppearanceLightStatusBars = false,
            isAppearanceLightNavigationBars = false
        ),
    ) {
        val view = LocalView.current
        SideEffect {
            val window = (view as? DialogWindowProvider)?.window ?: (view.parent as? DialogWindowProvider)?.window
            window?.apply {
                isNavigationBarContrastEnforced = false
                @Suppress("DEPRECATION")
                navigationBarColor = FermuxColors.background.toArgb()
            }
        }
        Column {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = "Bulk Configuration",
                    fontSize = 20.sp,
                    color = FermuxColors.white,
                    modifier = Modifier.padding(15.dp)
                )
            }

            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Configure in bulk. Select format, resolution, and audio quality you want for the entire queue.",
                    fontSize = 15.sp,
                    color = FermuxColors.white,
                    modifier = Modifier.padding(15.dp)
                )
            }

            ModularSegmentedButtons(
                optionsList = listOf(
                    SeqFormat.SeqVideo to "Video",
                    SeqFormat.SeqAudio to "Audio"
                ),
                selectedOption = format,
                onSelected = downloaderSettingsViewModel::setSeqFormat
            )

            AnimatedContent(
                targetState = format,
                transitionSpec = {
                    (slideInVertically(
                        animationSpec = spatialSpec.slowSpatialSpec(),
                        initialOffsetY = { -it }
                    ) + fadeIn(initialAlpha = 0.1f))
                        .togetherWith(
                            exit = slideOutVertically(
                                animationSpec = spatialSpec.slowSpatialSpec(),
                                targetOffsetY = { -it }
                            ) + fadeOut(targetAlpha = 0.1f)
                        )
                },
                contentKey = { it::class }
            ) { targetState ->
                when (targetState) {
                    SeqFormat.SeqVideo -> {
                        SeqVideoButtons(downloaderSettingsViewModel)
                    }

                    SeqFormat.SeqAudio -> {
                        SeqAudioButtons(downloaderSettingsViewModel)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(FermuxColors.background)
                .windowInsetsBottomHeight(WindowInsets.navigationBars)
        )
    }
}