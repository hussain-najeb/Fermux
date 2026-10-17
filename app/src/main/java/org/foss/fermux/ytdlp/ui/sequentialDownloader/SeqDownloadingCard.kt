package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItemStatus
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SeqDownloadingCard(
    modifier: Modifier,
    sequentialViewModel: SequentialViewModel,
    onCancelRequest: () -> Unit,
    item: SequentialItemStatus
) {



}