package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.animation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialDownloadQueueState
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialStatus
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SequentialStateMachine(
    state: SequentialDownloadQueueState,
    navController: NavController,
    sequentialViewModel: SequentialViewModel
) {

    val context = LocalContext.current
    val spatialSpec = MaterialTheme.motionScheme

    AnimatedContent(
        targetState = state,
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
        label = "DownloaderCardTransition",
        contentKey = { it.phase }
    ) { targetState ->
        when (targetState.phase) {

            SequentialStatus.SequentiallyIdle -> {
                SequentialIdle(
                     navController = navController,
                     sequentialViewModel = sequentialViewModel
                )
            }

            SequentialStatus.SequentiallyFetchingMetadata -> {
                SeqFetchMetadata(sequentialViewModel = sequentialViewModel)
            }

            SequentialStatus.SequentiallyDownloading -> {
                // SequentialDownloading()
            }

            SequentialStatus.SequentiallyCompleted -> {
                // SequentialSuccess()
            }

            SequentialStatus.SequentialError -> {
                // SequentialError()
            }

            else -> {}


        }
    }
}

@Composable
fun SequentialContent(
    navController: NavController,
    sequentialViewModel: SequentialViewModel = viewModel()
) {
    val state = sequentialViewModel.state

    if (state == null) {
        SequentialIdle(
            navController = navController,
            sequentialViewModel = sequentialViewModel
        )
    } else {
        SequentialStateMachine(
            state = state,
            navController = navController,
            sequentialViewModel = sequentialViewModel
        )
    }
}