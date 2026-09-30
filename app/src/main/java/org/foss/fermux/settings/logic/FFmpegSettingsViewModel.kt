package org.foss.fermux.settings.logic


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.foss.fermux.storage.DataStoreFFmpegSettings
import org.foss.fermux.storage.FFmpegSettingsRepo
import org.foss.fermux.storage.FFmpegSettingsSnapshot
import org.foss.fermux.utils.DebugLogDownloader


class FFmpegSettingsViewModel(application: Application) : AndroidViewModel(application) {

     private val ffmpegSettings: FFmpegSettingsRepo = DataStoreFFmpegSettings(application.applicationContext)


     val audioBitrate: StateFlow<String> = ffmpegSettings.audioBitrate
          .stateIn(viewModelScope, SharingStarted.Lazily, "")

     val ffmpegBellState: StateFlow<Boolean> = ffmpegSettings.ffmpegBellState
          .stateIn(viewModelScope, SharingStarted.Lazily, false)
     val ffmpegDebug: StateFlow<Boolean> = ffmpegSettings.ffmpegDebug
          .stateIn(viewModelScope, SharingStarted.Lazily, false)
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

     fun setFFmpegBellState(value: Boolean) {
          viewModelScope.launch { ffmpegSettings.setFFmpegBellState(value) }
     }

     fun setFFmpegDebug(value: Boolean) {
          DebugLogDownloader.setEnable(value)
          viewModelScope.launch { ffmpegSettings.setFFmpegDebug(value) }
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


     suspend fun resetFFmpegSettings(): FFmpegSettingsSnapshot {
          return ffmpegSettings.resetFFmpeg()
     }

     suspend fun restoreFFmpegSettings(snapshot: FFmpegSettingsSnapshot) {
          ffmpegSettings.restoreFFmpeg(snapshot)
     }
}
