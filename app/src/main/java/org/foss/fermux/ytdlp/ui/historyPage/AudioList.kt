@file:Suppress("RemoveRedundantQualifierName")

package org.foss.fermux.ytdlp.ui.historyPage

import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.storage.SettingsViewModel
import org.foss.fermux.storage.JSONHistoryCards

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
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (audioHistory.isEmpty()) {
                item {
                    Text(
                        "No Audio History",
                        color = Color(0xFF9AA6B6),
                        fontFamily = FontFamily.Default,
                        fontSize = 35.sp,
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


//  TODO. The cards should be scrollable and not static


@Composable
fun StoredCard(entry: JSONHistoryCards) {
    HistoryCards(entry = entry)
}