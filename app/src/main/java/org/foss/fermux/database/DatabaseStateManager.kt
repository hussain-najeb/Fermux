package org.foss.fermux.database

data class DownloadsStateManager(
     val downloads: List<DownloadsDatabaseField> = emptyList(),
     val title: String = "",
     val duration: Int = 0,
     val size: Long = 0,
     val isDeleting: Boolean = false,
     val sorting: DownloadsSorter = DownloadsSorter.Title
)