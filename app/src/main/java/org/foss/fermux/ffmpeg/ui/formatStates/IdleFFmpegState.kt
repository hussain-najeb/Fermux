package org.foss.fermux.ffmpeg.ui.formatStates

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
               title = "Audio",
               description = "Convert the selected media to audio",
               image = R.drawable.audio,
               onClick = { onPick(MediaKind.AUDIO) },
               position = TilePosition.MIDDLE
          ),
          InfoListClass(
               title = "Video",
               description = "Convert the selected media to video",
               image = R.drawable.video,
               onClick = { onPick(MediaKind.VIDEO) },
               position = TilePosition.MIDDLE
          ),
          InfoListClass(
               title = "Image",
               description = "Convert selected media to image",
               image = R.drawable.image,
               onClick = { onPick(MediaKind.IMAGE) },
               position = TilePosition.MIDDLE
          )
     )
     Column(
          modifier = Modifier
               .fillMaxWidth()
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