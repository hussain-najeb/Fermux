package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.layout.*
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
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.Miscellaneous
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialDownloadQueueState
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItemStatus
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SequentialDone(
    state: SequentialDownloadQueueState,
    modifier: Modifier = Modifier,
    sequentialViewModel: SequentialViewModel,
    navController: NavController
) {

    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = modifier.weight(1f)) {
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
                            onClick = {
                                sequentialViewModel.selectedLogItemId = item.id
                                navController.navigate(Miscellaneous.DownloaderSeqLogs.route)
                            }
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
                .fillMaxWidth()
                .padding(5.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            IconTextButton(
                modifier = Modifier.padding(5.dp),
                image = R.drawable.queue,
                activeColor = FermuxColors.fermuxErrorCardColor,
                activeIconColor = FermuxColors.white,
                text = "New Queue",
                onClick = {
                    sequentialViewModel.allDestroyer(context)
                },
            )
        }
    }
}