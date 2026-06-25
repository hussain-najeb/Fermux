package org.foss.fermux.ytdlp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.foss.fermux.ytdlp.logic.DownloadMetadata
import org.foss.fermux.ytdlp.logic.DownloadStatus



// TODO. have the download circle be a check
//  mark when the download finished so its apparent to the user the download is done
// TODO. have the duration of the video with a black/transcperent background behind it, so it doesn't look bad




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



@SuppressLint("DefaultLocale")
fun videoTime (seconds: Int): String {

    val minutes = seconds / 60
    val second = seconds % 60
    return String.format("%02d:%02d", minutes, second)
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
            .padding(8.dp)
    ) {
        ElevatedCard(
            modifier = Modifier
                .clip(RoundedCornerShape(4.dp))
                .fillMaxWidth()
                .padding(19.dp)
                .aspectRatio(16f / 9f)
                .background(Color.Transparent)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Fetching video info"
                ) // TODO. have it in the middle of the card to the right more, and change the color, reference from the terminal tab.

                Spacer(modifier = Modifier.height(40.dp))

                LoadingIndicator()
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


        ) // https://youtu.be/ZFSN40r--zk?si=5kWFmzzw6lWT7VI5 <-- testing


        {
            Box {
                AsyncImage(
                    model = metadata.thumbnail,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,

                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .background(Color.Transparent)
                )
                progress?.let {
                    CircularWavyProgressIndicator(
                        progress = { progress / 100f },
                        color = Color(0xFF2e36aa),
                        trackColor = Color(0xff999bb5),

                        modifier = Modifier
                            .padding(8.dp)
                            .align(Alignment.BottomEnd)
                    ).also {
                        Text(
                            videoTime(metadata.duration),
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.BottomStart)
                        )

                    }
                }
            }


            // the surface to get the damn info on screen from the video metadata
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp))
                    .background(color = Color(0xFF2b2a33))

            ) {

                // self-explanatory shit. Code being the comment and shit like that
                // ( terribile idea btw).

                Column {
                    Text(
                        metadata.title, modifier = Modifier
                            .padding(8.dp)
                    )
                    metadata.uploader?.let {
                        Text(
                            it, modifier = Modifier
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
        }
    }


