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
import org.foss.fermux.utils.DebugLogDownloader
import org.foss.fermux.ytdlp.logic.downloader.*
import java.util.concurrent.atomic.AtomicBoolean

class DownloaderSettingsViewModel(application: Application) : AndroidViewModel(application) {
     private val settingsTab = DataStoreDownloaderSettings(application.applicationContext)

     init {
          viewModelScope.launch {
               settingsTab.downloaderDebug.collect {
                    DebugLogDownloader.setEnable(it)
               }
          }
     }

     val downloadPath: StateFlow<String> = settingsTab.downloadPath
          .stateIn(viewModelScope, SharingStarted.Lazily, "")

     val downloaderBellState: StateFlow<Boolean> = settingsTab.downloaderBellState
          .stateIn(viewModelScope, SharingStarted.Lazily, false)

     val quickJS: StateFlow<Boolean> = settingsTab.quickJS
          .stateIn(viewModelScope, SharingStarted.Lazily, true )

     val downloaderDebug: StateFlow<Boolean> = settingsTab.downloaderDebug
          .stateIn(viewModelScope, SharingStarted.Lazily, false)

     val fingerprint: StateFlow<Boolean> = settingsTab.fingerprinting
          .stateIn(viewModelScope, SharingStarted.Lazily, true)

     val sleepRequest: StateFlow<Int> = settingsTab.sleepRequest
          .stateIn(viewModelScope, SharingStarted.Lazily, 0)

     val playlistState: StateFlow<Boolean> = settingsTab.playlistStatus
          .stateIn(viewModelScope, SharingStarted.Lazily, true)

     val aria2cMode: StateFlow<Aria2cMode> = settingsTab.aria2cMode
     .stateIn(viewModelScope, SharingStarted.Lazily, Aria2cMode.Always)

     val thumbnailFormat: StateFlow<ThumbnailFormat> = settingsTab.thumbnailFormat
          .stateIn(viewModelScope, SharingStarted.Lazily, ThumbnailFormat.Png)

     val audioFormats: StateFlow<AudioFormat> = settingsTab.audioFormat
          .stateIn(viewModelScope, SharingStarted.Lazily, AudioFormat.Mp3Format)

     val videoFormats: StateFlow<VideoFormat> = settingsTab.videoFormat
          .stateIn(viewModelScope, SharingStarted.Lazily, VideoFormat.Mp4Format)

     val videoComp: StateFlow<Boolean> = settingsTab.videoComp
          .stateIn(viewModelScope, SharingStarted.Lazily, false)

     val externalDownloaders: StateFlow<ExternalDownloaders> = settingsTab.externalDownloaders
          .stateIn(viewModelScope, SharingStarted.Lazily, ExternalDownloaders.Disabled)

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

     val upToDate: StateFlow<Boolean> = settingsTab.upToDate
          .stateIn(viewModelScope, SharingStarted.Lazily, false)

     val ytdlpChannel: StateFlow<YtdlpChannel> = settingsTab.ytdlpChannel
          .stateIn(viewModelScope, SharingStarted.Lazily, YtdlpChannel.Nightly)

     val wifi: StateFlow<Connectivity> = settingsTab.wifi
          .stateIn(viewModelScope, SharingStarted.Lazily, Connectivity.Any)

     val ipv: StateFlow<IpvConnection> = settingsTab.ipvConnection
          .stateIn(viewModelScope, SharingStarted.Lazily, IpvConnection.Disabled)

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

     fun setThumbnailFormat(value: ThumbnailFormat) {
          viewModelScope.launch { settingsTab.setThumbnail(value) }
     }

     fun setAudioFormat(value: AudioFormat) {
          viewModelScope.launch { settingsTab.setAudioFormat(value) }
     }

     fun setVideoFormat(value: VideoFormat) {
          viewModelScope.launch { settingsTab.setVideoFormat(value) }
     }

     fun setVideoComp(value: Boolean) {
          viewModelScope.launch { settingsTab.setVideoComp(value) }
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

     fun setDownloaderDebug(value: Boolean) {
          DebugLogDownloader.setEnable(value)
          viewModelScope.launch { settingsTab.setDownloaderDebug(value) }
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

     fun setDownloaderBellState(value: Boolean) {
          viewModelScope.launch { settingsTab.setDownloaderBellState(value) }
     }

     fun setUpToDate(value: Boolean) {
          viewModelScope.launch { settingsTab.setUpToDate(value) }
     }

     fun setYtdlpChannel(value: YtdlpChannel) {
          viewModelScope.launch { settingsTab.setYtdlpChannel(value) }
     }

     fun setWifi(value: Connectivity) {
          viewModelScope.launch { settingsTab.setWifi(value) }
     }

     fun setIpvConnection(value: IpvConnection) {
          viewModelScope.launch { settingsTab.setIpvConnection(value) }
     }

     fun clearHistory() {
          viewModelScope.launch { settingsTab.clearHistory() }
     }

     fun clearYtdlp() {
          viewModelScope.launch { settingsTab.clearYtdlp() }
     }

     fun clearArgs() {
          viewModelScope.launch { settingsTab.clearArgs() }
     }

     private val isUpdatingYtdlp = AtomicBoolean(false)

     private val _isCheckingForUpdate = MutableStateFlow(false)

     val isCheckingForUpdate: StateFlow<Boolean> = _isCheckingForUpdate

     private val _ytdlpUpdateStatus = MutableStateFlow<String?>(null)

     val ytdlpUpdateStatus: StateFlow<String?> = _ytdlpUpdateStatus

     private val _currentVersionName = MutableStateFlow(YoutubeDL.getInstance().versionName(getApplication()) ?: "Unknown")

     val currentVersionName: StateFlow<String> = _currentVersionName


     fun checkYtdlpUpdate(channel: YtdlpChannel) {
          if (!isUpdatingYtdlp.compareAndSet(false, true)) return

          _isCheckingForUpdate.value = true

          viewModelScope.launch(Dispatchers.IO) {
               _ytdlpUpdateStatus.value = "Updating yt-dlp..."

               try {
                    settingsTab.setYtdlpChannel(channel)

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
                         YoutubeDL.UpdateStatus.DONE -> {
                              settingsTab.setUpToDate(true)
                              "yt-dlp updated successfully"
                         }
                         YoutubeDL.UpdateStatus.ALREADY_UP_TO_DATE -> {
                              settingsTab.setUpToDate(true)
                              "yt-dlp is already up to date"
                         }
                         null -> {
                              settingsTab.setUpToDate(false)
                              "dlp update completed with an unknown result"
                         }
                    }

                    _currentVersionName.value = YoutubeDL.getInstance().versionName(getApplication()) ?: "Unknown"
               } catch (e: Exception) {
                    Log.e("fermuxYtdlpUpdater", "yt-dlp update failed", e)
                    _ytdlpUpdateStatus.value =
                         "Update failed"
               } finally {
                    _isCheckingForUpdate.value = false
                    isUpdatingYtdlp.set(false)
               }
          }
     }
}
