package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.CancelButton
import org.foss.fermux.fermuxUIComponents.buttons.LogImage
import org.foss.fermux.fermuxUIComponents.downloaderComponents.DownloaderCard
import org.foss.fermux.fermuxUIComponents.downloaderComponents.FermuxDownloadDescription
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.Miscellaneous
import org.foss.fermux.ytdlp.logic.downloader.DownloadMetadata
import org.foss.fermux.ytdlp.logic.downloader.sizeFormatting
import org.foss.fermux.ytdlp.logic.downloader.videoTime


private enum class ProgressState { InProgress, Done }

@Composable
fun FinishedDownloadCard(
     metadata: DownloadMetadata,
     progress: Float? = null,
     onCancel: () -> Unit,
     navController: NavController,
     @SuppressLint("ContextCastToActivity") settingsViewModel: DownloaderSettingsViewModel = viewModel(
          viewModelStoreOwner = LocalContext.current as ComponentActivity
     )

) {
     val showYtdlpDetails by settingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()

     FinishedCardContent(
          metadata = metadata,
          progress = progress,
          onCancel = onCancel,
          navController = navController,
          showYtdlpDetails = showYtdlpDetails
     )
}

@Composable
private fun FinishedCardContent(
     metadata: DownloadMetadata,
     progress: Float? = null,
     onCancel: () -> Unit,
     navController: NavController,
     showYtdlpDetails: Boolean
) {
     val downloadState = progress?.let {
          if (it >= 100f) ProgressState.Done else ProgressState.InProgress
     }

     Column(modifier = Modifier.fillMaxSize()) {
          DownloaderCard(modifier = Modifier.wrapContentSize()) {
               Box(
                    modifier = Modifier
                         .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                         .background(FermuxColors.fermuxSurface)
               ) {
                    AsyncImage(
                         model = metadata.thumbnail,
                         contentDescription = null,
                         contentScale = ContentScale.Crop,
                         modifier = Modifier
                              .aspectRatio(16f/9f)
                              .background(FermuxColors.fermuxSurface)
                    )
                    progress?.let {
                         when (downloadState) {
                              ProgressState.InProgress -> Column(
                                   modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(50.dp)
                                        .background(
                                             color = FermuxColors.fermuxComponents.copy(alpha = 0.70f),
                                             shape = RoundedCornerShape(8.dp)
                                        )
                              ) {
                                   CircularWavyProgressIndicator(
                                        progress = { progress / 100f },
                                        color = FermuxColors.fermuxGenericBorder,
                                        trackColor = FermuxColors.fermuxTertiaryBorder,
                                        modifier = Modifier
                                             .padding(8.dp)
                                             .align(Alignment.CenterHorizontally)
                                   )
                              }

                              ProgressState.Done -> Column(
                                   modifier = Modifier
                                        .align(Alignment.Center)
                                        .background(
                                             color = FermuxColors.fermuxComponents.copy(alpha = 0.75f),
                                             shape = RoundedCornerShape(8.dp)
                                        )
                              ) {
                                   Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Download Complete",
                                        modifier = Modifier
                                             .padding(8.dp)
                                             .align(Alignment.CenterHorizontally),
                                        tint = FermuxColors.fermuxWhiteColor
                                   )
                              }

                              null -> Unit
                         }
                    }
                    Box(modifier = Modifier
                         .padding(5.dp)
                         .wrapContentSize()
                         .background(
                              color = FermuxColors.fermuxComponents.copy(alpha = 0.75f),
                              shape = RoundedCornerShape(5.dp)
                         )
                         .wrapContentSize()
                         .align(Alignment.BottomEnd)
                    ) {
                         Row(modifier = Modifier.wrapContentSize()) {
                              metadata.size?.let {
                                   Text(
                                        text = sizeFormatting(it),
                                        color = FermuxColors.fermuxWhiteColor,
                                        fontSize = 16.sp,
                                        modifier = Modifier.padding(3.dp)
                                   )
                              }
                              Text(
                                   text = videoTime(seconds = metadata.duration),
                                   color = FermuxColors.fermuxWhiteColor,
                                   fontSize = 16.sp,
                                   modifier = Modifier.padding(3.dp)
                              )
                         }
                    }
               }

               FermuxDownloadDescription(modifier = Modifier.fillMaxWidth()
               ) {
                    Column(modifier = Modifier.height(100.dp)) {
                         Text(
                              text = metadata.title,
                              fontFamily = FontFamily.Default,
                              fontSize = 15.sp,
                              color = FermuxColors.fermuxWhiteColor,
                              maxLines = 1,
                              overflow = TextOverflow.Ellipsis,
                              fontWeight = FontWeight.W400,
                              modifier = Modifier
                                   .padding(7.dp)
                         )
                         metadata.uploader?.let {
                              Text(
                                   text = it,
                                   fontFamily = FontFamily.Default,
                                   fontSize = 13.sp,
                                   color = FermuxColors.fermuxOffWhiteTextColor,
                                   maxLines = 1,
                                   overflow = TextOverflow.Ellipsis,
                                   modifier = Modifier
                                        .padding(7.dp)
                              )
                         }
                         metadata.resolution?.let {
                              Text(
                                   text = it,
                                   fontFamily = FontFamily.Default,
                                   fontSize = 15.sp,
                                   color = FermuxColors.fermuxOffWhiteTextColor,
                                   maxLines = 1,
                                   overflow = TextOverflow.Ellipsis,
                                   modifier = Modifier
                                        .padding(7.dp)
                              )
                         }
                    }
               }
          }

          Row(
               modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
               verticalAlignment = Alignment.CenterVertically
          ) {
               if (showYtdlpDetails) {
                    LogImage(
                         image = R.drawable.logs,
                         onClick = { navController.navigate(Miscellaneous.DownloaderLogs.route) }
                    )
               }
               Spacer(modifier = Modifier.weight(1f))
               CancelButton(
                    onClick = { onCancel() }
               )
          }
     }
}

@Preview(showBackground = true, backgroundColor = 0xFF181825)
@Composable
fun Test3() {
     val navController = rememberNavController()

     Column(modifier = Modifier.fillMaxSize()) {

          FinishedCardContent(
               metadata = DownloadMetadata(
                    title = "Example Video Title, TEST....TEST. This is a test",
                    thumbnail = "/home/Hussain/Downloads/01_HistoryUniverse_Front_5aa6c115-6004-4508-9d43-41752d9cf891.jpg",
                    duration = 578,
                    uploader = "Example uploader, Youtube Channel, Or Null",
                    size = 35345455,
                    resolution = "720p"
               ),
               onCancel = {},
               navController = navController,
               progress = 100F,
               showYtdlpDetails = true
          )
     }
}
