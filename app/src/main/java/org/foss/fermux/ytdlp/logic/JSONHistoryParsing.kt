package org.foss.fermux.ytdlp.logic

import kotlinx.serialization.Serializable


@Serializable
data class JSONHistoryCards(
    val title: String,
    val thumbnail: String,
    val url: String,
    val videoDuration: Long,
    val downloadTime: Long,
    )

