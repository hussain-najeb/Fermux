package org.foss.fermux.components.downloaderComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SeqMetadataCard(
    modifier: Modifier,
    image: String,
    onCancelRequest: () -> Unit
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
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Surface(
                modifier = Modifier
                    .aspectRatio(4f/3f)
                    .align(Alignment.CenterVertically),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
            ) {
                CircularWavyProgressIndicator(modifier = Modifier.size(80.dp))
            }
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = FermuxColors.darkPurple,
            ) {
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                ) {
                    CancelButton(
                        modifier = Modifier.padding(10.dp),
                        transparency = true,
                        onClick = onCancelRequest,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF15152e)
@Composable
fun test15() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        SeqMetadataCard(
            modifier = Modifier,
            image = "/home/Hussain/Pictures/Wallpapers/wallpaperbetter.com_1920x1080(2).jpg",
            onCancelRequest = {},
        )
    }
}