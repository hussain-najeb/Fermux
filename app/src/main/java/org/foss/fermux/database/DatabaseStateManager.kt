package org.foss.fermux.database

data class DownloadsStateManager(
     val downloads: List<DownloadsDatabaseField> = emptyList(),
     val sorting: DownloadsSorter = DownloadsSorter.Title,
     val isDeleting: Boolean = false,
     val selectedDelete: DownloadsDatabaseField? = null
)

enum class DownloadsSorter {
     Title,
     Size,
     Extractor
}