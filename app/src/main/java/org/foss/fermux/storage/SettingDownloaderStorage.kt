@file:Suppress("SpellCheckingInspection")

package org.foss.fermux.storage

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode
import kotlinx.serialization.json.Json

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings_tab")

// ytdlp downloader tab.
val DOWNLOAD_PATH = stringPreferencesKey("download_path")
val DOWNLOAD_PROGRESS_NOTIFICATION = booleanPreferencesKey("download_progress_notification")
val SLEEP_REQUEST_KEY = intPreferencesKey("sleep_request_seconds")
val ARIA2C_MODE_KEY = stringPreferencesKey("aria2c_mode")
val DOWNLOADING_DETAILS = booleanPreferencesKey("download_details")
val SHOW_YTDLP_VIDEO_HISTORY = booleanPreferencesKey("video_history")
val QUICK_JS = booleanPreferencesKey("quick js")
val FINGERPRINT = booleanPreferencesKey("fingerprint")
val SHOW_YTDLP_AUDIO_HISTORY = booleanPreferencesKey("audio_history")
val EMBED_THUMBNAIL = booleanPreferencesKey("embed_thumbnail")
val PLAYLIST_STATUS = booleanPreferencesKey("playlist_status")
val SPONSOR_BLOCK_IMPLEMENTATION = booleanPreferencesKey("sponsor_block")
val DEFAULT_SPONSOR_BLOCK_CATEGORIES = setOf("sponsor", "selfpromo", "interaction")
val SPONSOR_BLOCK_CATEGORIES = stringSetPreferencesKey("sponsor_block_categories")
val JSON_AUDIO_HISTORY = stringPreferencesKey("json_audio")
val JSON_VIDEO_HISTORY = stringPreferencesKey("json_video")

@Suppress("PropertyName")
class DownloaderSettingsTab(private val context: Context) {

     val downloadPath: Flow<String> = context.dataStore.data.map { preferences -> preferences[DOWNLOAD_PATH] ?: "" }
     val notificationState: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[DOWNLOAD_PROGRESS_NOTIFICATION] ?: true }
     val sleepRequest: Flow<Int> = context.dataStore.data.map { preferences -> preferences[SLEEP_REQUEST_KEY] ?: 0 }
     val embedThumbnail: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[EMBED_THUMBNAIL] ?: true }
     val quickJS: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[QUICK_JS] ?: true }
     val fingerprinting: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[FINGERPRINT] ?: true }
     val aria2cMode: Flow<Aria2cMode> = context.dataStore.data.map { preferences ->
          preferences[ARIA2C_MODE_KEY]
               ?.let { runCatching { Aria2cMode.valueOf(it) }.getOrNull() }
               ?: Aria2cMode.Always
     }
     val audioHistory: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[SHOW_YTDLP_AUDIO_HISTORY] ?: true }
     val videoHistory: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[SHOW_YTDLP_VIDEO_HISTORY] ?: true }
     val ytdlpDetails: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[DOWNLOADING_DETAILS] ?: true }
     val sponsorBlock: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[SPONSOR_BLOCK_IMPLEMENTATION] ?: true }
     val playlistStatus: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[PLAYLIST_STATUS] ?: false }
     val sponsorBlockCategories: Flow<Set<String>> = context.dataStore.data.map { preferences ->
          preferences[SPONSOR_BLOCK_CATEGORIES] ?: DEFAULT_SPONSOR_BLOCK_CATEGORIES
     }
     val JSONAudioCard: Flow<List<JSONHistoryCards>> = context.dataStore.data.map { preferences ->
          val json =
               preferences[JSON_AUDIO_HISTORY] ?: "[]"
          Json.decodeFromString<List<JSONHistoryCards>>(json)
     }

     val JSONVideoCard: Flow<List<JSONHistoryCards>> = context.dataStore.data.map { preferences ->
          val json =
               preferences[JSON_VIDEO_HISTORY] ?: "[]"
          Json.decodeFromString<List<JSONHistoryCards>>(json)
     }

     suspend fun setDownloadPath(value: String) {
          context.dataStore.edit { preferences -> preferences[DOWNLOAD_PATH] = value }
     }

     suspend fun setNotificationState(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[DOWNLOAD_PROGRESS_NOTIFICATION] = value }
     }

     @SuppressLint("SuspiciousIndentation")
     suspend fun setSleepRequest(value: Int) {
          context.dataStore.edit { preferences -> preferences[SLEEP_REQUEST_KEY] = value }
     }

     suspend fun setAria2cMode(value: Aria2cMode) {
          context.dataStore.edit { preferences -> preferences[ARIA2C_MODE_KEY] = value.name }
     }

     suspend fun setQuickJS(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[QUICK_JS] = value }
     }

     suspend fun setFingerprinting(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[FINGERPRINT] = value }     
     }

     suspend fun setEmbedThumbnail(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[EMBED_THUMBNAIL] = value }
     }

     suspend fun setAudioHistory(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[SHOW_YTDLP_AUDIO_HISTORY] = value }
     }

     suspend fun setPlaylistStatus(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[PLAYLIST_STATUS] = value }
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

     suspend fun clearYtdlp() {
          context.dataStore.edit { preferences ->
               preferences.remove(DOWNLOAD_PATH)
               preferences.remove(DOWNLOAD_PROGRESS_NOTIFICATION)
               preferences.remove(SLEEP_REQUEST_KEY)
               preferences.remove(ARIA2C_MODE_KEY)
               preferences.remove(DOWNLOADING_DETAILS)
               preferences.remove(SHOW_YTDLP_VIDEO_HISTORY)
               preferences.remove(SHOW_YTDLP_AUDIO_HISTORY)
               preferences.remove(EMBED_THUMBNAIL)
               preferences.remove(PLAYLIST_STATUS)
               preferences.remove(SPONSOR_BLOCK_IMPLEMENTATION)
               preferences.remove(SPONSOR_BLOCK_CATEGORIES)
               preferences.remove(QUICK_JS)
               preferences.remove(FINGERPRINT)
          }
     }
}