package org.foss.fermux.dataStore

import kotlinx.serialization.Serializable


@Serializable
data class JSONHistoryCards(
     val title: String,
     val thumbnail: String,
     var url: String,
     val uploader: String? = null,
     val videoDuration: Long,
     val downloadTime: Long,
)

