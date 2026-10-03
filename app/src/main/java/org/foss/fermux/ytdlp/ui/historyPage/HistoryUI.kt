package org.foss.fermux.ytdlp.ui.historyPage

import android.content.ClipData
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.ImageButton
import org.foss.fermux.components.downloaderComponents.HistoryCard
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.MediumTopBarScaffold
import org.foss.fermux.database.DownloadsDatabaseViewModel
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun History(
     navController: NavController,
     modifier: Modifier = Modifier
) {
     val context = LocalContext.current
     val downloadsDatabaseViewModel: DownloadsDatabaseViewModel = viewModel(factory = DownloadsDatabaseViewModel.factory(context))
     val state by downloadsDatabaseViewModel.state.collectAsStateWithLifecycle()


     val snackbarHostState = remember { SnackbarHostState() }
     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()
     val containerColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.deepDarkBlue else FermuxColors.darkBlue,
          animationSpec = tween()
     )

     val scope = rememberCoroutineScope()
     val clipboard = LocalClipboard.current

     var expanded by remember { mutableStateOf(false) }
     var cardKey by remember { mutableStateOf<String?>(null) }



     MediumTopBarScaffold(
          title = "History",
          onBack = { navController.popBackStack() },
          snackbarHost = { AppSnackBar(snackbarHostState) }
     ) { innerPadding ->
          Column(
               modifier = Modifier
                    .fillMaxSize().padding(innerPadding)
          ) {

               // TODO. Implement the hopper thinggy as as one item, then make it a dropdown menu that shows the sorting options
               //  also have a "three dot" button that has "Delete" and "more info" and "copy media url"

               LazyColumn(
                    modifier = modifier
                         .fillMaxSize()
                         .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
               ) {
                    item {
                         Box(
                              modifier = Modifier
                                   .fillMaxSize()
                                   .padding(5.dp),
                              contentAlignment = Alignment.TopStart
                         ) {
//                              val surfaceColor by animateColorAsState(
//                                   targetValue = if (expanded) containerColor else Color.Transparent,
//                                   label = "color"
//                              )

                              Surface(
                                   modifier = Modifier
                                        .padding(5.dp)
                                        .animateContentSize(),
                                   shape = RoundedCornerShape(8.dp),
                                   color = Color.Transparent,
                              ) {
                                   Row(
                                        modifier = Modifier.wrapContentSize(),
                                        verticalAlignment = Alignment.CenterVertically
                                   ) {
                                        ImageButton(
                                             modifier = Modifier.padding(2.dp),
                                             image = R.drawable.filter,
                                             onClick = { expanded = !expanded }
                                        )
                                        AnimatedVisibility(
                                             visible = expanded,
                                             enter = expandHorizontally(animationSpec = tween(250)) + fadeIn(initialAlpha = 0.5f),
                                             exit = shrinkHorizontally(animationSpec = tween(200)) + fadeOut(targetAlpha = 0.6f)
                                        ) {
                                             Row(
                                                  modifier = Modifier.padding(2.dp),
                                                  verticalAlignment = Alignment.CenterVertically
                                             ) {
                                                  ImageButton(
                                                       modifier = Modifier.padding(2.dp),
                                                       image = if (state.isAscending) R.drawable.sort_descending else R.drawable.sort_ascending,
                                                       onClick = {

                                                       }
                                                  )
                                                  ImageButton(
                                                       modifier = Modifier.padding(2.dp),
                                                       image = if (state.isAscending) R.drawable.database_fill else R.drawable.database_empty,
                                                       onClick = {

                                                       }
                                                  )
                                                  ImageButton(
                                                       modifier = Modifier.padding(2.dp),
                                                       image = R.drawable.extractor,
                                                       onClick = {

                                                       }
                                                  )
                                             }
                                        }
                                   }
                              }
                         }
                    }
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
                         items(state.downloads, key = {"${it.extractor}: ${it.videoId}"}) { list ->
                              val key = "${list.extractor}:${list.videoId}"
                              HistoryCard(
                                   thumbnail = list.thumbnail,
                                   imageDescription = list.title,
                                   title = list.title,
                                   uploader = list.uploader,
                                   format = list.format,
                                   resolution = list.resolution,
                                   duration = list.duration,
                                   onMenuClick = { cardKey = if (cardKey == key) null else key }
                              )
                              HistoryActions(
                                   expanded = cardKey == key,
                                   onDelete = { downloadsDatabaseViewModel.deleteDownload(list) },
                                   onMoreInfo = {

                                   },
                                   onCopyUrl = {
                                        scope.launch {
                                             val clipData = ClipData.newPlainText("copied url", list.url)
                                             clipboard.setClipEntry(ClipEntry(clipData))
                                             snackbarHostState.showSnackbar(
                                                  message = "Copied Url",
                                                  duration = SnackbarDuration.Short
                                             )
                                        }
                                   }
                              )
                         }
                    }
               }
          }
     }
}