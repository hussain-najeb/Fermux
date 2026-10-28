package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.R
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.components.buttons.IconTextButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItem
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel


@Composable
fun SeqDownloadCard(
    modifier: Modifier,
    item: SequentialItem,
    sequentialViewModel: SequentialViewModel,
onCancelRequest: () -> Unit
) {

    val context = LocalContext.current

    val rotationDuration = rememberInfiniteTransition()

    val rotation by rotationDuration.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5500,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                    shape = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp),
                    color = FermuxColors.mutedBlue
                ) {
                    Box(
                        modifier = Modifier
                            .background(FermuxColors.darkPurple)
                    ) {
                        AsyncImage(
                            model = item.metadata?.thumbnail,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            clipToBounds = true,
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(40.dp)
                                .background(
                                    color = FermuxColors.warmPurple.copy(alpha = 0.70f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.loading_circle),
                                contentDescription = null,
                                modifier = Modifier
                                    .graphicsLayer {
                                        rotationZ = rotation
                                    }
                                    .padding(5.dp)
                                    .align(Alignment.Center),
                                tint = FermuxColors.white
                            )
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
                        Text(
                            text = "${item.progress.toInt()}% Downloaded",
                            color = FermuxColors.offWhiteTextColor,
                            fontSize = 13.sp,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 22.dp)
                        )

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
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.BottomEnd
        ) {
            IconTextButton(
                modifier = Modifier.padding(5.dp),
                image = R.drawable.cancel_queue,
                text = "Cancel Queue",
                onClick = {
                    sequentialViewModel.allDestroyer(context)
                }
            )
        }
    }
}
