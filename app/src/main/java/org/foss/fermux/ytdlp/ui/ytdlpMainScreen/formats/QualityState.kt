package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.formats


import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel
import org.foss.fermux.ytdlp.logic.downloader.FormatKind


@Composable
fun QualityState(downloaderViewModel: DownloaderViewModel) {

     var pickedFormat by remember { mutableStateOf(FormatKind.Idle) }
     val spatialSpec = MaterialTheme.motionScheme
     val context = LocalContext.current

     var downloadNotifAllow by remember { mutableStateOf<(() -> Unit)?>(null) }


     val notificationPermissionManager = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
          downloadNotifAllow?.invoke()
          downloadNotifAllow = null
     }

     fun startingDownloadWithPermissions(download: () -> Unit) {
          val permissionAlreadyGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || ContextCompat.checkSelfPermission(
               context,
               Manifest.permission.POST_NOTIFICATIONS
          ) == PackageManager.PERMISSION_GRANTED

          if (permissionAlreadyGranted) {
               download()
          } else {
               downloadNotifAllow = download
               notificationPermissionManager.launch(
                    Manifest.permission.POST_NOTIFICATIONS
               )
          }
     }

     AnimatedContent(
          targetState = pickedFormat,
          transitionSpec = {
               (slideInVertically(
                    animationSpec = spatialSpec.slowSpatialSpec(),
                    initialOffsetY = { -it }) + fadeIn(
                    initialAlpha = 0.1f
                    )
               ).togetherWith(
                         exit = slideOutVertically(
                              animationSpec = spatialSpec.slowSpatialSpec(),
                              targetOffsetY = { -it }) + fadeOut(targetAlpha = 0.1f)
                    )
          },
          label = "DownloaderQualityCardTransition",
          contentKey = { it }
     ) { targetState ->
          when (targetState) {
               FormatKind.Idle -> IdleQualityChoices(
                    onPick = { pickedFormat = it },
                    onCancel = { downloaderViewModel.cancelButton(context) }
               )
               FormatKind.Audio -> AudioQualityChoices(
                    onBack = { pickedFormat = FormatKind.Idle },
                    onQualitySelected = { quality ->
                         startingDownloadWithPermissions {
                              downloaderViewModel.startingDownload(context, audio = quality, video = null)
                         }
                    }
               )
               FormatKind.Video -> VideoQualityChoices(
                    onBack = { pickedFormat = FormatKind.Idle },
                    onQualitySelected = { quality ->
                         startingDownloadWithPermissions {
                              downloaderViewModel.startingDownload(context, video = quality, audio = null)
                         }
                    }
               )
          }
     }
}
