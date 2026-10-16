package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.foss.fermux.components.downloaderComponents.SeqCard
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SeqFetchMetadata(
        sequentialViewModel: SequentialViewModel
) {

    val state = sequentialViewModel.state ?: return
    val context = LocalContext.current

    LazyColumn {
        items(
            items = state.items,
            key = { item -> item.id }
        ) { item ->
            val index = state.items.indexOfFirst {
                it.id == item.id
            }

            SeqCard(
                modifier = Modifier.padding(6.dp),
                title = item.metadata?.title ?: "null",
                image = item.metadata?.thumbnail.orEmpty(),
                onCancelRequest = {
                    sequentialViewModel.singleProcessDestroyer(
                        context = context,
                        index = index
                    )
                }
            ) // TODO. Most Logic work is done (about fucking time) rest is UI, should make quick work of it next day (current day if you see this tomorrow)


        }

    }
}
