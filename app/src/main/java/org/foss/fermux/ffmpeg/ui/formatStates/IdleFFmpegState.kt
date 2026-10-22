package org.foss.fermux.ffmpeg.ui.formatStates

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.components.ffmpegComponents.FFmpegTiles
import org.foss.fermux.ffmpeg.logic.MediaKind
import org.foss.fermux.settings.logic.InfoListClass
import org.foss.fermux.settings.logic.TilePosition

@SuppressLint("SuspiciousIndentation")
@Composable
fun IdleConversionState(onPick: (MediaKind) -> Unit) {

     val scrollState = rememberScrollState()

     val formatOptions = listOf(
          InfoListClass(
               title = "Cancel",
               description = "Cancel this conversion",
               image = R.drawable.close,
               onClick = { onPick(MediaKind.Cancel) },
               position = TilePosition.TOP
          ),
          InfoListClass(
               title = "Video",
               description = "Convert the selected media to video",
               image = R.drawable.video,
               onClick = { onPick(MediaKind.Video) },
               position = TilePosition.MIDDLE
          ),
          InfoListClass(
               title = "Audio",
               description = "Convert the selected media to audio",
               image = R.drawable.audio,
               onClick = { onPick(MediaKind.Audio) },
               position = TilePosition.MIDDLE
          ),
          InfoListClass(
               title = "Image",
               description = "Convert selected media to image",
               image = R.drawable.image,
               onClick = { onPick(MediaKind.Image) },
               position = TilePosition.BOTTOM
          )
     )
     Column(
          modifier = Modifier
               .fillMaxWidth()
               .padding(bottom = 15.dp)
               .verticalScroll(scrollState)
     ) {
          formatOptions.forEach { option ->
               FFmpegTiles(
                    title = option.title,
                    image = option.image,
                    description = option.description,
                    onClick = option.onClick,
                    shape = option.position.TileShaper()
               )
          }
     }
}