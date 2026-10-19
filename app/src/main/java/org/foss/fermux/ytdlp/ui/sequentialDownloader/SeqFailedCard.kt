package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.Miscellaneous

@Composable
fun SeqFailedCard(
    modifier: Modifier,
    navController: NavController
) {

    Surface(
        modifier = modifier
            .clickable {
                navController.navigate(Miscellaneous.DownloaderSeqLogs.route)
            }
            .fillMaxWidth()
            .height(100.dp),
        color = FermuxColors.fermuxRedDeleteColorInActive,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, FermuxColors.warmPurple),
        tonalElevation = 12.dp,
        shadowElevation = 12.dp
    ) {
        Row {
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Error, Click To View Logs",
                    color = FermuxColors.white,
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}