package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderQualityFormatState


import androidx.compose.runtime.Composable

data class DownloaderFormatLists (
    val title: String,
    val description: String,
    val icon: Int? = null,
    val onClick: (() -> Unit)? = null,
    val content: @Composable (() -> Unit)? = null,
	)