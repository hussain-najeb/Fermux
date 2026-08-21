package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderQualityFormatState

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.downloaderComponents.DownloaderCard
import org.foss.fermux.fermuxUIComponents.downloaderComponents.DownloaderFormatList
import org.foss.fermux.ytdlp.logic.downloader.AudioQuality
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel

@Composable
fun AudioQualityChoices(downloaderViewModel: DownloaderViewModel, onBack: () -> Unit) {
	

val context = LocalContext.current

val audioListOptions = listOf(
	 DownloaderFormatLists(
            title = "Back",
            description = "Choose a different format",
            icon = R.drawable.back_arrow,
            onClick = onBack
        ),
    DownloaderFormatLists(
    	title = "Best Audio Quality",
    	description = "Highest available audio quality (~220-260 kbps)",
    	onClick = {
    		downloaderViewModel.startingDownload(context, AudioQuality.BEST, null)
    		}
    	),
    DownloaderFormatLists(
    	title = "High",
    	description = "~170-210 kbps",
    	onClick = {
    		downloaderViewModel.startingDownload(context, AudioQuality.HIGH, null)
    		}
    	),
	DownloaderFormatLists(
            title = "Medium",
            description = "~100-140 kbps, yt-dlp default",
            onClick = { downloaderViewModel.startingDownload(context, AudioQuality.MEDIUM, null) }
        ),
        DownloaderFormatLists(
            title = "Low",
            description = "Smallest file size (~65 kbps)",
            onClick = { downloaderViewModel.startingDownload(context, AudioQuality.LOW, null) }
        ),
	)
	DownloaderCard {
		Column(modifier = Modifier
			.fillMaxSize()
			) {
				audioListOptions.forEach { option ->
            	DownloaderFormatList(
                	title = option.title,
                	description = option.description,
                	image = option.icon,
                	onClick = { option.onClick?.invoke() }
            	)
			}
		}
	}
}