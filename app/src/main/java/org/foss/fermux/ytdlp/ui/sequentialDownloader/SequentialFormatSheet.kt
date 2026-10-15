package org.foss.fermux.ytdlp.ui.sequentialDownloader

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
      SeqVideoQuality.HD1080 to "1080p",
      SeqVideoQuality.HD720 to "720p",
      SeqVideoQuality.SD480 to "480p",
      SeqVideoQuality.Q360 to "360p",
      SeqVideoQuality.Q240 to "240p",
      SeqVideoQuality.Q144 to "144p"
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