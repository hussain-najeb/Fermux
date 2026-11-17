package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.components.buttons.IconTextButton
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun QuickDownloading(
    progress: Float? = null,
    onCancel: () -> Unit,
) {
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
            modifier = Modifier.padding(top = 19.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            progress?.let { value ->
                LinearProgressIndicator(
                    progress = { (value / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    color = FermuxColors.deepBlue,
                    trackColor = FermuxColors.white,
                    strokeCap = StrokeCap.Round
                )
            }
        }

        Box(
            modifier = Modifier.padding(7.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            IconTextButton(
                text = "Cancel",
                image = R.drawable.cancel_buttons,
                modifier = Modifier.wrapContentSize(),
                onClick = { onCancel() }
            )
        }
    }
}

@Preview(backgroundColor = 0xFF181825, showBackground = true)
@Composable
fun test8() {
    Column(modifier = Modifier.fillMaxSize().padding(5.dp).background(FermuxColors.background)) {
        QuickDownloading(
            progress = 50f,
            onCancel = {}
        )
    }
}