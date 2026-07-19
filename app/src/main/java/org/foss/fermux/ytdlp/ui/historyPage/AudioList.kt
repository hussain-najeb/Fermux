@file:Suppress("RemoveRedundantQualifierName")

package org.foss.fermux.ytdlp.ui.historyPage

import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion.then
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.storage.SettingsViewModel
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors

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
            .background(FermuxColors.fermuxBackground)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Audio History",
            color = FermuxColors.fermuxTextColorInActive,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            modifier = Modifier.padding(start = 24.dp, top = 19.dp, end = 24.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 10.dp),
            thickness = 1.dp,
            color = FermuxColors.fermuxSurface
        )


        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            if (audioHistory.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),

                        contentAlignment = Alignment.Center
                    ) {

                        Spacer(modifier = Modifier.height(700.dp))

                            Text(
                                text = "Audio files will appear here.",
                                color = FermuxColors.fermuxTextColorBackground,
                                fontSize = 16.sp,
                                fontFamily = FontFamily.SansSerif,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 40.dp)
                            )
                    }
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