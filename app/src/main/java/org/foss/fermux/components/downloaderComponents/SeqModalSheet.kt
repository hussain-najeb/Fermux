package org.foss.fermux.components.downloaderComponents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SeqFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SequentialModalSheet(
     onDismissRequest: () -> Unit,
     downloaderSettingsViewModel: DownloaderSettingsViewModel
) {
     val sheetState = rememberBottomSheetState(
          initialValue = SheetValue.Hidden,
          enabledValues = setOf(
               SheetValue.Hidden,
               SheetValue.Expanded
          )
     )

     val format = downloaderSettingsViewModel.seqFormat.collectAsStateWithLifecycle()


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
     ) {
          Column {
               Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
               ) {
                    Text(
                         text = "Bulk Configuration",
                         fontSize = 15.sp,
                         modifier = Modifier.padding(5.dp)
                    )
               }

               Box(modifier = Modifier.fillMaxWidth()) {
                    Text(
                         text = "Configure in bulk. Select format, resolution, and audio quality you want for the entire queue",
                         fontSize = 15.sp,
                         modifier = Modifier.padding(5.dp)
                    )
               }

               ModularSegmentedButtons(
                    optionsList = listOf(
                         SeqFormat.SeqVideo to "Video",
                         SeqFormat.SeqAudio to "Audio"
                    ),
                    selectedOption = ,
                    onSelected = sequentialViewModel.
               )

          }
     }
}