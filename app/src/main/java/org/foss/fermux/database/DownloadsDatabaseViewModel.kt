package org.foss.fermux.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DownloadsDatabaseViewModel(
     private val dao: DownloadsDao
): ViewModel() {

     private val _sorting = MutableStateFlow(DownloadsSorter.Title)
     private val _state = MutableStateFlow(DownloadsStateManager())
     @OptIn(ExperimentalCoroutinesApi::class)
     private val _downloadSorter = _sorting.flatMapLatest { sorter ->
          when(sorter) {
               DownloadsSorter.Duration -> dao.getDownloadsOrderedByDuration()
               DownloadsSorter.Size -> dao.getDownloadsOrderedBySize()
               DownloadsSorter.Title -> dao.getDownloadsOrderedByTitle()
               DownloadsSorter.Extractor -> dao.getDownloadsOrderedByExtractor()
          }
     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

     val state = combine(_state, _sorting, _downloadSorter) { state, sortType, downloadSorter ->
           state.copy(
                sorting = sortType,
                downloads = downloadSorter
           )
     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(2500), DownloadsStateManager())

     fun onEvent(event: DownloadsEvent) {
          when(event) {

               is DownloadsEvent.DeleteDownload -> {
                    viewModelScope.launch {
                         dao.deleteDownload(event.download)
                         _state.update { it.copy(isDeleting = false) }
                    }
               }

               is DownloadsEvent.SortDownloads -> {
                    _sorting.value = event.sorting
               }

               DownloadsEvent.HideDialog -> {
                    _state.update { it.copy(isDeleting = false) }
               }

               DownloadsEvent.ShowDialog -> {
                    _state.update { it.copy(isDeleting = true) }
               }
          }
     }



}