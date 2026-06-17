package org.foss.fermux.ytdlp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.foss.fermux.ytdlp.logic.DownloadMetadata
import org.foss.fermux.ytdlp.logic.DownloadStatus
import org.foss.fermux.ytdlp.logic.VideoTime

@Composable
fun WhenCards (state: DownloadStatus) {
    when (state) {
        is DownloadStatus.Idle -> {} // Idle state of the card

        is DownloadStatus.Loading -> {
            LoadingCard()
        } // while downloading the info to the card

        is DownloadStatus.Loaded -> {
            LoadedCard(state.metadata)
        }
        // the card gets loaded to view the damn
        // content when you call state. state here is assigned to "metadata", then to the actual card
        // composable later gets to be assgined to "DownloadMetadata" to fill out the
        // info in that data class.


        is DownloadStatus.Downloading -> {
            ProgressIndicatorBar()
        } // just to get a damn bar to show the progress.



        is DownloadStatus.Error -> { Text(
            state.errorMessage)
        } // if god forbids, an error happens, its seen here.
    }
}

@Composable
fun ProgressIndicatorBar () {}

@Composable
fun LoadingCard() {




}


@Composable
fun LoadedCard (metadata: DownloadMetadata) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ElevatedCard(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .fillMaxWidth()
                .padding(19.dp)
        )
        {
            AsyncImage(
                model = metadata.thumbnail,
                contentDescription = null,

                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Gray)
            )
        }
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(color = Color(0xFF2b2a33))
        ) {
            Column {
                Text(metadata.title, modifier = Modifier
                    .background(Color.White)
                    .padding(8.dp)
                )
                Text(VideoTime(metadata.duration),modifier = Modifier
                    .padding(8.dp)
                    .background(Color.White)
                )
            }
        }
    }
}


