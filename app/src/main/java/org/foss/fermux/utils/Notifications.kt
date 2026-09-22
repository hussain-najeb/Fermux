package org.foss.fermux.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.FFmpegSettingsViewModel

@Composable
fun rememberNotificationPermissionRequest(
     onGranted: () -> Unit, onPermissionDenied: () -> Unit
): () -> Unit {

     val downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel()
     val ffmpegSettingsViewModel: FFmpegSettingsViewModel = viewModel()

     val context = LocalContext.current
     val activity = context.findActivity()



     val launcher = rememberLauncherForActivityResult(
          ActivityResultContracts.RequestPermission()
     ) { isGranted ->
          if (isGranted) {
               onGranted()

               downloaderSettingsViewModel.setDownloaderBellState(true)
               ffmpegSettingsViewModel.setFFmpegBellState(true)

          } else {

               ffmpegSettingsViewModel.setFFmpegBellState(false)
               downloaderSettingsViewModel.setDownloaderBellState(false)


               val canAskAgain = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || activity?.let {
                    ActivityCompat.shouldShowRequestPermissionRationale(
                         it, Manifest.permission.POST_NOTIFICATIONS
                    )
               } == true
               if (!canAskAgain) onPermissionDenied()
          }
     }
     return {
          if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {

               downloaderSettingsViewModel.setDownloaderBellState(true)
               ffmpegSettingsViewModel.setFFmpegBellState(true)

               onGranted()
          } else {
               val granted = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.POST_NOTIFICATIONS
               ) == PackageManager.PERMISSION_GRANTED


               if (granted) {

                    downloaderSettingsViewModel.setDownloaderBellState(true)
                    ffmpegSettingsViewModel.setFFmpegBellState(true)

                    onGranted()
               } else {
                    launcher.launch(Manifest.permission.POST_NOTIFICATIONS)
               }
          }
     }
}

fun Context.findActivity(): Activity? = when (this) {
     is Activity -> this
     is ContextWrapper -> baseContext.findActivity()
     else -> null
}