package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderQualityFormatState


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel
import org.foss.fermux.ytdlp.logic.downloader.FormatKind
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderQualityFormatState.AudioQualityChoices
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderQualityFormatState.IdleQualityChoices
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderQualityFormatState.VideoQualityChoices


@Composable
fun QualityState(downloaderViewModel: DownloaderViewModel) {

    var pickedFormat by remember { mutableStateOf(FormatKind.Idle) }
    val spatialSpec = MaterialTheme.motionScheme
    val context = LocalContext.current


    AnimatedContent(
        targetState = pickedFormat,
        transitionSpec = {
            (slideInVertically(animationSpec = spatialSpec.slowSpatialSpec(), initialOffsetY = { -it }) + fadeIn(initialAlpha = 0.1f))
                .togetherWith(
                    exit = slideOutVertically(animationSpec = spatialSpec.slowSpatialSpec(), targetOffsetY = { -it }) + fadeOut(targetAlpha = 0.1f)
                )
        },
        label = "DownloaderQualityCardTransition",
        contentKey = { it } 
    ) { targetState ->
        when (targetState) {
            
            FormatKind.Idle -> IdleQualityChoices(onPick = { pickedFormat = it }, onCancel = { downloaderViewModel.cancelButton(context) })
            
            FormatKind.Audio -> AudioQualityChoices(downloaderViewModel, onBack = { pickedFormat = FormatKind.Idle })

            FormatKind.Video -> VideoQualityChoices(downloaderViewModel, onBack = { pickedFormat = FormatKind.Idle })

        }
    }
}