package org.foss.fermux.ytdlp.ui.sequentialDownloader

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItem

@Composable
fun SeqCompletedCard(
    modifier: Modifier = Modifier,
    item: SequentialItem
) {
    Surface(
        modifier = modifier.fillMaxWidth().height(100.dp),
        color = FermuxColors.darkPurple,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, FermuxColors.warmPurple),
        tonalElevation = 12.dp,
        shadowElevation = 12.dp
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Surface(
                modifier = Modifier
                    .aspectRatio(4f / 3f)
                    .align(Alignment.CenterVertically),
                shape = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp),
                color = FermuxColors.mutedBlue
            ) {
                Box(modifier = Modifier.background(FermuxColors.fermuxSurface)) {
                    AsyncImage(
                        model = item.metadata?.thumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        clipToBounds = true,
                    )
                    Box(
                        modifier = Modifier.align(Alignment.Center).size(50.dp).background(
                            color = FermuxColors.warmPurple.copy(alpha = 0.70f),
                            shape = RoundedCornerShape(8.dp)
                        )
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Download Complete",
                            modifier = Modifier.fillMaxSize().padding(10.dp).align(Alignment.Center),
                            tint = FermuxColors.white
                        )
                    }
                }
            }
            Column {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    Text(
                        text = "dw'lkmdaonmdpiauwndaiunwdop[aiknd",
                        //  text = item.metadata?.title ?: item.url,
                        color = FermuxColors.white,
                        maxLines = 2,
                        fontSize = 16.sp,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(12.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Text(
                        text = "Download Done",
                        color = FermuxColors.offWhiteTextColor,
                        fontSize = 13.sp,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}