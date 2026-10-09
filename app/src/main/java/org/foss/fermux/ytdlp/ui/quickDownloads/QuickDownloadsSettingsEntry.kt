package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.runtime.Composable
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel


@Composable
fun QuickDownloadSettingQuality(
     downloaderSettingsViewModel: DownloaderSettingsViewModel
) {

     val formatPick by downloaderSettingsViewModel.


     ModularSegmentedButtons(
          optionsList = listOf(),
          selectedOption = ,
          onOptionSelected = {

          }
     )



}