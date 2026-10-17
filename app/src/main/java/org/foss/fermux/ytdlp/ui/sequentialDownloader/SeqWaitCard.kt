package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.R
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItem
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItemStatus
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SeqCard(
    sequentialViewModel: SequentialViewModel,
    modifier: Modifier,
    status: SequentialItemStatus,
    item: SequentialItem,
    onCancelRequest: () -> Unit
) {

    val state = sequentialViewModel.state

    val statusText = when (status) { // TODO. Should probibly switch this for a loading bar
        SequentialItemStatus.FetchingMetadata -> "Fetching Metadata"
        SequentialItemStatus.Waiting -> "Waiting"
        SequentialItemStatus.Downloading -> "Downloading ${item.progress.toInt()}%"
        SequentialItemStatus.Completed -> "Download Completed"
        SequentialItemStatus.Failed -> item.error ?: "Download Failed"
        SequentialItemStatus.Cancelled -> "Cancelled"
    }

    val statusBall = when (status) {
        SequentialItemStatus.FetchingMetadata -> {

        }
        SequentialItemStatus.Waiting -> {

        }
        SequentialItemStatus.Downloading -> {

        }
        SequentialItemStatus.Completed -> {

        }
        SequentialItemStatus.Cancelled -> {

        }
        SequentialItemStatus.Failed -> {

        }
    } // TODO. Add these, its a small ball that has light for each state, like, if the video is in waiting,
    //    the ball is gray, if its downloading, its blue, if it's done, its green, error is red, and have it glow,
    //    like maybe an animation circle, so it goes, bright blue glow, then goes to the same color, then goes bright,
    //    like a breathing motion, an animation circle that is for all balls btw.

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        color = FermuxColors.darkPurple,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, FermuxColors.warmPurple),
        tonalElevation = 12.dp,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .aspectRatio(4f / 3f)
                    .align(Alignment.CenterVertically),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                color = FermuxColors.mutedBlue
            ) {
                AsyncImage(
                    model = state.currentItem?.metadata?.thumbnail,
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(4f / 3f),
                    placeholder = painterResource(R.drawable.file_image),
                    fallback = painterResource(R.drawable.error_sign), // TODO. Edit the size of this icon, its currently Massive
                    contentScale = ContentScale.Crop,
                    clipToBounds = true,
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp, start = 5.dp)
                    .fillMaxHeight()
            ) {
                Column {
                    Text(
                        text = item.metadata?.title ?: item.url,
                        color = FermuxColors.white,
                        maxLines = 2,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(2.dp)
                    )
                    Text(
                        text = statusText,
                        color = FermuxColors.offWhiteTextColor,
                        fontSize = 15.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 22.dp)
                    )

                    Box(
                        modifier = Modifier.padding(),
                        contentAlignment = Alignment.Center
                    ) {
                            LinearProgressIndicator(
                                progress = { (item.progress / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 8.dp, end = 65.dp),
                                color = FermuxColors.deepBlue,
                                trackColor = FermuxColors.white,
                                strokeCap = StrokeCap.Round
                            )
                    }
                }
                CancelButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 1.dp, top = 5.dp),
                    transparency = true,
                    onClick = onCancelRequest
                )
            }
        }
    }
}