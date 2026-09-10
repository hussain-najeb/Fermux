package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.formats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.downloaderComponents.FormatTiles
import org.foss.fermux.ytdlp.logic.downloader.VideoQuality


@Composable
fun VideoQualityChoices(
     onQualitySelected: (VideoQuality) -> Unit,
     onBack: () -> Unit
) {

     val videoListOptions = listOf(
          DownloaderFormatLists(
               title = "Back",
               description = "Choose a different format",
               icon = R.drawable.back_arrow,
               onClick = onBack
          ),
          DownloaderFormatLists(
               title = "Best",
               description = "Highest available resolution",
               onClick = { onQualitySelected(VideoQuality.BEST) }
          ),
          DownloaderFormatLists(
               title = "1080p",
               description = "Full HD",
               onClick = { onQualitySelected(VideoQuality.HD1080) }
          ),
          DownloaderFormatLists(
               title = "720p",
               description = "HD",
               onClick = { onQualitySelected(VideoQuality.HD720) }
          ),
          DownloaderFormatLists(
               title = "480p",
               description = "SD",
               onClick = { onQualitySelected(VideoQuality.SD480) }
          ),
          DownloaderFormatLists(
               title = "360p",
               description = "Lower quality, smaller size",
               onClick = { onQualitySelected(VideoQuality.Q360) }
          ),
          DownloaderFormatLists(
               title = "240p",
               description = "Low quality",
               onClick = { onQualitySelected(VideoQuality.Q240) }
          ),
          DownloaderFormatLists(
               title = "144p",
               description = "Lowest quality, smallest size",
               onClick = { onQualitySelected(VideoQuality.Q144) }
          )
     )
     Column(
          modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp)
     ) {
          videoListOptions.forEach { option ->
               FormatTiles(
                    title = option.title,
                    description = option.description,
                    image = option.icon,
                    onClick = { option.onClick?.invoke() }
               )
          }
     }
}