package org.foss.fermux.ytdlp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.foss.fermux.ytdlp.logic.AudioQuality
import org.foss.fermux.ytdlp.logic.DownloadQuality


@OptIn(ExperimentalMaterial3Api::class)

/**
 * @param showSheet is just a boolean to pop the sheet up the screen, it's off by default
 * @param onConfirm is also for calling the thing, the enum classes at
 * @see [AudioQuality]
 * @see [DownloadQuality]
 */

@Composable
fun QualitySheets (showSheet: Boolean, onDismiss: () -> Unit,
                   onConfirm: (AudioQuality?, DownloadQuality?) -> Unit)

{

    var pickedAudio by remember { mutableStateOf<AudioQuality?>(null) } // for the string to pass to
    // the execute function in ytdlp
    var pickedVideo by remember { mutableStateOf<DownloadQuality?>(null) } // same here for videos




    if (showSheet) {
        val sheetState = rememberModalBottomSheetState() // for keeping the sheet on screen
        var page by remember { mutableIntStateOf(1) } // for selecting each tab in the sheet, so if video was
        // selected it will go to 3 and so on


        ModalBottomSheet( // the composable that does the thing
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle(modifier = Modifier.background(Color.Cyan)) },
            modifier = Modifier
                .fillMaxWidth(.70f)
        ) {
            var selectedIndex by remember { mutableIntStateOf(0) } // to get the buttons an index to work by
            val options = listOf("Audio", "Video") // the names of the chosen options. 0 is audio, 1 is video
            when (page) {
                1 -> {
                    Column(  // column for the first page
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        Icon(imageVector =  Icons.Outlined.Download, contentDescription = null, // the download icon in the middle of the thing
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                            .size(50.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        SingleChoiceSegmentedButtonRow { // the button row that looks like a pill or whatever
                            options.forEachIndexed { index, label ->
                                SegmentedButton( // the button thing i think
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size), // to get the rounded shape
                                    // from both sizes
                                    onClick = { selectedIndex = index
                                        when (index){
                                            0 -> page = 2 // Audio
                                            1 -> page = 3 // Video
                                        }
                                              },
                                    selected = index == selectedIndex ,
                                    label = { Text(label) },
                                )
                            }
                        }

                    }

                }
                2 -> {}
            }
        }
    }
}


