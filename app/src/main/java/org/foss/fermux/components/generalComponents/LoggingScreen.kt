package org.foss.fermux.components.generalComponents

import android.content.ClipData
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.ErrorCopyButton
import org.foss.fermux.components.buttons.ImageButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono
import org.foss.fermux.utils.DebugClass


@Composable
fun LoggingScreen(
     loggingTitle: String,
     logs: String,
     debugLogs: List<DebugClass>,
     debugSwitch: Boolean,
     expanded: Boolean,
     navController: NavController
) {

     val pageScroll = rememberScrollState()
     val logsScroll = rememberScrollState()
     val debugScroll = rememberScrollState()
     val scope = rememberCoroutineScope()
     val snackbarHostState = remember { SnackbarHostState() }
     val clipboard = LocalClipboard.current
     var expandedSurface by remember { mutableStateOf(expanded) }


     MediumTopBarScaffold(
          title = loggingTitle,
          snackbarHost = { AppSnackBar(snackbarHostState) },
          onBack = { navController.popBackStack() }
     ) { innerPadding ->
          Column(
               modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(pageScroll)
                    .padding(16.dp),
               horizontalAlignment = Alignment.CenterHorizontally
          ) {
               Surface(
                    modifier = Modifier
                         .fillMaxWidth()
                         .height(250.dp),
                    color = FermuxColors.fermuxComponents,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, FermuxColors.white)
               ) {
                    Box(
                         modifier = Modifier
                              .fillMaxSize()
                              .padding(horizontal = 10.dp, vertical = 12.dp)
                    ) {
                         Text(
                              text = logs,
                              color = FermuxColors.white,
                              fontFamily = JetbrainsMono,
                              modifier = Modifier
                                   .fillMaxSize()
                                   .verticalScroll(logsScroll)
                                   .padding(6.dp)
                         )
                    }
               }
               ErrorCopyButton(
                    modifier = Modifier
                         .padding(start = 2.dp, top = 6.dp)
                         .align(Alignment.Start)
                         .size(50.dp),
                    onClick = {
                         scope.launch {
                              val clipData = ClipData.newPlainText("logs", logs)
                              clipboard.setClipEntry(ClipEntry(clipData))
                              snackbarHostState.showSnackbar(
                                   message = "Copied logs",
                                   duration = SnackbarDuration.Short
                              )
                         }
                    }
               )
               Row(modifier = Modifier.fillMaxWidth()) {
                    if (debugSwitch) {
                         ErrorCopyButton(
                              modifier = Modifier
                                   .padding(2.dp)
                                   .size(50.dp),
                              onClick = {
                                   scope.launch {
                                        val formattedDebugLogs = debugLogs.joinToString("\n\n") { log ->
                                             buildString {
                                                  append("${log.timestamp} ${log.tag} ${log.level} ${log.message}")
                                                  log.throwable?.let {
                                                       appendLine()
                                                       append(it.stackTraceToString())
                                                  }
                                             }
                                        }
                                        val clipData = ClipData.newPlainText("debug logs", formattedDebugLogs)
                                        clipboard.setClipEntry(ClipEntry(clipData))
                                        snackbarHostState.showSnackbar(
                                             message = "Copied debug logs",
                                             duration = SnackbarDuration.Short
                                        )
                                   }
                              }
                         )
                         ImageButton(
                              modifier = Modifier
                                   .padding(2.dp)
                                   .size(50.dp),
                              border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder),
                              contentPadding = PaddingValues(10.dp),
                              shape = RoundedCornerShape(16.dp),
                              onClick = { expandedSurface = !expandedSurface },
                              image = R.drawable.debug
                         )
                    }
               }
               AnimatedVisibility(
                    visible = expandedSurface,
                    enter = expandVertically(tween(250)) + fadeIn(),
                    exit = shrinkVertically(tween(250)) + fadeOut()
               ) {
                    Surface(
                         modifier = Modifier
                              .fillMaxWidth()
                              .height(250.dp),
                         color = FermuxColors.fermuxComponents,
                         shape = RoundedCornerShape(10.dp),
                         border = BorderStroke(1.dp, FermuxColors.white)
                    ) {
                         LazyColumn(
                              modifier = Modifier
                                   .fillMaxSize()
                                   .padding(horizontal = 10.dp, vertical = 12.dp)
                         ) {
                              if (debugLogs.isEmpty()) {
                                   item {
                                        Text(
                                             text = "No downloader debug logs captured yet",
                                             color = FermuxColors.fermuxBackgroundTextColor,
                                             fontFamily = JetbrainsMono,
                                             modifier = Modifier.fillMaxWidth()
                                        )
                                   }
                              } else {
                                   items(debugLogs){ log ->
                                        Text(
                                             text = "${log.tag} \n ${log.message} \n ${log.level} \n ${log.timestamp} \n ${log.throwable}",
                                             color = FermuxColors.white,
                                             fontFamily = JetbrainsMono,
                                             modifier = Modifier
                                                  .fillMaxWidth()
                                                  .padding(6.dp)
                                                  .verticalScroll(debugScroll)
                                        )
                                   }
                              }
                         }
                    }
               }
          }
     }
}