package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.formats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.downloaderComponents.FormatTiles
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ytdlp.logic.downloader.VideoQuality


@Composable
fun VideoQualityChoices(
     onQualitySelected: (VideoQuality) -> Unit,
     onBack: () -> Unit
) {

     val videoListOptions = listOf(
          SettingListInfo(
               title = "Back",
               description = "Back to previous page",
               image = R.drawable.back_arrow,
               onClick = onBack,
               position = TilePosition.TOP
          ),
          SettingListInfo(
               title = "Best",
               description = "Highest available resolution",
               onClick = { onQualitySelected(VideoQuality.BEST) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "1080p",
               description = "A 1080 × 1920 video",
               onClick = { onQualitySelected(VideoQuality.HD1080) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "720p",
               description = "A 1280 × 720 video",
               onClick = { onQualitySelected(VideoQuality.HD720) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "480p",
               description = "A 854 × 480 video",
               onClick = { onQualitySelected(VideoQuality.SD480) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "360p",
               description = "A 640 × 360 video",
               onClick = { onQualitySelected(VideoQuality.Q360) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "240p",
               description = "A 426 × 240 video",
               onClick = { onQualitySelected(VideoQuality.Q240) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "144p",
               description = "A 256 × 144 video",
               onClick = { onQualitySelected(VideoQuality.Q144) },
               position = TilePosition.BOTTOM
          )
     )
     Column(
          modifier = Modifier
               .fillMaxWidth()
               .padding(start = 4.dp, end = 4.dp)

     ) {
          videoListOptions.forEach { option ->
               FormatTiles(
                    title = option.title,
                    description = option.description,
                    shape = option.position.toShape(),
                    image = option.image,
                    onClick = { option.onClick?.invoke() }
               )
          }
     }
}