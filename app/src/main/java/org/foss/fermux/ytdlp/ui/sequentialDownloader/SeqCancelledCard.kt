package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        color = FermuxColors.mutedBlue,
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
                    modifier = Modifier.background(FermuxColors.fermuxSurface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.close),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp),
                        tint = FermuxColors.white
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Downloader process canceled",
                    color = FermuxColors.white,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF102f60)
@Composable
fun test21() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SeqCancelledCard(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
        )
    }
}