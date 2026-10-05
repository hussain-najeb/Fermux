package org.foss.fermux.database

data class DownloadsStateManager(
     val downloads: List<DownloadsDatabaseField> = emptyList(),
     val sorting: DownloadsSorter = DownloadsSorter.TitleASC,
     val isDeleting: Boolean = false,
     val selectedDelete: DownloadsDatabaseField? = null,
     val selectedEntry: DownloadsDatabaseField? = null,
     val isViewingInfo: Boolean? = false
)


// TODO. add "most recent" to the dao and make it a sorting option.
enum class DownloadsSorter {
     TitleASC,
     SizeASC,
     TitleDESC,
     SizeDESC,}