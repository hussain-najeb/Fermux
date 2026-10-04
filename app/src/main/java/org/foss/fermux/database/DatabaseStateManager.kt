package org.foss.fermux.database

data class DownloadsStateManager(
     val downloads: List<DownloadsDatabaseField> = emptyList(),
     val sorting: DownloadsSorter = DownloadsSorter.Title,
     val isDeleting: Boolean = false,
     val selectedDelete: DownloadsDatabaseField? = null,
     val selectedEntry: DownloadsDatabaseField? = null
)


// TODO. add "most recent" to the dao and make it a sorting option.
enum class DownloadsSorter {
     Title,
     Size,
     Extractor
}