package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.formats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.components.downloaderComponents.FormatTiles
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ytdlp.logic.downloader.AudioQuality

@Composable
fun AudioQualityChoices(
     onBack: () -> Unit,
     onQualitySelected: (AudioQuality) -> Unit
) {
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
               description = "Highest audio quality",
               onClick = { onQualitySelected(AudioQuality.BEST) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "High",
               description = "Best middle ground for size and quality",
               onClick = { onQualitySelected(AudioQuality.HIGH) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Medium",
               description = "Yt-dlp default audio quality",
               onClick = { onQualitySelected(AudioQuality.MEDIUM) },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = "Low",
               description = "Smallest file size with the lowest audio quality",
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