package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.components.downloaderComponents.SeqMetadataCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialDownloadQueueState
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItem
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
                            onCancelRequest = {
                                sequentialViewModel.singleProcessDestroyer(
                                    context = context,
                                    index = index
                                )
                            },
                            modifier = Modifier
                        )
                    }

                    else -> {
                        SequentialItemStatusCard(
                            modifier = Modifier.padding(6.dp),
                            item = item,
                            status = status,
                            onCancelRequest = {
                                sequentialViewModel.singleProcessDestroyer(
                                    context = context,
                                    index = index
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SequentialItemStatusCard(
    modifier: Modifier,
    item: SequentialItem,
    status: SequentialItemStatus,
    onCancelRequest: () -> Unit
) {
    val statusText = when (status) {
        SequentialItemStatus.Waiting -> "Waiting"
        SequentialItemStatus.Downloading -> "Downloading ${item.progress.toInt()}%"
        SequentialItemStatus.Completed -> "Completed"
        SequentialItemStatus.Failed -> item.error ?: "Download failed"
        SequentialItemStatus.Cancelled -> "Cancelled"
        SequentialItemStatus.FetchingMetadata -> "Fetching metadata"
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = FermuxColors.darkPurple,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, FermuxColors.warmPurple),
        tonalElevation = 12.dp,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.metadata?.title ?: item.url,
                    color = FermuxColors.white,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = statusText,
                    color = FermuxColors.offWhiteTextColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (status == SequentialItemStatus.Waiting ||
                status == SequentialItemStatus.Downloading
            ) {
                CancelButton(
                    modifier = Modifier.padding(start = 8.dp),
                    transparency = true,
                    onClick = onCancelRequest
                )
            }
        }
    }
}
