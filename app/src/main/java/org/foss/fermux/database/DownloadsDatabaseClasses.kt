package org.foss.fermux.database

import androidx.room.Entity

@Entity(
     tableName = "downloads",
     primaryKeys = ["extractor", "videoId"]
)
data class DownloadsDatabaseField(
     val extractor: ,
     val title: String? = null,
     val uploader: String? = null,
     val thumbnail: String? = null,
     val duration: Int? = null,
     val size: Long? = null,
     val format: String,
)