package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ytdlp.logic.downloader.QuickAudioQuality
import org.foss.fermux.ytdlp.logic.downloader.QuickDownloadFormats
import org.foss.fermux.ytdlp.logic.downloader.QuickVideoQuality


@Composable
fun QuickDownloadSettingQuality(
     downloaderSettingsViewModel: DownloaderSettingsViewModel
) {
     val formatPick by downloaderSettingsViewModel.quickFormat.collectAsStateWithLifecycle()
     val spatialSpec = MaterialTheme.motionScheme

     Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.Center
     ) {
          ModularSegmentedButtons(
               optionsList = listOf(
                    QuickDownloadFormats.QuickVideo to "Video",
                    QuickDownloadFormats.QuickAudio to "Audio"
               ),
               selectedOption = formatPick,
               onOptionSelected = {
                    downloaderSettingsViewModel.setQuickFormat(it)
               }
          )

          Spacer(modifier = Modifier.height(10.dp))

          AnimatedContent(
               targetState = formatPick,
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
               contentKey = {it::class}
          ) { targetState ->
               when (targetState) {
                    QuickDownloadFormats.QuickVideo -> {
                         QuickVideo(downloaderSettingsViewModel)
                    }

                    QuickDownloadFormats.QuickAudio -> {
                         QuickAudio(downloaderSettingsViewModel)
                    }
               }
          }
     }
}

@Composable
fun QuickVideo(
     downloaderSettingsViewModel: DownloaderSettingsViewModel
) {
     val video by downloaderSettingsViewModel.quickVideo.collectAsStateWithLifecycle()
     ModularSegmentedButtons(
          optionsList = listOf(
               QuickVideoQuality.HD1080 to "FHD",
               QuickVideoQuality.HD720 to "HD",
               QuickVideoQuality.SD480 to "SD",
               QuickVideoQuality.Q360 to "360p",
               QuickVideoQuality.Q240 to "240p",
               QuickVideoQuality.Q144 to "144p"
          ),
          selectedOption = video,
          onOptionSelected = {
               downloaderSettingsViewModel.setQuickVideo(it)
          }
     )
}

@Composable
fun QuickAudio(
     downloaderSettingsViewModel: DownloaderSettingsViewModel
) {
     val audio by downloaderSettingsViewModel.quickAudio.collectAsStateWithLifecycle()
     ModularSegmentedButtons(
          optionsList = listOf(
               QuickAudioQuality.Best to "Best",
               QuickAudioQuality.High to "High",
               QuickAudioQuality.Medium to "Medium",
               QuickAudioQuality.Low to "Low"
          ),
          selectedOption = audio,
          onOptionSelected = {
               downloaderSettingsViewModel.setQuickAudio(it)
          }
     )
}