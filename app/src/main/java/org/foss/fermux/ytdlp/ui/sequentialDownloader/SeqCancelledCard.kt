package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.R
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SeqCancelledCard(
    modifier: Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp),
        color = FermuxColors.warmPurple,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, FermuxColors.warmPurple),
        tonalElevation = 12.dp,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(4f/3f)
            ) {
                Icon(
                    painter = painterResource(R.drawable.cancel_buttons),
                    contentDescription = null
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Queue Entry Cancelled",
                    color = FermuxColors.white,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}