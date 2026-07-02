package org.foss.fermux.settings.logic

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.foss.fermux.ytdlp.logic.JSONHistoryCards

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings_tab")

// ytdlp downloader tab.
val DOWNLOAD_PATH = stringPreferencesKey("download_path")
val DOWNLOAD_PROGRESS_NOTIFICATION = booleanPreferencesKey("download_progress_notification")
val DOWNLOADING_DETAILS = booleanPreferencesKey("download_details")
val SHOW_YTDLP_VIDEO_HISTORY = booleanPreferencesKey("video_history")
val SHOW_YTDLP_AUDIO_HISTORY = booleanPreferencesKey("audio_history")
val SPONSOR_BLOCK_IMPLEMENTATION = booleanPreferencesKey("sponsor_block") // TODO. feature for later.
val YTDLP_AUTO_UPDATER = booleanPreferencesKey("update_download_ytp") // TODO. feature for later as well.
val LANGUAGE = stringPreferencesKey("language")

val JSON_AUDIO_HISTORY = stringPreferencesKey("json_audio")

val JSON_VIDEO_HISTORY = stringPreferencesKey("json_video")



//
class SettingsTab(private val context: Context) {

    val downloadPath:      Flow<String> = context.dataStore.data.map { preferences -> preferences[DOWNLOAD_PATH] ?: "" }

    val notificationState: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[DOWNLOAD_PROGRESS_NOTIFICATION] ?: true }

    val audioHistory:      Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[SHOW_YTDLP_AUDIO_HISTORY] ?: true }

    val videoHistory:      Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[SHOW_YTDLP_VIDEO_HISTORY] ?: true }

    val ytdlpDetails:      Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[DOWNLOADING_DETAILS] ?: true }

    val sponserBlock:      Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[SPONSOR_BLOCK_IMPLEMENTATION] ?: true } // TODO. later.

    val ytdlpUpdater:      Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[YTDLP_AUTO_UPDATER] ?: false } // TODO. later.

    val language:          Flow<String> = context.dataStore.data.map { preferences -> preferences[LANGUAGE] ?: "" }

    val JSONAudioCard:     Flow<List<JSONHistoryCards>> = context.dataStore.data.map { preferences -> val json =
        preferences[JSON_AUDIO_HISTORY] ?: "[]"
        Json.decodeFromString<List<JSONHistoryCards>>(json)}

    val JSONVideoCard:     Flow<List<JSONHistoryCards>> = context.dataStore.data.map { preferences -> val json =
        preferences[JSON_VIDEO_HISTORY] ?: "[]"
        Json.decodeFromString<List<JSONHistoryCards>>(json)}

    suspend fun setDownloadPath (value: String) {
        context.dataStore.edit { preferences -> preferences[DOWNLOAD_PATH] = value}
    }

    suspend fun setNotificationState (value: Boolean) {
        context.dataStore.edit { preferences -> preferences[DOWNLOAD_PROGRESS_NOTIFICATION] = value }
    }

    suspend fun setAudioHistory(value: Boolean) {
        context.dataStore.edit { preferences -> preferences[SHOW_YTDLP_AUDIO_HISTORY] = value }
    }

    suspend fun setVideoHistory(value: Boolean) {
        context.dataStore.edit { preferences -> preferences[SHOW_YTDLP_VIDEO_HISTORY] = value }
    }

    suspend fun setYtdlpDetails(value: Boolean) {
        context.dataStore.edit { preferences -> preferences[DOWNLOADING_DETAILS] = value }
    }

    suspend fun setSponsorBlock(value: Boolean) {
        context.dataStore.edit { preferences -> preferences[SPONSOR_BLOCK_IMPLEMENTATION] = value }
    }

    suspend fun setYtdlpUpdater(value: Boolean) {
        context.dataStore.edit { preferences -> preferences[YTDLP_AUTO_UPDATER] = value }
    }

    suspend fun setLanguage(value: String) {
        context.dataStore.edit { preferences -> preferences[LANGUAGE] = value }
    }

    suspend fun setJSONAudio(value: JSONHistoryCards) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[JSON_AUDIO_HISTORY] ?: "[]"
            val currentList = Json.decodeFromString<List<JSONHistoryCards>>(currentJson)
            val updatedList = currentList + value
            preferences[JSON_AUDIO_HISTORY] = Json.encodeToString(updatedList)
        }
    }

    suspend fun setJSONVideo(value: JSONHistoryCards) {
        context.dataStore.edit { preferences ->
            val currentJson = preferences[JSON_VIDEO_HISTORY] ?: "[]"
            val currentList = Json.decodeFromString<List<JSONHistoryCards>>(currentJson)
            val updatedList = currentList + value
            preferences[JSON_VIDEO_HISTORY] = Json.encodeToString(updatedList)
        }
    }
}





