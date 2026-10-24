package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
import org.foss.fermux.components.buttons.IconTextButton
import org.foss.fermux.components.downloaderComponents.DownloaderCard
import org.foss.fermux.components.downloaderComponents.FermuxDownloadDescription
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.Miscellaneous
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.DownloadMetadata
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.resolutionFormatting
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.sizeFormatting
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.videoTime


enum class ProgressState { InProgress, Done }

@Composable
fun FinishedDownloadCard(
     metadata: DownloadMetadata,
     progress: Float? = null,
     onCancel: () -> Unit,
     navController: NavController,
     snackbarHostState: SnackbarHostState,
     settingsViewModel: DownloaderSettingsViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
) {

     val showYtdlpDetails by settingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()
     val task = progress != null && progress >= 100

     LaunchedEffect(task) {
          snackbarHostState.showSnackbar(
               message = if (task) "Download Complete" else "Downloading...",
               duration = SnackbarDuration.Short
          )
     }

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
          DownloaderCard(modifier = Modifier.wrapContentSize(),
          ) {
               Box {
                    AsyncImage(
                         model = metadata.thumbnail,
                         contentDescription = null,
                         contentScale = ContentScale.Crop,
                         modifier = Modifier
                              .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp))
                              .aspectRatio(16f/9f)
                              .background(FermuxColors.fermuxSurface)
                    )

                    AdditionalInfo(progress, downloadState, metadata)

               }

               Description(metadata)
          }
          Row(
               modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
               verticalAlignment = Alignment.CenterVertically
          ) {
               if (showYtdlpDetails) {
                    IconTextButton(
                         text = "View Logs",
                         image = R.drawable.log,
                         onClick = { navController.navigate(Miscellaneous.DownloaderLogs.route) }
                    )
               }

               Spacer(modifier = Modifier.weight(1f))

               IconTextButton(
                    text = "New Download",
                    image = R.drawable.undo,
                    onClick = { onCancel() }
               )
          }
     }
}

@Composable
private fun Description(
     metadata: DownloadMetadata
) {
     FermuxDownloadDescription(
          modifier = Modifier.fillMaxWidth()
     ) {
          Column(modifier = Modifier.height(100.dp)) {
               Text(
                    text = metadata.title,
                    fontSize = 15.sp,
                    color = FermuxColors.white,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier
                         .padding(7.dp)
               )
               metadata.uploader?.let {
                    Text(
                         text = it,
                         fontSize = 13.sp,
                         color = FermuxColors.offWhiteTextColor,
                         maxLines = 1,
                         overflow = TextOverflow.Ellipsis,
                         modifier = Modifier
                              .padding(7.dp)
                    )
               }
               Row {
                    metadata.resolution.let {
                         Text(
                              text = if (it.equals("audio only", ignoreCase = true))
                                   metadata.resolution else "${resolutionFormatting(it)}p",
                              fontSize = 15.sp,
                              color = FermuxColors.offWhiteTextColor,
                              maxLines = 1,
                              overflow = TextOverflow.Ellipsis,
                              modifier = Modifier
                                   .padding(start = 7.dp, top = 7.dp)
                         )
                    }
                    metadata.audioQuality?.let {
                         Text(
                              text = " | audio quality is $it kbps",
                              fontSize = 15.sp,
                              color = FermuxColors.offWhiteTextColor,
                              maxLines = 1,
                              overflow = TextOverflow.Ellipsis,
                              modifier = Modifier
                                   .padding(top = 7.dp)
                         )
                    }
                    Text(
                         text = " | ${metadata.format}",
                         fontSize = 15.sp,
                         color = FermuxColors.offWhiteTextColor,
                         overflow = TextOverflow.Ellipsis,
                         modifier = Modifier
                              .padding(top = 7.dp)
                    )
               }
          }
     }
}
@Composable
private fun BoxScope.AdditionalInfo(
     progress: Float?,
     downloadState: ProgressState?,
     metadata: DownloadMetadata
) {
     progress?.let {
          when (downloadState) {
               ProgressState.InProgress -> Column(
                    modifier = Modifier
                         .align(Alignment.Center)
                         .size(50.dp)
                         .background(
                              color = FermuxColors.warmPurple.copy(alpha = 0.70f),
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
                              color = FermuxColors.warmPurple.copy(alpha = 0.75f),
                              shape = RoundedCornerShape(8.dp)
                         )
               ) {
                    Icon(
                         Icons.Default.Check,
                         contentDescription = "Download Complete",
                         modifier = Modifier
                              .padding(8.dp)
                              .align(Alignment.CenterHorizontally),
                         tint = FermuxColors.white
                    )
               }

               null -> Unit
          }
          Box(modifier = Modifier
               .padding(5.dp)
               .wrapContentSize()
               .background(
                    color = FermuxColors.warmPurple.copy(alpha = 0.75f),
                    shape = RoundedCornerShape(5.dp)
               )
               .wrapContentSize()
               .align(Alignment.BottomEnd)
          ) {
               Row(modifier = Modifier.wrapContentSize()) {
                    metadata.size?.let {
                         Text(
                              text = sizeFormatting(it),
                              color = FermuxColors.white,
                              fontSize = 16.sp,
                              modifier = Modifier.padding(3.dp)
                         )
                    }
                    metadata.duration?.let {
                         Text(
                              text = videoTime(seconds = it),
                              color = FermuxColors.white,
                              fontSize = 16.sp,
                              modifier = Modifier.padding(3.dp)
                         )
                    }
               }
          }
     }
}




@Preview(showBackground = true, backgroundColor = 0xFF181825)
@Composable
fun test3() {
     val navController = rememberNavController()

     Column(modifier = Modifier.fillMaxSize()) {

          FinishedCardContent(
               metadata = DownloadMetadata(
                    mediaId = "",
                    url = "",
                    extractor = "",
                    title = "Example Video Title, TEST....TEST. This is a test",
                    thumbnail = "/home/Hussain/Downloads/tempdev/thumbnail.webp",
                    duration = 578,
                    uploader = "Example uploader, Youtube Channel, Or Null",
                    size = 35345455,
                    format = "",
                    audioQuality = 125.6,
                    resolution = "720p"
               ),
               onCancel = {},
               navController = navController,
               progress = 100F,
               showYtdlpDetails = true
          )
     }
}
