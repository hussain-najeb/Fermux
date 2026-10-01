package org.foss.fermux.ytdlp.ui.historyPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.foss.fermux.components.downloaderComponents.HistoryCard
import org.foss.fermux.components.generalComponents.MediumTopBarScaffold
import org.foss.fermux.database.DownloadsDatabaseViewModel
import org.foss.fermux.database.DownloadsEvent
import org.foss.fermux.database.DownloadsStateManager
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun History(
     historyState: DownloadsStateManager,
     events: DownloadsEvent,
     navController: NavController,
     modifier: Modifier
) {
     val downloadsDatabaseViewModel: DownloadsDatabaseViewModel = viewModel()
     val state by downloadsDatabaseViewModel.state.collectAsStateWithLifecycle()

     MediumTopBarScaffold(
          title = "History",
          onBack = { navController.popBackStack() }
     ) {
          LazyColumn(
               modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
               verticalArrangement = Arrangement.Top,
               horizontalAlignment = Alignment.CenterHorizontally
          ) {
               if (state.downloads.isEmpty()) item {
                    Text(
                         text = "History Looks Empty!",
                         textAlign = TextAlign.Center,
                         fontSize = 13.sp,
                         fontStyle = FontStyle.Italic,
                         fontFamily = FontFamily.Default,
                         fontWeight = FontWeight.W400,
                         color = FermuxColors.fermuxOffWhiteTextColor
                    )
               } else {
                    items(state.downloads) { list ->
                         HistoryCard(
                              thumbnail = list.thumbnail?.let {

                              }
                         )
                    }
               }
          }
     }
}