package org.foss.fermux.ffmpeg.ui.formatStates

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.FormatLists
import org.foss.fermux.ffmpeg.logic.MediaKind

@SuppressLint("SuspiciousIndentation")
@Composable
fun IdleConversionState(onPick: (MediaKind) -> Unit) {

     val scrollState = rememberScrollState()

     val formatOptions = listOf(
          FormatListItem(
               title = "Audio",
               description = "Convert the selected media to audio",
               image = R.drawable.audio,
               onClick = { onPick(MediaKind.AUDIO) }
          ),
          FormatListItem(
               title = "Video",
               description = "Convert the selected media to video",
               image = R.drawable.video,
               onClick = { onPick(MediaKind.VIDEO) }
          ),
          FormatListItem(
               title = "Image",
               description = "Convert selected media to image",
               image = R.drawable.image,
               onClick = { onPick(MediaKind.IMAGE) }
          )
     )
     Column(modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(scrollState)
     ) {
          formatOptions.forEach { option ->
               FormatLists(
                    title = option.title,
                    image = option.image,
                    description = option.description,
                    onClick = option.onClick
               )
          }
     }
}