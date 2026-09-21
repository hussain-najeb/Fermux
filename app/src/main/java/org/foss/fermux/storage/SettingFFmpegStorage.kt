package org.foss.fermux.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


interface FFmpegSettingsRepo {

     val audioBitrate: Flow<String>
     val ffmpegDebug: Flow<Boolean>
     val normalizeAudio: Flow<Boolean>
     val monoDownmix: Flow<Boolean>
     val enableVideoCompression: Flow<Boolean>
     val videoResolution: Flow<String>
     val videoCrf: Flow<Int>
     val useHardwareEncoder: Flow<Boolean>
     val threadLimit: Flow<Int>

     suspend fun setAudioBitrate(value: String)
     suspend fun setFFmpegDebug(value: Boolean)
     suspend fun setNormalizeAudio(value: Boolean)
     suspend fun setMonoDownmix(value: Boolean)
     suspend fun setEnableVideoCompression(value: Boolean)
     suspend fun setVideoResolution(value: String)
     suspend fun setVideoCrf(value: Int)
     suspend fun setUseHardwareEncoder(value: Boolean)
     suspend fun setThreadLimit(value: Int)
     suspend fun clearFFmpeg()
}

val AUDIO_BITRATE_KEY = stringPreferencesKey("ffmpeg_audio_bitrate")
val FFMPEG_DEBUG = booleanPreferencesKey("debug_switch")
val NORMALIZE_AUDIO_KEY = booleanPreferencesKey("ffmpeg_normalize_audio")
val MONO_DOWNMIX_KEY = booleanPreferencesKey("ffmpeg_mono_downmix")
val ENABLE_VIDEO_COMPRESSION_KEY = booleanPreferencesKey("ffmpeg_enable_video_compression")
val VIDEO_RESOLUTION_KEY = stringPreferencesKey("ffmpeg_video_resolution")
val VIDEO_CRF_KEY = intPreferencesKey("ffmpeg_video_crf")
val USE_HARDWARE_ENCODER_KEY = booleanPreferencesKey("ffmpeg_hardware_encoder")
val THREAD_LIMIT_KEY = intPreferencesKey("ffmpeg_thread_limit")


class DataStoreFFmpegSettings(private val settingStore: DataStore<Preferences>) : FFmpegSettingsRepo {

     constructor(context: Context) : this(context.dataStore)

     override val audioBitrate: Flow<String> =
          settingStore.data.map { preferences -> preferences[AUDIO_BITRATE_KEY] ?: "" }

     override val ffmpegDebug: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[FFMPEG_DEBUG] ?: false }
     override val normalizeAudio: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[NORMALIZE_AUDIO_KEY] ?: false }
     override val monoDownmix: Flow<Boolean> = settingStore.data.map { it[MONO_DOWNMIX_KEY] ?: false }
     override val enableVideoCompression: Flow<Boolean> =
          settingStore.data.map { it[ENABLE_VIDEO_COMPRESSION_KEY] ?: false }
     override val videoResolution: Flow<String> = settingStore.data.map { it[VIDEO_RESOLUTION_KEY] ?: "" }
     override val videoCrf: Flow<Int> = settingStore.data.map { it[VIDEO_CRF_KEY] ?: 23 }
     override val useHardwareEncoder: Flow<Boolean> = settingStore.data.map { it[USE_HARDWARE_ENCODER_KEY] ?: false }
     override val threadLimit: Flow<Int> = settingStore.data.map { it[THREAD_LIMIT_KEY] ?: 0 }

     override suspend fun setAudioBitrate(value: String) {
          settingStore.edit { preferences -> preferences[AUDIO_BITRATE_KEY] = value }
     }

     override suspend fun setFFmpegDebug(value: Boolean) {
          settingStore.edit { preferences -> preferences[FFMPEG_DEBUG] = value }
     }


     override suspend fun setNormalizeAudio(value: Boolean) {
          settingStore.edit { preferences -> preferences[NORMALIZE_AUDIO_KEY] = value }
     }


     override suspend fun setMonoDownmix(value: Boolean) {
          settingStore.edit { preferences -> preferences[MONO_DOWNMIX_KEY] = value }
     }


     override suspend fun setEnableVideoCompression(value: Boolean) {
          settingStore.edit { preferences -> preferences[ENABLE_VIDEO_COMPRESSION_KEY] = value }
     }


     override suspend fun setVideoResolution(value: String) {
          settingStore.edit { preferences -> preferences[VIDEO_RESOLUTION_KEY] = value }
     }


     override suspend fun setVideoCrf(value: Int) {
          settingStore.edit { preferences -> preferences[VIDEO_CRF_KEY] = value }
     }


     override suspend fun setUseHardwareEncoder(value: Boolean) {
          settingStore.edit { preferences -> preferences[USE_HARDWARE_ENCODER_KEY] = value }
     }


     override suspend fun setThreadLimit(value: Int) {
          settingStore.edit { preferences -> preferences[THREAD_LIMIT_KEY] = value }
     }


     override suspend fun clearFFmpeg() {
          settingStore.edit { preferences ->
               preferences.remove(AUDIO_BITRATE_KEY)
               preferences.remove(FFMPEG_DEBUG)
               preferences.remove(NORMALIZE_AUDIO_KEY)
               preferences.remove(MONO_DOWNMIX_KEY)
               preferences.remove(ENABLE_VIDEO_COMPRESSION_KEY)
               preferences.remove(VIDEO_RESOLUTION_KEY)
               preferences.remove(VIDEO_CRF_KEY)
               preferences.remove(USE_HARDWARE_ENCODER_KEY)
               preferences.remove(THREAD_LIMIT_KEY)
          }
     }
}
