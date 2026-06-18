package org.foss.fermux.ytdlp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.foss.fermux.ytdlp.logic.DownloadMetadata
import org.foss.fermux.ytdlp.logic.DownloadStatus
import org.foss.fermux.ytdlp.logic.videoTime

@Composable
fun WhenCards (state: DownloadStatus) {
    when (state) {
        is DownloadStatus.Idle -> {} // Idle state of the card

        is DownloadStatus.Loading -> {
            LoadingCard()
        } // while downloading the info to the card

        is DownloadStatus.Downloading -> {
            LoadedCard(state.metadata, state.downloadProgress)
        } // just to get a damn bar to show the progress.

        is DownloadStatus.Loaded -> {
            LoadedCard(state.metadata)
        }
        // the card gets loaded to view the damn
        // content when you call state. state here is assigned to "metadata", then to the actual card
        // composable later gets to be assgined to "DownloadMetadata" to fill out the
        // info in that data class.

        is DownloadStatus.Error -> { Text(
            state.errorMessage)
        } // if god forbids, an error happens, its seen here.
    }
}


//Box(modifier = Modifier
//.fillMaxWidth()
//.padding(8.dp),
//contentAlignment = Alignment.Center
//) {
//    LoadingIndicator()
//}

@Composable
fun LoadingCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clip(RoundedCornerShape(4.dp)),

            ) {
            Row {
                LoadingIndicator()
                Spacer(modifier = Modifier.width(9.dp))
                Text(text = "fetching video info") // TODO have it in the middle of the screen, and change the color, reference from the terminal tab.
            }
        }

    }
}
@Composable

fun LoadedCard (metadata: DownloadMetadata, progress: Float? = null) {   // for the fully Loaded card to view on screen

    Column( // a column to have both the surface and the card be in the same page
        modifier = Modifier
            .fillMaxWidth()
    ) {
        ElevatedCard( // the video card with the picture of the downloaded video
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
            Box(contentAlignment = Alignment.BottomCenter) {

                progress?.let {
                    LinearWavyProgressIndicator(
                        progress = { progress / 100f },
                        color = Color(0xff999bb5),
                        trackColor = Color(0xFF2e36aa),

                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }


            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // the surface to get the damn info on screen from the video metadata

        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .background(color = Color(0xFF2b2a33))

        ) {

            // self-explanatory shit. Code being the comment and shit like that
            // ( terribile idea btw).

            Column {
                Text(metadata.title, modifier = Modifier
                    .padding(8.dp)
                )
                Text(videoTime(metadata.duration),modifier = Modifier
                    .padding(8.dp)
                )
                metadata.uploader?.let { Text(it, modifier = Modifier
                    .padding(8.dp)) }
            }
        }
    }
}


