package org.foss.fermux.ytdlp.ui


import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.ytdlp.logic.JSONHistoryCards
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@SuppressLint("ContextCastToActivity")
@Composable
fun DownloadedAudioScreen() {

    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity, factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    )

    val audioHistory by settingsViewModel.audioHistoryList.collectAsState()

    Column(
        modifier = Modifier
            .padding(10.dp)
            .background(Color(0xFF181825))
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {

        Text(
            "Audio History",
            fontFamily = FontFamily.Default,
            fontSize = 40.sp,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )



        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(end = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (audioHistory.isEmpty()) {
                item {
                    Text(
                        "No Audio History",
                        color = Color(0xFF44474d),
                        fontFamily = FontFamily.Default,
                        fontSize = 20.sp,
                    )
                }
            } else {
                items(audioHistory) { audioItems -> StoredCard(entry = audioItems)
                Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}




@Composable
fun StoredCard(entry: JSONHistoryCards) {

    val cardBorder = Modifier
        .border(1.5.dp, Color(0xFF17DB6F), RoundedCornerShape(8.dp))
        .border(1.5.dp, Color(0xFF20B161), RoundedCornerShape(8.dp))
        .border(0.8.dp, Color(0xFF20bf6b), RoundedCornerShape(8.dp))

    val expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (expanded) 180f else 0f)

    Column(verticalArrangement = Arrangement.Center) {
        ElevatedCard(
            modifier = cardBorder
                .fillMaxSize()
        ) {
            AsyncImage(
                model = entry.thumbnail,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(16f / 9f)
            )

            // TODO. A button to expand the card so you can delete it or see the original url.
            // TODO. Have a "how much this video took to download" text to the video cards as well.

        }

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = " Title: ${entry.title}",
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            color = Color.White
        )

        Text(
            text = "Duration: ${videoTime(entry.videoDuration.toInt())}",
            modifier = Modifier
                .padding(3.dp),
            fontFamily = FontFamily.Default,
            color = Color.White
        )

        val formattedDate = remember(entry.downloadTime) {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(entry.downloadTime))
        }

        Text(
            text = "Date: $formattedDate",
            fontSize = 16.sp,
            fontFamily = FontFamily.Default,
            color = Color.White,
            modifier = Modifier
                .padding(3.dp)
        )

        entry.uploader?.let {
            Text(
                text = "Uploader: $it",
                fontSize = 16.sp,
                fontFamily = FontFamily.Default,
                color = Color.White,
                modifier = Modifier
                    .padding(3.dp)
            )
        }
    }
}
