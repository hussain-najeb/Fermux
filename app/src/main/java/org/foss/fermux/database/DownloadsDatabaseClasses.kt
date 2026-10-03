package org.foss.fermux.database

import androidx.room.Entity

@Entity(
     tableName = "downloads", primaryKeys = ["extractor", "videoId"]
)
data class DownloadsDatabaseField(
     val extractor: String,
     val videoId: String,
     val fileUri: String,
     val url: String,
     val title: String? = null,
     val uploader: String? = null,
     val thumbnail: String? = null,
     val duration: Int? = null,
     val size: Long? = null,
     val format: String,
     val resolution: String? = "",  // TODO. add "most recent" to the dao and make it a sorting option.
)