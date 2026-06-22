    package org.foss.fermux.ytdlp.ui

    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.outlined.Cancel
    import androidx.compose.material.icons.outlined.Download
    import androidx.compose.material3.BottomSheetDefaults
    import androidx.compose.material3.Button
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.IconButton
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
    fun QualitySheet (showSheet: Boolean, onDismiss: () -> Unit,
                       onConfirm: (AudioQuality?, DownloadQuality?) -> Unit) {
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
                var selectedPageIndex by remember { mutableIntStateOf(0) } // to get the buttons an index to work by
                val options = listOf("Audio", "Video") // the names of the chosen options. 0 is audio, 1 is video


                Column(  // the default column so user can choose the audio and the video thing
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    Icon(imageVector =  Icons.Outlined.Download, contentDescription = null, // the download icon in the middle of the thing
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(50.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Download format:")

                    Spacer(modifier = Modifier.height(2.dp))

                    SingleChoiceSegmentedButtonRow { // the button row that looks like a pill or whatever
                        options.forEachIndexed { index, label ->
                            SegmentedButton( // the button thing I think
                                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size), // to get the rounded shape
                                // from both sizes I think, so it doesn't look like shit
                                onClick = { selectedPageIndex = index
                                    when (index){
                                        0 -> page = 2 // Audio
                                        1 -> page = 3 // Video
                                    }
                                },
                                selected = index == selectedPageIndex,
                                label = { Text(label) },
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))


                when (page) {
                    2 -> {
                        var selectedAudioIndex by remember { mutableIntStateOf(0) }
                        val audioOptions = listOf("Best", "High", "Medium")

                        Column(modifier = Modifier.fillMaxSize())

                        {

                            Text(text = "Audio Quality:")

                            Spacer(modifier = Modifier.height(2.dp))

                            SingleChoiceSegmentedButtonRow {
                                audioOptions.forEachIndexed {
                                        index, label ->

                                    SegmentedButton(
                                        shape = SegmentedButtonDefaults.itemShape(index = index, count = audioOptions.size),
                                        onClick = {selectedAudioIndex = index

                                            when (index)
                                            {
                                                0 -> pickedAudio = AudioQuality.BEST
                                                1 -> pickedAudio = AudioQuality.HIGH
                                                2 -> pickedAudio = AudioQuality.MEDIUM
                                            }
                                        },
                                        selected = index == selectedAudioIndex,
                                        label = { Text(label) },
                                    )
                                }
                            }
                        }
                    }

                    3 -> {

                        val videoOptions = listOf("Best", "1080p", "720p", "480p", "360p", "240p")
                        var selectedVideoIndex by remember { mutableIntStateOf(0) }

                        Column(modifier = Modifier.fillMaxSize()) {

                            Text(text = "Video Quality:")

                            Spacer(modifier = Modifier.height(2.dp))

                            SingleChoiceSegmentedButtonRow {
                                videoOptions.forEachIndexed {
                                    index, label ->

                                    SegmentedButton(
                                        shape = SegmentedButtonDefaults.itemShape(index = index, count = videoOptions.size),
                                        onClick = { selectedVideoIndex = index

                                            when (index)
                                            {
                                                0 -> pickedVideo = DownloadQuality.BEST
                                                1 -> pickedVideo = DownloadQuality.HD1080
                                                2 -> pickedVideo = DownloadQuality.HD720
                                                3 -> pickedVideo = DownloadQuality.SD480
                                                4 -> pickedVideo = DownloadQuality.Q360
                                                5 -> pickedVideo = DownloadQuality.Q240
                                            }
                                        },
                                        selected = index == selectedVideoIndex,
                                        label = { Text(label) },
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(5.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth())
                {

                    Button(
                        shape = RoundedCornerShape(4.dp),
                        onClick = { onConfirm( pickedAudio, pickedVideo )
                                  onDismiss()
                                  },
                    ) {
                        Icon(imageVector = Icons.Outlined.Download, contentDescription = null)
                        Text(text = "Download")
                    }

                    Button(
                        shape = RoundedCornerShape(4.dp),
                        onClick = {
                        onDismiss()
                    }) {
                        Icon(imageVector = Icons.Outlined.Cancel, contentDescription = null,)
                        Text(text = "Cancel")
                    }
                }
            }
        }
    }