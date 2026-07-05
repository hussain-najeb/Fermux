package org.foss.fermux.ytdlp.ui.historyPage

import androidx.compose.runtime.Composable
import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.storage.SettingsViewModel
import org.foss.fermux.storage.JSONHistoryCards

@SuppressLint("ContextCastToActivity")
@Composable
fun DownloadVideoList() {

    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity, factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    )

    val videoHistory by settingsViewModel.videoHistoryList.collectAsState()

    Column(
        modifier = Modifier
            .background(Color(0xFF181825))
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            "Video History",
            fontFamily = FontFamily.Default,
            fontStyle = FontStyle.Italic,
            fontSize = 40.sp,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            if (videoHistory.isEmpty()) {
                item {
                    Text(
                        text = "No Items Here!",
                        fontStyle = FontStyle.Normal,
                        fontFamily = FontFamily.Default,
                        fontSize = 35.sp
                    )
                }
            } else {
                items(videoHistory) {videoItem -> StoredVideos(entry = videoItem) }
            }
        }
    }
}





@Composable
fun StoredVideos(entry: JSONHistoryCards)
{
    HistoryCards(entry = entry)
}



