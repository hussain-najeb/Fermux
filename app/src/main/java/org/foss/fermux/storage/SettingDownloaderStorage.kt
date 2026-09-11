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
import org.foss.fermux.ytdlp.logic.downloader.ExternalDownloaders


interface DownloaderSettings {

     val downloadPath: Flow<String>
     val notificationState: Flow<Boolean>
     val sleepRequest: Flow<Int>
     val embedThumbnail: Flow<Boolean>
     val quickJS: Flow<Boolean>
     val fingerprinting: Flow<Boolean>
     val aria2cMode: Flow<Aria2cMode>
     val externalDownloaders: Flow<ExternalDownloaders>
     val ytdlpDetails: Flow<Boolean>
     val sponsorBlock: Flow<Boolean>
     val playlistStatus: Flow<Boolean>
     val sponsorBlockCategories: Flow<Set<String>>
     val audioHistory: Flow<Boolean>
     val videoHistory: Flow<Boolean>
     val jsonAudioCard: Flow<List<JSONHistoryCards>>
     val jsonVideoCard: Flow<List<JSONHistoryCards>>

     suspend fun setDownloadPath(value: String)
     suspend fun setNotificationState(value: Boolean)
     suspend fun setSleepRequest(value: Int)
     suspend fun setAria2cMode(value: Aria2cMode)
     suspend fun setExternalDownloader(value: ExternalDownloaders)
     suspend fun setQuickJS(value: Boolean)
     suspend fun setFingerprinting(value: Boolean)
     suspend fun setEmbedThumbnail(value: Boolean)
     suspend fun setAudioHistory(value: Boolean)
     suspend fun setVideoHistory(value: Boolean)
     suspend fun setPlaylistStatus(value: Boolean)
     suspend fun setYtdlpDetails(value: Boolean)
     suspend fun setSponsorBlock(value: Boolean)
     suspend fun setSponsorBlockCategories(value: Set<String>)
     suspend fun setJSONAudio(value: JSONHistoryCards)
     suspend fun setJSONVideo(value: JSONHistoryCards)
     suspend fun clearYtdlp()
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings_tab")

// ytdlp downloader tab.
val DOWNLOAD_PATH = stringPreferencesKey("download_path")
val DOWNLOAD_PROGRESS_NOTIFICATION = booleanPreferencesKey("download_progress_notification")
val SLEEP_REQUEST_KEY = intPreferencesKey("sleep_request_seconds")
val ARIA2C_MODE_KEY = stringPreferencesKey("aria2c_mode")
val EXTERNAL_DOWNLOADER = stringPreferencesKey("set external downloaders for ytdlp")
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
class DataStoreDownloaderSettings(private val context: Context) : DownloaderSettings {

