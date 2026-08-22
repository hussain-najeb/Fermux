package org.foss.fermux.storage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey


val AUDIO_BITRATE_KEY = stringPreferencesKey("ffmpeg_audio_bitrate")
val NORMALIZE_AUDIO_KEY = booleanPreferencesKey("ffmpeg_normalize_audio")
val MONO_DOWNMIX_KEY = booleanPreferencesKey("ffmpeg_mono_downmix")
val ENABLE_VIDEO_COMPRESSION_KEY = booleanPreferencesKey("ffmpeg_enable_video_compression")
val VIDEO_RESOLUTION_KEY = stringPreferencesKey("ffmpeg_video_resolution")
val VIDEO_CRF_KEY = intPreferencesKey("ffmpeg_video_crf")
val USE_HARDWARE_ENCODER_KEY = booleanPreferencesKey("ffmpeg_hardware_encoder")
val THREAD_LIMIT_KEY = intPreferencesKey("ffmpeg_thread_limit") 



class FFmpegSettingsTab(private val context: Context) {

	val audioBitrate: Flow<String> = context.dataStore.data.map { preferences -> preferences[AUDIO_BITRATE_KEY] ?: "" }
	val normalizeAudio: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[NORMALIZE_AUDIO_KEY] ?: false }
	val monoDownmix: Flow<Boolean> = context.dataStore.data.map { it[MONO_DOWNMIX_KEY] ?: false }
    val enableVideoCompression: Flow<Boolean> = context.dataStore.data.map { it[ENABLE_VIDEO_COMPRESSION_KEY] ?: false }
    val videoResolution: Flow<String> = context.dataStore.data.map { it[VIDEO_RESOLUTION_KEY] ?: "" }
    val videoCrf: Flow<Int> = context.dataStore.data.map { it[VIDEO_CRF_KEY] ?: 23 }
    val useHardwareEncoder: Flow<Boolean> = context.dataStore.data.map { it[USE_HARDWARE_ENCODER_KEY] ?: false }
    val threadLimit: Flow<Int> = context.dataStore.data.map { it[THREAD_LIMIT_KEY] ?: 0 }

	suspend fun setAudioBitrate(value: String) {
		context.dataStore.edit { preferences -> preferences[AUDIO_BITRATE_KEY] = value }
	}


    suspend fun setNormalizeAudio(value: Boolean) {
    	context.dataStore.edit { preferences -> preferences[NORMALIZE_AUDIO_KEY] = value }
    }


    suspend fun setMonoDownmix(value: Boolean) {
    	context.dataStore.edit { preferences -> preferences[MONO_DOWNMIX_KEY] = value }
    }


    suspend fun setEnableVideoCompression(value: Boolean) {
    	context.dataStore.edit { preferences -> preferences[ENABLE_VIDEO_COMPRESSION_KEY] = value }
    }


    suspend fun setVideoResolution(value: String) {
    	context.dataStore.edit { preferences -> preferences[VIDEO_RESOLUTION_KEY] = value }
    }


    suspend fun setVideoCrf(value: Int) {
    	context.dataStore.edit { preferences -> preferences[VIDEO_CRF_KEY] = value }
    }


    suspend fun setUseHardwareEncoder(value: Boolean) {
    	context.dataStore.edit { preferences -> preferences[USE_HARDWARE_ENCODER_KEY] = value }
    }


    suspend fun setThreadLimit(value: Int) {
    	context.dataStore.edit { preferences -> preferences[THREAD_LIMIT_KEY] = value }
    }
}