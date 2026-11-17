package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

@Composable
fun SeqWaitCard(
    modifier: Modifier,
    status: SequentialItemStatus,
    item: SequentialItem,
    onCancelRequest: () -> Unit
) {

    val statusText = when (status) {
        SequentialItemStatus.Waiting -> "Waiting"
        SequentialItemStatus.Cancelled -> "Cancelled"
        SequentialItemStatus.Failed -> item.error ?: "Download Failed"
        else -> null
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
                shape = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp),
                color = FermuxColors.fermuxSurface
            ) {
                AsyncImage(
                    model = item.metadata?.thumbnail,
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(4f / 3f),
                    placeholder = painterResource(R.drawable.file_image),
                    fallback = painterResource(R.drawable.clock_pause),
                    contentScale = ContentScale.None,
                    clipToBounds = true,
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            text = item.metadata?.title ?: item.url,
                            color = FermuxColors.white,
                            maxLines = 2,
                            fontSize = 16.sp,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    if (statusText != null) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(12.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Text(
                                text = statusText,
                                color = FermuxColors.offWhiteTextColor,
                                fontSize = 15.sp,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(3.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                CancelButton(
                    transparency = true,
                    onClick = onCancelRequest
                )
            }
        }
    }
}
