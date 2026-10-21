package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.foss.fermux.R
import org.foss.fermux.components.buttons.IconTextButton
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialDownloadQueueState
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItemStatus
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SequentialDone(
    state: SequentialDownloadQueueState,
    modifier: Modifier = Modifier,
    navController: NavController,
    sequentialViewModel: SequentialViewModel
) {

    val context = LocalContext.current

    Column {
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
                        SeqFailedCard(
                            modifier = Modifier.padding(6.dp),
                            navController = navController
                        )
                    }

                    SequentialItemStatus.Cancelled -> {
                        SeqCancelledCard(
                            modifier = Modifier.padding(6.dp)
                        )
                    }

                    else -> Unit
                }
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(5.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            IconTextButton(
                modifier = Modifier.padding(5.dp),
                image = R.drawable.queue,
                text = "New Queue",
                onClick = {
                    sequentialViewModel.allDestroyer(context)
                },
            )
        }
    }
}