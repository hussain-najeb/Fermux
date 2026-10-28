package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.foss.fermux.R
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.Miscellaneous

@Composable
fun SeqFailedCard(
    modifier: Modifier,
    navController: NavController
) {

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { navController.navigate(Miscellaneous.DownloaderSeqLogs.route) }
            .height(100.dp),
        color = FermuxColors.darkPurple,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, FermuxColors.fermuxLightErrorTextColor),
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
                        painter = painterResource(R.drawable.error_sign),
                        contentDescription = null,
                        modifier = Modifier
                            .size(60.dp),
                        tint = FermuxColors.fermuxTextError
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Download Failed, Press to view logs",
                    color = FermuxColors.fermuxLightErrorTextColor,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF102f60)
@Composable
fun test22() {
    val navController = rememberNavController()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SeqFailedCard(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            navController = navController
        )
    }
}