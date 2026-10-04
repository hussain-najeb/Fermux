package org.foss.fermux.database

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DownloadsDatabaseViewModel(
     private val dao: DownloadsDao
): ViewModel() {

     companion object {
          fun factory(context: Context) = viewModelFactory {
               initializer {
                    DownloadsDatabaseViewModel(
                         DownloaderDb.getDatabase(context).downloadsDao
                    )
               }
          }
     }

     private val _sorting = MutableStateFlow(DownloadsSorter.TitleASC)
     private val _state = MutableStateFlow(DownloadsStateManager())
     @OptIn(ExperimentalCoroutinesApi::class)
     private val _downloadSorter = _sorting.flatMapLatest { sorter ->
          when(sorter) {
               DownloadsSorter.SizeASC -> dao.getDownloadsOrderedBySizeASC()
               DownloadsSorter.TitleASC -> dao.getDownloadsOrderedByTitleASC()
               DownloadsSorter.ExtractorASC -> dao.getDownloadsOrderedByExtractorASC()
               DownloadsSorter.TitleDESC -> dao.getDownloadsOrderByTitleDESC()
               DownloadsSorter.SizeDESC -> dao.getDownloadsOrderBySizeDESC()
               DownloadsSorter.ExtractorDESC -> dao.getDownloadsOrderByExtractorDESC()
          }
     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

     val state = combine(_state, _sorting, _downloadSorter) { state, sortType, downloadSorter ->
           state.copy(
                sorting = sortType,
                downloads = downloadSorter
           )
     }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(2500), DownloadsStateManager())

     fun showDeleteDialog(item: DownloadsDatabaseField) {
          _state.update { it.copy(isDeleting = true, selectedDelete = item) }
     }

     fun hideDeleteDialog() {
          _state.update { it.copy(isDeleting = false, selectedDelete = null) }
     }

     fun deleteDownload(item: DownloadsDatabaseField) {
          viewModelScope.launch {
               dao.deleteDownload(item)
               _state.update { it.copy(isDeleting = false, selectedDelete = null) }
          }
     }

     fun titleSorter() {
          _sorting.value = when(_sorting.value) {
               DownloadsSorter.TitleASC -> DownloadsSorter.TitleASC
               DownloadsSorter.TitleDESC -> DownloadsSorter.TitleDESC
               else -> DownloadsSorter.TitleASC
          }
     }

     fun sizeSorter() {
          _sorting.value = when(_sorting.value) {
               DownloadsSorter.SizeASC -> DownloadsSorter.SizeASC
               DownloadsSorter.SizeDESC -> DownloadsSorter.SizeDESC
               else -> DownloadsSorter.SizeASC
          }
     }

     fun extractorSorter() {
          _sorting.value = when(_sorting.value) {
               DownloadsSorter.ExtractorASC -> DownloadsSorter.ExtractorASC
               DownloadsSorter.ExtractorDESC -> DownloadsSorter.ExtractorDESC
               else -> DownloadsSorter.ExtractorASC
          }
     }

}