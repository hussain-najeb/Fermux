package org.foss.fermux.database


enum class DownloadsSorter {
     Title,
     Duration,
     Size,
     Extractor
}

sealed interface DownloadsEvent {

     object ShowDialog: DownloadsEvent

     object HideDialog: DownloadsEvent

     data class SortDownloads(val sorting: DownloadsSorter): DownloadsEvent

     data class DeleteDownload(val download: DownloadsDatabaseField): DownloadsEvent
}