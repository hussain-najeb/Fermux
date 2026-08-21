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
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.storage.SettingsTab
import java.util.concurrent.atomic.AtomicBoolean

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val settingsTab = SettingsTab(application.applicationContext)

    val downloadPath: StateFlow<String> = settingsTab.downloadPath
        .stateIn(viewModelScope, SharingStarted.Lazily, "")

    val notificationState: StateFlow<Boolean> = settingsTab.notificationState
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val sleepRequest: StateFlow<Int> = settingsTab.sleepRequest
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val aria2c: StateFlow<Boolean> = settingsTab.aria2c
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val aria2cEdgeCase: StateFlow<Boolean> = settingsTab.aria2cHLSWithDASHCase
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val audioHistory: StateFlow<Boolean> = settingsTab.audioHistory
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val embedThumbnail: StateFlow<Boolean> = settingsTab.embedThumbnail
        .stateIn(viewModelScope, started = SharingStarted.Lazily, initialValue = true)

    val videoHistory: StateFlow<Boolean> = settingsTab.videoHistory
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val ytdlpDetails: StateFlow<Boolean> = settingsTab.ytdlpDetails
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    val sponsorBlock: StateFlow<Boolean> = settingsTab.sponsorBlock
        .stateIn(viewModelScope, SharingStarted.Lazily, true)

    val sponsorBlockCategories: StateFlow<Set<String>> = settingsTab.sponsorBlockCategories
        .stateIn(viewModelScope, SharingStarted.Lazily, setOf("sponsor", "selfpromo", "interaction"))

    val audioHistoryList: StateFlow<List<JSONHistoryCards>> = settingsTab.JSONAudioCard
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val videoHistoryList: StateFlow<List<JSONHistoryCards>> = settingsTab.JSONVideoCard
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun setNotificationState(value: Boolean) {
        viewModelScope.launch { settingsTab.setNotificationState(value) }
    }

    fun setSleepRequest(value: Int) {
        viewModelScope.launch { settingsTab.setSleepRequest(value) }
    }

    fun setAria2cImpl(value: Boolean) {
        viewModelScope.launch { settingsTab.setAria2cImpl(value) }
    }

    fun setAria2cEdgeCase(value: Boolean) {
        viewModelScope.launch { settingsTab.setAria2cEdgeCase(value) }
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

    private val isUpdatingYtdlp = AtomicBoolean(false)
    private val _isCheckingForUpdate = MutableStateFlow(false)
    val isCheckingForUpdate: StateFlow<Boolean> = _isCheckingForUpdate
    private val _ytdlpUpdateStatus = MutableStateFlow<String?>(null)
    private val _upToDate = MutableStateFlow<Boolean?>(null)
    val upToDate: StateFlow<Boolean?> = _upToDate
    val ytdlpUpdateStatus: StateFlow<String?> = _ytdlpUpdateStatus
    val currentVersionName = YoutubeDL.getInstance().versionName(getApplication())
    fun checkYtdlpUpdate() {
        if (!isUpdatingYtdlp.compareAndSet(false, true)) return
        _isCheckingForUpdate.value = true
        viewModelScope.launch(Dispatchers.IO) {
            _ytdlpUpdateStatus.value = "Checking for update..."
            try {
                YoutubeDL.getInstance().updateYoutubeDL(
                    appContext = getApplication(),
                    updateChannel = YoutubeDL.UpdateChannel.STABLE
                )

                _ytdlpUpdateStatus.value = "yt-dlp is up to date"
                _upToDate.value = true
            } catch (e: Exception) {
                Log.e("fermuxYtdlpUpdater", "yt-dlp update failed", e)
                _ytdlpUpdateStatus.value = "Update check failed"
                _upToDate.value = false
            } finally {
                _isCheckingForUpdate.value = false
                isUpdatingYtdlp.set(false)
            }
        }
    }

}