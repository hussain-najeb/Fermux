package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
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
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates.ProgressState


@Composable
fun SeqDownloadCard(
    modifier: Modifier,
    status: SequentialItemStatus,
    item: SequentialItem,
    onCancelRequest: () -> Unit
) {


    val statusText = when (status) {
        SequentialItemStatus.Downloading -> "${item.progress.toInt()}% Downloaded"
        SequentialItemStatus.Completed -> "Download Complete"
        else -> null
    }

    val rotationDuration = rememberInfiniteTransition()

    val rotation by rotationDuration.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val downloadState = item.progress.let {
        if (it >= 100f) ProgressState.Done else ProgressState.InProgress
    }


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
                Box(
                    modifier = Modifier
                        .background(FermuxColors.fermuxSurface)
                ) {
                    AsyncImage(
                        model = item.metadata?.thumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        clipToBounds = true,
                    )
                    when (downloadState) {
                        ProgressState.InProgress -> Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(50.dp)
                                .background(
                                    color = FermuxColors.warmPurple.copy(alpha = 0.70f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.loading_circle),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .graphicsLayer {
                                        rotationZ = rotation
                                    }
                                    .padding(10.dp)
                                    .align(Alignment.CenterHorizontally),
                                tint = FermuxColors.white
                            )
                        }

                        ProgressState.Done -> Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(50.dp)
                                .background(
                                    color = FermuxColors.warmPurple.copy(alpha = 0.70f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Download Complete",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp)
                                    .align(Alignment.CenterHorizontally),
                                tint = FermuxColors.white
                            )
                        }
                    }
                }
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
                    if (statusText != null) {
                        Text(
                            text = statusText,
                            color = FermuxColors.offWhiteTextColor,
                            fontSize = 13.sp,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 22.dp)
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