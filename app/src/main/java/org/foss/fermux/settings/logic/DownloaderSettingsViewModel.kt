package org.foss.fermux.settings.logic

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yausername.youtubedl_android.YoutubeDL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.foss.fermux.storage.DataStoreDownloaderSettings
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.utils.DebugLog
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode
import org.foss.fermux.ytdlp.logic.downloader.ExternalDownloaders
import org.foss.fermux.ytdlp.logic.downloader.YtdlpChannel
import java.util.concurrent.atomic.AtomicBoolean

class DownloaderSettingsViewModel(application: Application) : AndroidViewModel(application) {
     private val settingsTab = DataStoreDownloaderSettings(application.applicationContext)

     init {
          viewModelScope.launch {
               settingsTab.debug.collect {
                    DebugLog.setEnable(it)
               }
          }
     }

     val downloadPath: StateFlow<String> = settingsTab.downloadPath
          .stateIn(viewModelScope, SharingStarted.Lazily, "")

     val bellState: StateFlow<Boolean> = settingsTab.bellState
          .stateIn(viewModelScope, SharingStarted.Lazily, false)

     val quickJS: StateFlow<Boolean> = settingsTab.quickJS
          .stateIn(viewModelScope, SharingStarted.Lazily, true )

     val debug: StateFlow<Boolean> = settingsTab.debug
          .stateIn(viewModelScope, SharingStarted.Lazily, false)

     val fingerprint: StateFlow<Boolean> = settingsTab.fingerprinting
          .stateIn(viewModelScope, SharingStarted.Lazily, true)

     val sleepRequest: StateFlow<Int> = settingsTab.sleepRequest
          .stateIn(viewModelScope, SharingStarted.Lazily, 0)

     val playlistState: StateFlow<Boolean> = settingsTab.playlistStatus
          .stateIn(viewModelScope, SharingStarted.Lazily, true)

     val aria2cMode: StateFlow<Aria2cMode> = settingsTab.aria2cMode
     .stateIn(viewModelScope, SharingStarted.Lazily, Aria2cMode.Always)

     val externalDownloaders: StateFlow<ExternalDownloaders> = settingsTab.externalDownloaders
          .stateIn(viewModelScope, SharingStarted.Lazily, ExternalDownloaders.TurnedOff)

     val audioHistory: StateFlow<Boolean> = settingsTab.audioHistory
          .stateIn(viewModelScope, SharingStarted.Lazily, true)

     val embedThumbnail: StateFlow<Boolean> = settingsTab.embedThumbnail
          .stateIn(viewModelScope, SharingStarted.Lazily, true)

     val videoHistory: StateFlow<Boolean> = settingsTab.videoHistory
          .stateIn(viewModelScope, SharingStarted.Lazily, true)

     val ytdlpDetails: StateFlow<Boolean> = settingsTab.ytdlpDetails
          .stateIn(viewModelScope, SharingStarted.Lazily, false)

     val sponsorBlock: StateFlow<Boolean> = settingsTab.sponsorBlock
          .stateIn(viewModelScope, SharingStarted.Lazily, true)

     val sponsorBlockCategories: StateFlow<Set<String>> = settingsTab.sponsorBlockCategories
          .stateIn(viewModelScope, SharingStarted.Lazily, setOf("sponsor", "selfpromo", "interaction"))

     val audioHistoryList: StateFlow<List<JSONHistoryCards>> = settingsTab.jsonAudioCard
          .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

     val videoHistoryList: StateFlow<List<JSONHistoryCards>> = settingsTab.jsonVideoCard
          .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

     fun setSleepRequest(value: Int) {
          viewModelScope.launch { settingsTab.setSleepRequest(value) }
     }

     fun setAria2cMode(value: Aria2cMode) {
          viewModelScope.launch { settingsTab.setAria2cMode(value) }
     }

     fun setExternalDownloaders(value: ExternalDownloaders) {
          viewModelScope.launch { settingsTab.setExternalDownloader(value) }
     }

     fun setPlaylistState(value: Boolean) {
          viewModelScope.launch { settingsTab.setPlaylistStatus(value) }
     }

