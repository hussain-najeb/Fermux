package org.foss.fermux.utils

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat


fun allowNotificationPermission(notification: () -> Unit) {

     val context = LocalContext.current

     var processNotifAllowance by remember { mutableStateOf<(() -> Unit)?>(null) }

     val notificationPermissionManager = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
          processNotifAllowance?.invoke()
          processNotifAllowance = null
     }

     val permissionAlreadyGranted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU || ContextCompat.checkSelfPermission(
          context,
          Manifest.permission.POST_NOTIFICATIONS
     ) == PackageManager.PERMISSION_GRANTED

     if (permissionAlreadyGranted) {
          notification()
     } else {
          processNotifAllowance = notification
          notificationPermissionManager.launch(
               Manifest.permission.POST_NOTIFICATIONS
          )
     }
}