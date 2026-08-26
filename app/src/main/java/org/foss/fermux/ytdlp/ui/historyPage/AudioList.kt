@file:Suppress("RemoveRedundantQualifierName")

package org.foss.fermux.ytdlp.ui.historyPage

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.ui.theme.FermuxColors

@SuppressLint("ContextCastToActivity")
@Composable
fun DownloadedAudioScreen(navController: NavController) {

     val context = LocalContext.current

     val settingsViewModel: DownloaderSettingsViewModel = viewModel(
          viewModelStoreOwner = LocalContext.current as ComponentActivity, factory =
               ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
     )

     val audioHistory by settingsViewModel.audioHistoryList.collectAsState()

LargeTopBarScaffold(
          title = "Video History",
          onBack = { navController.popBackStack() }
          ) { paddingValues -> 
          LazyColumn(
               modifier = Modifier
               .fillMaxSize()
               .padding(paddingValues),
               contentPadding = PaddingValues(8.dp),
               horizontalAlignment = Alignment.CenterHorizontally,
               verticalArrangement = Arrangement.Top
          ) {
               if (audioHistory.isEmpty()) {
                    item {
                         Box(
                              modifier = Modifier
                                   .fillMaxSize(),
                              contentAlignment = Alignment.Center
                         ) {

                              Text(
                                   text = "Audio files will appear here",
                                   color = FermuxColors.fermuxTextColorBackground,
                                   fontSize = 16.sp,
                                   fontFamily = FontFamily.SansSerif,
                                   textAlign = TextAlign.Center,
                              )
                         }
                    }
               } else {

                    items(audioHistory) { audioItems ->
                         StoredCard(entry = audioItems)

                         Spacer(modifier = Modifier.height(14.dp))

                    }
               }
          }
     }
}

@Composable
fun StoredCard(entry: JSONHistoryCards) {
     HistoryCards(entry = entry)
}