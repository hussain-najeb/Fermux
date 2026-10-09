package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.formats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.components.downloaderComponents.FormatTiles
import org.foss.fermux.settings.logic.InfoListClass
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ytdlp.logic.downloader.FormatKind


@Composable
fun IdleQualityChoices(onPick: (FormatKind) -> Unit, onCancel: () -> Unit) {


     val formatOptions = listOf(
          InfoListClass(
               title = "Cancel",
               description = "Cancel this downloader process",
               onClick = { onCancel() },
               image = R.drawable.cancel_buttons,
               position = TilePosition.TOP
          ),
          InfoListClass(
               title = "Audio",
               description = "Only download the audio track",
               image = R.drawable.audio,
               onClick = { onPick(FormatKind.Audio) },
               position = TilePosition.MIDDLE
          ),
          InfoListClass(
               title = "Video",
               description = "Download the full video",
               image = R.drawable.video,
               onClick = { onPick(FormatKind.Video) },
               position = TilePosition.BOTTOM
          )
     )
     Column(
          modifier = Modifier
               .fillMaxWidth()
               .padding(start = 4.dp, end = 4.dp)
     ) {
          formatOptions.forEach { option ->
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