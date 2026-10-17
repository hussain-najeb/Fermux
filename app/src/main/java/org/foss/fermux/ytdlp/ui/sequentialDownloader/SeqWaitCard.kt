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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.foss.fermux.R
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SeqWaitCard(
    //sequentialViewModel: SequentialViewModel,
    modifier: Modifier,
    onCancelRequest: () -> Unit
) {

   // val state = sequentialViewModel.state

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
                    .aspectRatio(4f / 3f)
                    .align(Alignment.CenterVertically),
                shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                color = FermuxColors.mutedBlue
            ) {
                AsyncImage(
                    //   model = state.currentItem?.metadata?.thumbnail,
                    model = "/home/Hussain/Downloads/Random/Screenshot_20260702_074739.png",
                    contentDescription = null,
                    modifier = Modifier.aspectRatio(4f / 3f),
                    placeholder = painterResource(R.drawable.file_image),
                    fallback = painterResource(R.drawable.error_sign), // TODO. Edit the size of this
                    contentScale = ContentScale.Crop,
                    clipToBounds = true,
                )
            }

            Text(
                "Waiting"
            )

        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFF15152e)
@Composable
fun test16() {

    Column(modifier = Modifier.fillMaxSize().padding(5.dp)) {
        SeqWaitCard(
            modifier = Modifier.fillMaxWidth(),
            onCancelRequest = {}
        )
    }

}