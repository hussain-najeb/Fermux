package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderQualityFormatState

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.foss.fermux.fermuxUIComponents.downloaderComponents.DownloaderFormatList
import org.foss.fermux.ytdlp.logic.downloader.FormatKind
import org.foss.fermux.R



@SuppressLint("SuspiciousIndentation")
@Composable
fun IdleQualityChoices(onPick: (FormatKind) -> Unit, onCancel: () -> Unit) {



	val formatOptions = listOf(
        DownloaderFormatLists(
            title = "Cancel",
            description = "Cancel this process",
            onClick = {
                onCancel()
                }
            ),
        DownloaderFormatLists(
            title = "Audio",
            description = "Download just the audio track",
            icon = R.drawable.audio,
            onClick = { onPick(FormatKind.Audio) }
        ),
        DownloaderFormatLists(
            title = "Video",
            description = "Download the full video",
            icon = R.drawable.video,
            onClick = { onPick(FormatKind.Video) }
        )
    )
    Column(modifier = Modifier
        .fillMaxWidth()
    ) {
        formatOptions.forEach { option ->
            DownloaderFormatList(
                title = option.title,
                description = option.description,
                image = option.icon,
                onClick = { option.onClick?.invoke() }
            )
        }
    }
}