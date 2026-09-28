package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.components.buttons.ErrorCopyButton
import org.foss.fermux.components.buttons.ImageButton
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono
import org.foss.fermux.utils.DebugLogFFmpeg


@SuppressLint("ContextCastToActivity")
@Composable
fun FFmpegLogs(
     navController: NavHostController
) {
     val ffmpegViewModel: FFmpegViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
     val clipboard = LocalClipboardManager.current
     val logs = ffmpegViewModel.FFmpegLogs
     val logScrollState = rememberScrollState()
     val pageScrollState = rememberScrollState()
     val debug by DebugLogFFmpeg.enabled.collectAsStateWithLifecycle()
     val logcat by DebugLogFFmpeg.ffmpegLogcat.collectAsStateWithLifecycle()
     var debugEnabledSurface by remember { mutableStateOf(false) }

     Column(
          modifier = Modifier
               .fillMaxSize()
               .background(FermuxColors.fermuxBackground)
     ) {
          LargeTopBarScaffold(
               title = "Logs",
               onBack = { navController.popBackStack() }
          ) { paddingValues ->
               Column(
                    modifier = Modifier
                         .padding(paddingValues)
                         .verticalScroll(pageScrollState)
                         .fillMaxWidth()
                         .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
               ) {
                    Surface(
                         modifier = Modifier
                              .fillMaxWidth()
                              .height(400.dp),
                         color = FermuxColors.fermuxComponents,
                         shape = RoundedCornerShape(10.dp),
                         border = BorderStroke(1.dp, FermuxColors.fermuxWhiteColor)
                    ) {
                         Box(
                              modifier = Modifier
                                   .fillMaxSize()
                                   .padding(horizontal = 10.dp, vertical = 12.dp)
                         ) {
                              Text(
                                   text = logs,
                                   color = FermuxColors.fermuxWhiteColor,
                                   fontFamily = JetbrainsMono,
                                   modifier = Modifier
                                        .fillMaxSize()
                                        .verticalScroll(logScrollState)
                                        .padding(bottom = 40.dp)
                              )
                              ErrorCopyButton(
                                   modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .padding(8.dp)
                                        .size(50.dp),
                                   onClick = { clipboard.setText(AnnotatedString(logs)) }
                              )
                         }
                    }
                    Text(
                         text = "Note*: This is the log page for the ffmpeg output during conversion",
                         color = FermuxColors.fermuxBackgroundTextColor,
                         fontSize = 16.sp,
                         fontStyle = FontStyle.Normal,
                         fontFamily = FontFamily.Default,
                         modifier = Modifier.padding(7.dp)
                    )
                    Row(
                         modifier = Modifier.fillMaxWidth()

                    ) {
                         if (debug) {
                         ErrorCopyButton(
                              modifier = Modifier
                                   .padding(2.dp)
                                   .size(50.dp),
                              onClick = {
                                   val formattedClipboard = logcat.joinToString(separator = "\n\n") { log ->
                                        buildString {
                                             append(
                                                  "${log.timestamp} ${log.tag} ${log.level} ${log.message} ${
                                                       log.throwable?.let {
                                                            appendLine()
                                                            append(it.stackTraceToString())
                                                       }
                                                  }"
                                             )
                                        }
                                   }
                                   clipboard.setText(AnnotatedString(formattedClipboard))
                              }
                         )
                              ImageButton(
                                   modifier = Modifier
                                        .padding(2.dp)
                                        .size(50.dp),
                                   border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder),
                                   contentPadding = PaddingValues(10.dp),
                                   shape = RoundedCornerShape(16.dp),
                                   onClick = { debugEnabledSurface = !debugEnabledSurface },
                                   image = R.drawable.debug
                              )
                         }
                    }
                    AnimatedVisibility(
                         visible = debugEnabledSurface,
                         enter = expandVertically(tween(250)) + fadeIn(),
                         exit = shrinkVertically(tween(250)) + fadeOut()
                    ) {
                         Surface(
                              modifier = Modifier
                                   .fillMaxWidth()
                                   .height(250.dp),
                              color = FermuxColors.fermuxComponents,
                              shape = RoundedCornerShape(10.dp),
                              border = BorderStroke(1.dp, FermuxColors.fermuxWhiteColor)
                         ) {
                              LazyColumn(
                                   modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 10.dp, vertical = 12.dp)
                              ) {
                                   if (logcat.isEmpty()) {
                                        item {
                                             Text(
                                                  text = "No downloader debug logs captured yet",
                                                  color = FermuxColors.fermuxBackgroundTextColor,
                                                  fontFamily = JetbrainsMono,
                                                  modifier = Modifier.fillMaxWidth()
                                             )
                                        }
                                   } else {
                                        items(logcat) { log ->
                                             Text(
                                                  text = "${log.level} ${log.tag} ${log.message}${
                                                       log.throwable?.let { " $it" }.orEmpty()
                                                  } ${log.timestamp}",
                                                  color = FermuxColors.fermuxWhiteColor,
                                                  fontFamily = JetbrainsMono,
                                                  modifier = Modifier
                                                       .fillMaxWidth()
                                                       .padding(6.dp)
                                             )
                                        }
                                   }
                              }
                         }
                    }
               }
          }
     }
}