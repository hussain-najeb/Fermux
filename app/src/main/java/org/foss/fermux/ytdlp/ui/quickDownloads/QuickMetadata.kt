package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.foss.fermux.ui.theme.FermuxColors
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun QuickDownloadsMetadata(
    progress: Float? = null
) {

    val messages = remember {
        listOf(
            "Fetching video info",
            "Fetching video info.",
            "Fetching video info..",
            "Fetching video info..."
        )
    }
    var loadingMessage by remember { mutableStateOf(messages.first()) }
    LaunchedEffect(Unit) {
        while (true) {
            for (message in messages) {
                loadingMessage = message
                delay(300.milliseconds)
            }
        }
    }

    Surface(
        modifier = Modifier
            .padding(8.dp)
            .height(80.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = FermuxColors.darkPurple,
        border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder)
    ) {
        Box(
            modifier = Modifier.padding(12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = loadingMessage,
                fontSize = 18.sp,
                fontStyle = FontStyle.Normal,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Start,
                color = FermuxColors.white
            )
        }

        Box(
            modifier = Modifier
                .padding(end = 15.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            if (progress != null) {
                if (progress > -1f)
                    LoadingIndicator(
                        modifier = Modifier.size(45.dp),
                        color = FermuxColors.skyBlue
                    )
            }
        }
    }
}

@Preview
@Composable
fun test7() {
    QuickDownloadsMetadata(progress = 4f)
}