     fun setEmbedThumbnail(value: Boolean) {
          viewModelScope.launch { settingsTab.setEmbedThumbnail(value) }
     }

     fun setAudioHistory(value: Boolean) {
          viewModelScope.launch { settingsTab.setAudioHistory(value) }
     }

     fun setVideoHistory(value: Boolean) {
          viewModelScope.launch { settingsTab.setVideoHistory(value) }
     }

     fun setQuickJS(value: Boolean) {
          viewModelScope.launch { settingsTab.setQuickJS(value) }
     }

     fun setDebug(value: Boolean) {
          DebugLog.setEnable(value)
          viewModelScope.launch { settingsTab.setDebug(value) }
     }

     fun setFingerprint(value: Boolean) {
          viewModelScope.launch { settingsTab.setFingerprinting(value) }
     }

     fun setYtdlpDetails(value: Boolean) {
          viewModelScope.launch { settingsTab.setYtdlpDetails(value) }
     }

     fun setSponsorBlock(value: Boolean) {
          viewModelScope.launch { settingsTab.setSponsorBlock(value) }
     }

     fun setSponsorBlockCategories(value: Set<String>) {
          viewModelScope.launch { settingsTab.setSponsorBlockCategories(value) }
     }

     fun setDownloadPath(value: String) {
          viewModelScope.launch { settingsTab.setDownloadPath(value) }
     }

     fun setBellState(value: Boolean) {
          viewModelScope.launch { settingsTab.setBellState(value) }
     }

     fun setClearYtdlp() {
          viewModelScope.launch { settingsTab.clearYtdlp() }
     }

     private val isUpdatingYtdlp = AtomicBoolean(false)
     private val _isCheckingForUpdate = MutableStateFlow(false)
     val isCheckingForUpdate: StateFlow<Boolean> = _isCheckingForUpdate
     private val _ytdlpUpdateStatus = MutableStateFlow<String?>(null)
     private val _upToDate = MutableStateFlow<Boolean?>(null)
     val upToDate: StateFlow<Boolean?> = _upToDate // TODO. Add this in the UI
     val ytdlpUpdateStatus: StateFlow<String?> = _ytdlpUpdateStatus
     private val _currentVersionName = MutableStateFlow(
          YoutubeDL.getInstance().versionName(getApplication()) ?: "Unknown"
     )
     val currentVersionName: StateFlow<String> = _currentVersionName

     fun checkYtdlpUpdate(channel: YtdlpChannel = YtdlpChannel.Stable) {
          if (!isUpdatingYtdlp.compareAndSet(false, true)) return

          _isCheckingForUpdate.value = true
          _upToDate.value = null

          viewModelScope.launch(Dispatchers.IO) {
               _ytdlpUpdateStatus.value = "Updating yt-dlp..."

               try {
                    val updateChannel = when (channel) {
                         YtdlpChannel.Stable -> YoutubeDL.UpdateChannel.STABLE
                         YtdlpChannel.Nightly -> YoutubeDL.UpdateChannel.NIGHTLY
                         YtdlpChannel.Master -> YoutubeDL.UpdateChannel.MASTER
                    }

                    val result = YoutubeDL.getInstance().updateYoutubeDL(
                         appContext = getApplication(),
                         updateChannel = updateChannel
                    )

                    _ytdlpUpdateStatus.value = when (result) {
                         YoutubeDL.UpdateStatus.DONE -> "yt-dlp updated successfully"
                         YoutubeDL.UpdateStatus.ALREADY_UP_TO_DATE -> "yt-dlp is already up to date"
                         null -> "yt-dlp update completed"
                    }

                    _currentVersionName.value = YoutubeDL.getInstance().versionName(getApplication()) ?: "Unknown"
                    _upToDate.value = true
               } catch (e: Exception) {
                    Log.e("fermuxYtdlpUpdater", "yt-dlp update failed", e)
                    _ytdlpUpdateStatus.value =
                         "Update failed"
                    _upToDate.value = false
               } finally {
                    _isCheckingForUpdate.value = false
                    isUpdatingYtdlp.set(false)
               }
          }
     }
}
