package org.foss.fermux.settings.logic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsTab = SettingsTab(application.applicationContext)

    val downloadPath: StateFlow<String> = settingsTab.downloadPath
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val notificationState: StateFlow<Boolean> = settingsTab.notificationState
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val audioHistory: StateFlow<Boolean> = settingsTab.audioHistory
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val videoHistory: StateFlow<Boolean> = settingsTab.videoHistory
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val ytdlpDetails: StateFlow<Boolean> = settingsTab.ytdlpDetails
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val sponserBlock: StateFlow<Boolean> = settingsTab.sponserBlock
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val ytdlpUpdater: StateFlow<String> = settingsTab.ytdlpUpdater
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")

    val language: StateFlow<String> = settingsTab.language
        .stateIn(viewModelScope, SharingStarted.Eagerly, "")



    fun setDownloadPath(value: String) {
        viewModelScope.launch {settingsTab.setDownloadPath(value)}
    }

    fun setNotificationState(value: Boolean) {
        viewModelScope.launch {settingsTab.setNotificationState(value)}
    }

    fun setAudioHistory(value: Boolean) {
        viewModelScope.launch {settingsTab.setAudioHistory(value)}
    }

    fun setVideoHistory(value: Boolean) {
        viewModelScope.launch {settingsTab.setVideoHistory(value)}
    }

    fun setYtdlpDetails(value: Boolean) {
        viewModelScope.launch {settingsTab.setYtdlpDetails(value)}
    }

    fun setSponsorBlock(value: Boolean) {
        viewModelScope.launch {settingsTab.setSponsorBlock(value)}
    }

    fun setYtdlpUpdater(value: String) {
        viewModelScope.launch {settingsTab.setYtdlpUpdater(value)}
    }

    fun setLanguage(value: String) {
        viewModelScope.launch {settingsTab.setLanguage(value)}
    }
}



