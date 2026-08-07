@file:Suppress("SpellCheckingInspection")

package org.foss.fermux.storage

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings_tab")

// ytdlp downloader tab.
val DOWNLOAD_PATH = stringPreferencesKey("download_path")
val DOWNLOAD_PROGRESS_NOTIFICATION = booleanPreferencesKey("download_progress_notification")
val SLEEP_REQUEST_KEY = intPreferencesKey("sleep_request_seconds")
val ARIA2C_KEY = booleanPreferencesKey("aria2c_implementation")
val ARIA2C_EDGE_CASE = booleanPreferencesKey("aria2c_implementation_edge_case")
val DOWNLOADING_DETAILS = booleanPreferencesKey("download_details")
val SHOW_YTDLP_VIDEO_HISTORY = booleanPreferencesKey("video_history")
val SHOW_YTDLP_AUDIO_HISTORY = booleanPreferencesKey("audio_history")
val SPONSOR_BLOCK_IMPLEMENTATION = booleanPreferencesKey("sponsor_block")
val DEFAULT_SPONSOR_BLOCK_CATEGORIES = setOf("sponsor", "selfpromo", "interaction")
val SPONSOR_BLOCK_CATEGORIES = stringSetPreferencesKey("sponsor_block_categories")
val JSON_AUDIO_HISTORY = stringPreferencesKey("json_audio")
val JSON_VIDEO_HISTORY = stringPreferencesKey("json_video")

@Suppress("PropertyName")
class SettingsTab(private val context: Context) {

    val downloadPath:      Flow<String> = context.dataStore.data.map { preferences -> preferences[DOWNLOAD_PATH] ?: "" }

    val notificationState: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[DOWNLOAD_PROGRESS_NOTIFICATION] ?: true }

    val sleepRequest: Flow<Int?> = context.dataStore.data.map { preferences -> preferences[SLEEP_REQUEST_KEY] ?: 1}
    val aria2c: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[ARIA2C_KEY] ?: true }

    val aria2cHLSWithDASHCase: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[ARIA2C_EDGE_CASE] ?: false }

    val audioHistory:      Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[SHOW_YTDLP_AUDIO_HISTORY] ?: true }

    val videoHistory:      Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[SHOW_YTDLP_VIDEO_HISTORY] ?: true }

    val ytdlpDetails:      Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[DOWNLOADING_DETAILS] ?: true }

    val sponsorBlock:      Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[SPONSOR_BLOCK_IMPLEMENTATION] ?: false }
    val sponsorBlockCategories: Flow<Set<String>> =  context.dataStore.data.map { preferences -> preferences[SPONSOR_BLOCK_CATEGORIES] ?: DEFAULT_SPONSOR_BLOCK_CATEGORIES }

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

    @SuppressLint("SuspiciousIndentation")
    suspend fun sleepRequest (value: Int?) {
        if (value != null)
        context.dataStore.edit { preferences -> preferences[SLEEP_REQUEST_KEY] ?: 1 }
    }

    suspend fun aria2cImpl (value: Boolean) {
        context.dataStore.edit { preferences -> preferences[ARIA2C_KEY] = value }
    }

    suspend fun aria2cImplEdgeCase (value: Boolean) {
        context.dataStore.edit { preferences -> preferences[ARIA2C_EDGE_CASE] = value }
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

    suspend fun setSponsorBlockCategories(value: Set<String>) {
       context.dataStore.edit { preferences -> preferences[SPONSOR_BLOCK_CATEGORIES] = value }
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