     override val downloadPath: Flow<String> = context.dataStore.data.map { preferences -> preferences[DOWNLOAD_PATH] ?: "" }
     override val notificationState: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[DOWNLOAD_PROGRESS_NOTIFICATION] ?: true }
     override val sleepRequest: Flow<Int> = context.dataStore.data.map { preferences -> preferences[SLEEP_REQUEST_KEY] ?: 0 }
     override val embedThumbnail: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[EMBED_THUMBNAIL] ?: true }
     override val quickJS: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[QUICK_JS] ?: true }
     override val fingerprinting: Flow<Boolean> = context.dataStore.data.map { preferences -> preferences[FINGERPRINT] ?: true }
     override val aria2cMode: Flow<Aria2cMode> = context.dataStore.data.map { preferences ->
          preferences[ARIA2C_MODE_KEY]
               ?.let { runCatching { Aria2cMode.valueOf(it) }.getOrNull() }
               ?: Aria2cMode.Always
     }

     override val externalDownloaders: Flow<ExternalDownloaders> = context.dataStore.data.map { preferences ->
          preferences[EXTERNAL_DOWNLOADER]
               ?.let { runCatching { ExternalDownloaders.valueOf(it) }.getOrNull() } 
               ?: ExternalDownloaders.TurnedOff
     }

     override val audioHistory: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[SHOW_YTDLP_AUDIO_HISTORY] ?: true }
     override val videoHistory: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[SHOW_YTDLP_VIDEO_HISTORY] ?: true }
     override val ytdlpDetails: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[DOWNLOADING_DETAILS] ?: true }
     override val sponsorBlock: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[SPONSOR_BLOCK_IMPLEMENTATION] ?: true }
     override val playlistStatus: Flow<Boolean> =
          context.dataStore.data.map { preferences -> preferences[PLAYLIST_STATUS] ?: false }
     override val sponsorBlockCategories: Flow<Set<String>> = context.dataStore.data.map { preferences ->
          preferences[SPONSOR_BLOCK_CATEGORIES] ?: DEFAULT_SPONSOR_BLOCK_CATEGORIES
     }
     override val jsonAudioCard: Flow<List<JSONHistoryCards>> = context.dataStore.data.map { preferences ->
          val json =
               preferences[JSON_AUDIO_HISTORY] ?: "[]"
          Json.decodeFromString<List<JSONHistoryCards>>(json)
     }

     override val jsonVideoCard: Flow<List<JSONHistoryCards>> = context.dataStore.data.map { preferences ->
          val json =
               preferences[JSON_VIDEO_HISTORY] ?: "[]"
          Json.decodeFromString<List<JSONHistoryCards>>(json)
     }

     override suspend fun setDownloadPath(value: String) {
          context.dataStore.edit { preferences -> preferences[DOWNLOAD_PATH] = value }
     }

     override suspend fun setNotificationState(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[DOWNLOAD_PROGRESS_NOTIFICATION] = value }
     }

     @SuppressLint("SuspiciousIndentation")
     override suspend fun setSleepRequest(value: Int) {
          context.dataStore.edit { preferences -> preferences[SLEEP_REQUEST_KEY] = value }
     }

     override suspend fun setAria2cMode(value: Aria2cMode) {
          context.dataStore.edit { preferences ->
               preferences[ARIA2C_MODE_KEY] = value.name
               if (value != Aria2cMode.Disabled) {
                    preferences[EXTERNAL_DOWNLOADER] = ExternalDownloaders.TurnedOff.name
               }
          }
     }

     override suspend fun setExternalDownloader(value: ExternalDownloaders) {
          context.dataStore.edit { preferences ->
               preferences[EXTERNAL_DOWNLOADER] = value.name
               if (value != ExternalDownloaders.TurnedOff) {
                    preferences[ARIA2C_MODE_KEY] = Aria2cMode.Disabled.name
               }
          }
     }

     override suspend fun setQuickJS(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[QUICK_JS] = value }
     }

     override suspend fun setFingerprinting(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[FINGERPRINT] = value }     
     }

     override suspend fun setEmbedThumbnail(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[EMBED_THUMBNAIL] = value }
     }

     override suspend fun setAudioHistory(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[SHOW_YTDLP_AUDIO_HISTORY] = value }
     }

     override suspend fun setPlaylistStatus(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[PLAYLIST_STATUS] = value }
     }

     override suspend fun setVideoHistory(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[SHOW_YTDLP_VIDEO_HISTORY] = value }
     }

     override suspend fun setYtdlpDetails(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[DOWNLOADING_DETAILS] = value }
     }

     override suspend fun setSponsorBlock(value: Boolean) {
          context.dataStore.edit { preferences -> preferences[SPONSOR_BLOCK_IMPLEMENTATION] = value }
     }

     override suspend fun setSponsorBlockCategories(value: Set<String>) {
          context.dataStore.edit { preferences -> preferences[SPONSOR_BLOCK_CATEGORIES] = value }
     }

     override suspend fun setJSONAudio(value: JSONHistoryCards) {
          context.dataStore.edit { preferences ->
               val currentJson = preferences[JSON_AUDIO_HISTORY] ?: "[]"
               val currentList = Json.decodeFromString<List<JSONHistoryCards>>(currentJson)
               val updatedList = currentList + value
               preferences[JSON_AUDIO_HISTORY] = Json.encodeToString(updatedList)
          }
     }

     override suspend fun setJSONVideo(value: JSONHistoryCards) {
          context.dataStore.edit { preferences ->
               val currentJson = preferences[JSON_VIDEO_HISTORY] ?: "[]"
               val currentList = Json.decodeFromString<List<JSONHistoryCards>>(currentJson)
               val updatedList = currentList + value
               preferences[JSON_VIDEO_HISTORY] = Json.encodeToString(updatedList)
          }
     }

     override suspend fun clearYtdlp() {
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
