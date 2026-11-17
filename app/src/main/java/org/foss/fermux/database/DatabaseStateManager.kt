package org.foss.fermux.database

data class DownloadsStateManager(
    val downloads: List<DownloadsDatabaseField> = emptyList(),
    val sorting: DownloadsSorter = DownloadsSorter.TitleASC,
    val isDeleting: Boolean = false,
    val selectedDelete: DownloadsDatabaseField? = null,
    val selectedEntry: DownloadsDatabaseField? = null,
    val isViewingInfo: Boolean? = false
)


enum class DownloadsSorter {
    TitleASC,
    SizeASC,
    TitleDESC,
    SizeDESC,
}