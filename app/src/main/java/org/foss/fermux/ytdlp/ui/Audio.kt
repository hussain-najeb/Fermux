package org.foss.fermux.ytdlp.ui


import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.settings.logic.SettingsViewModel


@SuppressLint("ContextCastToActivity")
@Composable
fun DownloadedAudioScreen() {

    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity, factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    )

    val audioHistory by settingsViewModel.audioHistoryList.collectAsState()

    LazyColumn(modifier = Modifier
        .padding(10.dp)
        .background(Color(0xFF181825))
        .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            Text()

        }

    }















}
