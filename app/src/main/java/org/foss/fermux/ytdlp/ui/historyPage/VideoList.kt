package org.foss.fermux.ytdlp.ui.historyPage

import androidx.compose.runtime.Composable
import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
            text = "Video History",
            color = Color.White,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(start = 24.dp, top = 19.dp, end = 24.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 10.dp),
            thickness = 1.dp,
            color = Color(0xFF303258)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
                ) {

            if (videoHistory.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                        ,
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Video files will appear here.",
                            color = Color(0xFF727882),
                            fontSize = 16.sp,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 40.dp)
                            )
                    }
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



