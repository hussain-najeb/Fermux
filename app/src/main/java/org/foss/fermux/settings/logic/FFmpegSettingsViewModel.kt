package org.foss.fermux.settings.logic


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.foss.fermux.storage.FFmpegSettingsTab


class FFmpegSettingsViewModel(application: Application) : AndroidViewModel(application) {

     private val ffmpegSettings = FFmpegSettingsTab(application.applicationContext)


     val audioBitrate: StateFlow<String> = ffmpegSettings.audioBitrate
          .stateIn(viewModelScope, SharingStarted.Lazily, "")
     val normalizeAudio: StateFlow<Boolean> = ffmpegSettings.normalizeAudio
          .stateIn(viewModelScope, SharingStarted.Lazily, false)
     val monoDownmix: StateFlow<Boolean> = ffmpegSettings.monoDownmix
          .stateIn(viewModelScope, SharingStarted.Lazily, false)
     val enableVideoCompression: StateFlow<Boolean> = ffmpegSettings.enableVideoCompression
          .stateIn(viewModelScope, SharingStarted.Lazily, false)
     val videoResolution: StateFlow<String> = ffmpegSettings.videoResolution
          .stateIn(viewModelScope, SharingStarted.Lazily, "")
     val videoCrf: StateFlow<Int> = ffmpegSettings.videoCrf
          .stateIn(viewModelScope, SharingStarted.Lazily, 23)
     val useHardwareEncoder: StateFlow<Boolean> = ffmpegSettings.useHardwareEncoder
          .stateIn(viewModelScope, SharingStarted.Lazily, false)
     val threadLimit: StateFlow<Int> = ffmpegSettings.threadLimit
          .stateIn(viewModelScope, SharingStarted.Lazily, 0)


     fun setAudioBitrate(value: String) {
          viewModelScope.launch { ffmpegSettings.setAudioBitrate(value) }
     }

     fun setNormalizeAudio(value: Boolean) {
          viewModelScope.launch { ffmpegSettings.setNormalizeAudio(value) }
     }

     fun setMonoDownmix(value: Boolean) {
          viewModelScope.launch { ffmpegSettings.setMonoDownmix(value) }
     }

     fun setEnableVideoCompression(value: Boolean) {
          viewModelScope.launch { ffmpegSettings.setEnableVideoCompression(value) }
     }

     fun setVideoResolution(value: String) {
          viewModelScope.launch { ffmpegSettings.setVideoResolution(value) }
     }

     fun setVideoCrf(value: Int) {
          viewModelScope.launch { ffmpegSettings.setVideoCrf(value) }
     }

     fun setUseHardwareEncoder(value: Boolean) {
          viewModelScope.launch { ffmpegSettings.setUseHardwareEncoder(value) }
     }


     fun setThreadLimit(value: Int) {
          viewModelScope.launch { ffmpegSettings.setThreadLimit(value) }
     }


     fun setClearFFmpeg() {
          viewModelScope.launch { ffmpegSettings.clearFFmpeg() }
     }
}