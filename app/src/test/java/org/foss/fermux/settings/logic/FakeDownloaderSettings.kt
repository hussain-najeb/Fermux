package org.foss.fermux.settings.logic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import org.foss.fermux.storage.DownloaderSettingsRepo
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode
import org.foss.fermux.ytdlp.logic.downloader.ExternalDownloaders

class FakeDownloaderSettings : DownloaderSettingsRepo {

     private var _downloadPath = MutableStateFlow("")
     private var _notificationState = MutableStateFlow(true)
     private var _sleepRequest = MutableStateFlow(0)
     private var _embedThumbnail = MutableStateFlow(true)
     private var _quickJS = MutableStateFlow(true)
     private var _fingerprinting = MutableStateFlow(true)
     private var _aria2cMode = MutableStateFlow(Aria2cMode.Always)
     private var _externalDownloaders = MutableStateFlow(ExternalDownloaders.YtdlpNativeDownloader)
     private var _ytdlpDetails = MutableStateFlow(true)
     private var _sponsorBlock = MutableStateFlow(true)
     private var _sponsorBlockCategories = MutableStateFlow(setOf("sponsor", "selfpromo", "interaction"))
     private var _playlistStatus = MutableStateFlow(true)
     private var _audioHistory = MutableStateFlow(true)
     private var _videoHistory = MutableStateFlow(true)
     private val _jsonAudioCard = MutableStateFlow(emptyList<JSONHistoryCards>())
     private val _jsonVideoCard = MutableStateFlow(emptyList<JSONHistoryCards>())


     override val downloadPath: Flow<String> = _downloadPath
     override val notificationState: Flow<Boolean> = _notificationState
     override val sleepRequest: Flow<Int> = _sleepRequest
     override val embedThumbnail: Flow<Boolean> = _embedThumbnail
     override val quickJS: Flow<Boolean> = _quickJS
     override val fingerprinting: Flow<Boolean> = _fingerprinting
     override val aria2cMode: Flow<Aria2cMode> = _aria2cMode
     override val externalDownloaders: Flow<ExternalDownloaders> = _externalDownloaders
     override val ytdlpDetails: Flow<Boolean> = _ytdlpDetails
     override val sponsorBlock: Flow<Boolean> = _sponsorBlock
     override val sponsorBlockCategories: Flow<Set<String>> = _sponsorBlockCategories
     override val playlistStatus: Flow<Boolean> = _playlistStatus
     override val audioHistory: Flow<Boolean> = _audioHistory
     override val videoHistory: Flow<Boolean> = _videoHistory
     override val jsonAudioCard: Flow<List<JSONHistoryCards>> = _jsonAudioCard
     override val jsonVideoCard: Flow<List<JSONHistoryCards>> = _jsonVideoCard

     override suspend fun setDownloadPath(value: String) {
         _downloadPath.value = value
     }

     override suspend fun setNotificationState(value: Boolean) {
          _notificationState.value = value
     }

     override suspend fun setSleepRequest(value: Int) {
          _sleepRequest.value = value
     }

     override suspend fun setAria2cMode(value: Aria2cMode) {
          _aria2cMode.value = value
     }

     override suspend fun setExternalDownloader(value: ExternalDownloaders) {
          _externalDownloaders.value = value
     }

     override suspend fun setQuickJS(value: Boolean) {
          _quickJS.value = value
     }

     override suspend fun setFingerprinting(value: Boolean) {
          _fingerprinting.value = value
     }

     override suspend fun setEmbedThumbnail(value: Boolean) {
          _embedThumbnail.value = value
     }

     override suspend fun setAudioHistory(value: Boolean) {
          _audioHistory.value = value
     }

     override suspend fun setVideoHistory(value: Boolean) {
          _videoHistory.value = value
     }

     override suspend fun setPlaylistStatus(value: Boolean) {
          _playlistStatus.value = value
     }

     override suspend fun setYtdlpDetails(value: Boolean) {
          _ytdlpDetails.value = value
     }

     override suspend fun setSponsorBlock(value: Boolean) {
          _sponsorBlock.value = value
     }

     override suspend fun setSponsorBlockCategories(value: Set<String>) {
          _sponsorBlockCategories.value = value
     }

     override suspend fun setJSONAudio(value: JSONHistoryCards) {
          _jsonAudioCard.value += value
     }

     override suspend fun setJSONVideo(value: JSONHistoryCards) {
          _jsonVideoCard.value += value
     }

     override suspend fun clearYtdlp() {
          _jsonVideoCard.value = listOf()
          _jsonAudioCard.value = listOf()
          _sponsorBlockCategories = MutableStateFlow(setOf("sponsor", "selfpromo", "interaction"))
          _sponsorBlock = MutableStateFlow(true)
          _ytdlpDetails = MutableStateFlow(true)
          _playlistStatus = MutableStateFlow(true)
          _audioHistory = MutableStateFlow(true)
          _videoHistory = MutableStateFlow(true)
          _embedThumbnail = MutableStateFlow(true)
          _fingerprinting = MutableStateFlow(true)
          _quickJS = MutableStateFlow(true)
          _externalDownloaders = MutableStateFlow(ExternalDownloaders.YtdlpNativeDownloader)
          _sleepRequest = MutableStateFlow(1)
          _notificationState = MutableStateFlow(true)
          _aria2cMode = MutableStateFlow(Aria2cMode.Always)
          _downloadPath = MutableStateFlow("")
     }

}