package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.formats

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.downloaderComponents.FormatTiles
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ytdlp.logic.downloader.AudioQuality

@Composable
fun AudioQualityChoices(
     onBack: () -> Unit,
     onQualitySelected: (AudioQuality) -> Unit
) {

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

     val audioListOptions = listOf(
          SettingListInfo(
               title = "Back",
               description = "Back to the previous page",
               image = R.drawable.back_arrow,
               onClick = onBack,
               position = TilePosition.TOP
          ),
          SettingListInfo(
               title = "Best Audio Quality",
               description = "Highest audio quality, ~220-260 kbps",
               onClick = { onQualitySelected(AudioQuality.BEST) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "High",
               description = "~170-210 kbps",
               onClick = { onQualitySelected(AudioQuality.HIGH) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Medium",
               description = "~100-140 kbps, yt-dlp default",
               onClick = { onQualitySelected(AudioQuality.MEDIUM) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Low",
               description = "Smallest file size (~65 kbps)",
               onClick = { onQualitySelected(AudioQuality.LOW) },
               position = TilePosition.BOTTOM
          ),
     )
     Column(
          modifier = Modifier
               .fillMaxWidth()
               .padding(start = 4.dp, end = 4.dp)
     ) {
          audioListOptions.forEach { option ->
               FormatTiles(
                    title = option.title,
                    description = option.description,
                    shape = option.position.TileShaper(),
                    image = option.image,
                    onClick = { option.onClick?.invoke() }
               )
          }
     }
}

@Preview(showBackground = true, backgroundColor = 0xFF181825)
@Composable
fun Test4() {
     Column(modifier = Modifier
          .padding(10.dp)
          .fillMaxSize()
     ) {
          AudioQualityChoices(
               onBack = {},
               onQualitySelected = { quality ->
                    println("Quality is $quality")
               }
          )
     }
}