package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialDownloadQueueState
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItemStatus

@Composable
fun SequentialDone(
    state: SequentialDownloadQueueState,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(
            items = state.items,
            key = { it.id }
        ) { item ->
            when (item.status) {
                SequentialItemStatus.Completed -> {
                    SeqCompletedCard(
                        item = item,
                        modifier = Modifier.padding(6.dp)
                    )
                }

                SequentialItemStatus.Failed -> {
                    SeqFailedCard()
                }

                SequentialItemStatus.Cancelled -> {
                    SeqCancelledCard()
                }

                else -> Unit
            }
        }
    }
}