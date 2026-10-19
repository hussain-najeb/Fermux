package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.foss.fermux.components.downloaderComponents.SeqMetadataCard
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialDownloadQueueState
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItemStatus
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SequentialQueue(
    state: SequentialDownloadQueueState,
    sequentialViewModel: SequentialViewModel
) {
    val context = LocalContext.current

    LazyColumn {
        itemsIndexed(
            items = state.items,
            key = { _, item -> item.id }
        ) { index, item ->
            AnimatedContent(
                targetState = item.status,
                label = "SequentialItemTransition"
            ) { status ->
                when (status) {
                    SequentialItemStatus.FetchingMetadata -> {
                        SeqMetadataCard(
                            modifier = Modifier.padding(6.dp),
                            onCancelRequest = {
                                sequentialViewModel.singleProcessDestroyer(
                                    context = context,
                                    index = index
                                )
                            }
                        )
                    }
                    SequentialItemStatus.Waiting -> {
                        SeqWaitCard(
                            modifier = Modifier.padding(6.dp),
                            item = item,
                            onCancelRequest = {
                                sequentialViewModel.singleProcessDestroyer(
                                    context = context,
                                    index = index
                                )
                            },
                            status = status,
                        )
                    }

                    SequentialItemStatus.Downloading -> {
                        SeqDownloadCard(
                            modifier = Modifier.padding(6.dp),
                            status = status,
                            item = item,
                            onCancelRequest = {
                                sequentialViewModel.singleProcessDestroyer(
                                    context = context,
                                    index = index
                                )
                            }
                        )
                    }

                    SequentialItemStatus.Completed -> {
                        SeqCompletedCard(
                            item = item,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                    SequentialItemStatus.Failed -> {

                    }
                    SequentialItemStatus.Cancelled -> {

                    }
                }
            }
        }
    }
}
