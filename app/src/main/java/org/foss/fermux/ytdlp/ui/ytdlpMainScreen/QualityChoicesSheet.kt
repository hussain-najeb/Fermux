package org.foss.fermux.ytdlp.ui.ytdlpMainScreen

    import androidx.compose.foundation.background
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.foundation.layout.Row
    import androidx.compose.foundation.layout.Spacer
    import androidx.compose.foundation.layout.fillMaxWidth
    import androidx.compose.foundation.layout.height
    import androidx.compose.foundation.layout.padding
    import androidx.compose.foundation.layout.size
    import androidx.compose.foundation.layout.width
    import androidx.compose.foundation.shape.RoundedCornerShape
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.outlined.Cancel
    import androidx.compose.material.icons.outlined.Done
    import androidx.compose.material.icons.outlined.Download
    import androidx.compose.material3.BottomSheetDefaults
    import androidx.compose.material3.Button
    import androidx.compose.material3.ExperimentalMaterial3Api
    import androidx.compose.material3.Icon
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.ModalBottomSheet
    import androidx.compose.material3.OutlinedButton
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
    import androidx.compose.ui.text.font.FontFamily
    import androidx.compose.ui.unit.dp
    import org.foss.fermux.ytdlp.logic.downloader.AudioQuality
    import org.foss.fermux.ytdlp.logic.downloader.VideoQuality


    @OptIn(ExperimentalMaterial3Api::class)

    /**
     * @param showSheet is just a boolean to pop the sheet up the screen, it's off by default
     * @param onConfirm is also for calling the thing, the enum classes at
     * @see [AudioQuality]
     * @see [VideoQuality]
     */

    @Composable
    fun QualitySheet (showSheet: Boolean, onDismiss: () -> Unit,
                       onConfirm: (AudioQuality?, VideoQuality?) -> Unit) {
        var pickedAudio by remember { mutableStateOf<AudioQuality?>(null) } // for the string to pass to
        // the execute function in ytdlp
        var pickedVideo by remember { mutableStateOf<VideoQuality?>(null) } // same here for videos

        if (showSheet) {
            val sheetState = rememberModalBottomSheetState() // for keeping the sheet on screen
            var page by remember { mutableIntStateOf(1) } // for selecting each tab in the sheet, so if video was
            // selected it will go to 3 and so on

            ModalBottomSheet(
                // the composable that does the thing
                onDismissRequest = onDismiss,
                sheetState = sheetState,
                dragHandle = { BottomSheetDefaults.DragHandle() },
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth(1f),
            ) {
                var selectedPageIndex by remember { mutableIntStateOf(0) } // to get the buttons an index to work by
                val options = listOf("Audio", "Video") // the names of the chosen options. 0 is audio, 1 is video


                Column(  // the default column so user can choose the audio and the video thing
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                ) {

                    Spacer(modifier = Modifier.height(7.dp))

                    Icon(
                        imageVector = Icons.Outlined.Download,
                        contentDescription = null, // the download icon in the middle of the thing
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .size(50.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "Before Downloading....",
                        style = MaterialTheme.typography.headlineMediumEmphasized,
                        fontFamily = FontFamily.Default,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        text = "Download format:",
                        fontFamily = FontFamily.Default,
                        modifier = Modifier.padding(start = 19.dp)
                    )

                    Spacer(modifier = Modifier.height(13.dp))

                    SingleChoiceSegmentedButtonRow(modifier = Modifier.align(Alignment.CenterHorizontally)) { // the button row that looks like a pill or whatever
                        options.forEachIndexed { index, label ->
                            SegmentedButton(
                                // the button thing I think
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = options.size
                                ), // to get the rounded shape
                                // from both sizes I think, so it doesn't look like shit
                                onClick = {
                                    selectedPageIndex = index
                                    when (index) {
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

                Spacer(modifier = Modifier.height(15.dp))


                when (page) {
                    2 -> {
                        var selectedAudioIndex by remember { mutableIntStateOf(0) }
                        val audioOptions = listOf("Best", "High", "Medium")

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                        )

                        {

                            Spacer(modifier = Modifier.height(15.dp))

                            Text(text = "Audio Quality:", fontFamily = FontFamily.Monospace)

                            Spacer(modifier = Modifier.height(4.dp))

                            SingleChoiceSegmentedButtonRow(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                audioOptions.forEachIndexed { index, label ->

                                    SegmentedButton(
                                        shape = SegmentedButtonDefaults.itemShape(
                                            index = index,
                                            count = audioOptions.size
                                        ),
                                        onClick = {
                                            selectedAudioIndex = index

                                            when (index) {
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

                        val videoOptions = listOf("Best", "1080p", "720p", "480p", "360p")
                        var selectedVideoIndex by remember { mutableIntStateOf(0) }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            Spacer(modifier = Modifier.height(15.dp))

                            Text(text = "Video Quality:", fontFamily = FontFamily.Monospace)

                            Spacer(modifier = Modifier.height(4.dp))

                            SingleChoiceSegmentedButtonRow(
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                videoOptions.forEachIndexed { index, label ->

                                    SegmentedButton(
                                        shape = SegmentedButtonDefaults.itemShape(
                                            index = index,
                                            count = videoOptions.size
                                        ),
                                        onClick = {
                                            selectedVideoIndex = index

                                            when (index) {
                                                0 -> pickedVideo = VideoQuality.BEST
                                                1 -> pickedVideo = VideoQuality.HD1080
                                                2 -> pickedVideo = VideoQuality.HD720
                                                3 -> pickedVideo = VideoQuality.SD480
                                                4 -> pickedVideo = VideoQuality.Q360
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

                Spacer(modifier = Modifier.height(15.dp))

                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth())
                {

                    OutlinedButton(
                        shape = RoundedCornerShape(24.dp),
                        onClick = {
                            onDismiss()
                        }) {
                        Icon(imageVector = Icons.Outlined.Cancel, contentDescription = null)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "Cancel")
                    }

                    Spacer(modifier = Modifier.width(7.dp))

                    Button(
                        shape = RoundedCornerShape(24.dp),
                        onClick = {
                            onConfirm(pickedAudio, pickedVideo)
                            onDismiss()
                        },
                    ) {
                        Icon(imageVector = Icons.Outlined.Done, contentDescription = null)
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(text = "Download")
                    }

                }
            }
        }
    }


