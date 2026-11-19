package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.foss.fermux.R
import org.foss.fermux.components.buttons.StandardButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.Miscellaneous

@Composable
fun QuickdownloadError(
    onCancel: () -> Unit,
    navController: NavController
) {

    Surface(
        modifier = Modifier
            .padding(8.dp)
            .height(80.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = FermuxColors.darkPurple,
        border = BorderStroke(1.dp, FermuxColors.fermuxLightErrorTextColor)
    ) {
        Box(
            modifier = Modifier
                .padding(6.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Error while downloading",
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Start,
                color = FermuxColors.fermuxTextError,
                modifier = Modifier.padding(start = 2.dp)
            )
        }
        Box(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Row(
                modifier = Modifier.padding(5.dp),
                horizontalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                StandardButton(
                    onClick = { navController.navigate(Miscellaneous.DownloaderLogs.route) },
                    image = R.drawable.log
                )
                StandardButton(
                    modifier = Modifier.padding(end = 5.dp),
                    image = R.drawable.close,
                    activeColor = FermuxColors.fermuxErrorCardColor,
                    activeIconColor = FermuxColors.white,
                    onClick = { onCancel() }
                )
            }
        }
    }
}