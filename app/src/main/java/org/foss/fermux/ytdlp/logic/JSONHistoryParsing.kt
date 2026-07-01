package org.foss.fermux.ytdlp.logic

import kotlinx.serialization.Serializable


@Serializable
data class DownloadHistoryEntry(
    val title: String,
    val thumbnail: String,
    val url: String,
    val videoTime: Long
        )

