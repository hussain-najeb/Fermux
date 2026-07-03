package org.foss.fermux.ytdlp.logic

import kotlinx.serialization.Serializable


@Serializable
data class JSONHistoryCards(
    val title: String,
    val thumbnail: String,
    val url: String,
    val uploader: String? = null,
    val videoDuration: Long,
    val downloadTime: Long,
    )

