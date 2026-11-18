package org.foss.fermux.ytdlp.ui.sequentialDownloader.stateMachine

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SeqAudioQuality
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SeqVideoQuality

@Composable
fun SeqVideoButtons(downloaderSettingsViewModel: DownloaderSettingsViewModel) {
    val video by downloaderSettingsViewModel.seqVideo.collectAsStateWithLifecycle()
    ModularSegmentedButtons(
        optionsList = listOf(
            SeqVideoQuality.HD1080 to "1080",
            SeqVideoQuality.HD720 to "720",
            SeqVideoQuality.SD480 to "480",
            SeqVideoQuality.Q360 to "360",
            SeqVideoQuality.Q240 to "240",
            SeqVideoQuality.Q144 to "144"
        ),
        selectedOption = video,
        onSelected = downloaderSettingsViewModel::setSeqVideo
    )
}

@Composable
fun SeqAudioButtons(downloaderSettingsViewModel: DownloaderSettingsViewModel) {
    val audio by downloaderSettingsViewModel.seqAudio.collectAsStateWithLifecycle()
    ModularSegmentedButtons(
        optionsList = listOf(
            SeqAudioQuality.Best to "Best",
            SeqAudioQuality.High to "High",
            SeqAudioQuality.Medium to "Medium",
            SeqAudioQuality.Low to "Low"
        ),
        selectedOption = audio,
        onSelected = downloaderSettingsViewModel::setSeqAudio
    )
}