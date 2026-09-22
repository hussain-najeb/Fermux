@file:Suppress("SpellCheckingInspection")

package org.foss.fermux.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode
import org.foss.fermux.ytdlp.logic.downloader.ExternalDownloaders


interface DownloaderSettingsRepo {

     val downloadPath: Flow<String>
     val downloaderBellState: Flow<Boolean>
     val sleepRequest: Flow<Int>
     val embedThumbnail: Flow<Boolean>
     val quickJS: Flow<Boolean>
     val downloaderDebug: Flow<Boolean>
     val fingerprinting: Flow<Boolean>
     val aria2cMode: Flow<Aria2cMode>
     val externalDownloaders: Flow<ExternalDownloaders>
     val ytdlpDetails: Flow<Boolean>
     val sponsorBlock: Flow<Boolean>
     val sponsorBlockCategories: Flow<Set<String>>
     val playlistStatus: Flow<Boolean>
     val audioHistory: Flow<Boolean>
     val videoHistory: Flow<Boolean>
     val jsonAudioCard: Flow<List<JSONHistoryCards>>
     val jsonVideoCard: Flow<List<JSONHistoryCards>>

     suspend fun setDownloadPath(value: String)
     suspend fun setDownloaderBellState(value: Boolean)
     suspend fun setSleepRequest(value: Int)
     suspend fun setAria2cMode(value: Aria2cMode)
     suspend fun setExternalDownloader(value: ExternalDownloaders)
     suspend fun setQuickJS(value: Boolean)
     suspend fun setDownloaderDebug(value: Boolean)
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
     suspend fun clearHistory()
     suspend fun clearYtdlp()
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings_tab")

// ytdlp downloader tab.
val DOWNLOAD_PATH = stringPreferencesKey("download_path")
val DOWNLOADER_BELL_STATE = booleanPreferencesKey("bellState")
val SLEEP_REQUEST_KEY = intPreferencesKey("sleep_request_seconds")
val ARIA2C_MODE_KEY = stringPreferencesKey("aria2c_mode")
val EXTERNAL_DOWNLOADER = stringPreferencesKey("set external downloaders for ytdlp")
val DOWNLOADING_DETAILS = booleanPreferencesKey("download_details")
val SHOW_YTDLP_VIDEO_HISTORY = booleanPreferencesKey("video_history")
val QUICK_JS = booleanPreferencesKey("quick js")
val DOWNLOADER_DEBUG = booleanPreferencesKey("debug_button")
val FINGERPRINT = booleanPreferencesKey("fingerprint")
val SHOW_YTDLP_AUDIO_HISTORY = booleanPreferencesKey("audio_history")
val EMBED_THUMBNAIL = booleanPreferencesKey("embed_thumbnail")
val PLAYLIST_STATUS = booleanPreferencesKey("playlist_status")
val SPONSOR_BLOCK_IMPLEMENTATION = booleanPreferencesKey("sponsor_block")
val DEFAULT_SPONSOR_BLOCK_CATEGORIES = setOf("sponsor", "selfpromo", "interaction")
val SPONSOR_BLOCK_CATEGORIES = stringSetPreferencesKey("sponsor_block_categories")
val JSON_AUDIO_HISTORY = stringPreferencesKey("json_audio")
val JSON_VIDEO_HISTORY = stringPreferencesKey("json_video")

class DataStoreDownloaderSettings(private val settingStore: DataStore<Preferences>) : DownloaderSettingsRepo {

     constructor(context: Context) : this(context.dataStore)

     override val downloadPath: Flow<String> = settingStore.data.map { preferences -> preferences[DOWNLOAD_PATH] ?: "" }
     override val downloaderBellState: Flow<Boolean> = settingStore.data.map { preferences -> preferences[DOWNLOADER_BELL_STATE] ?: false }
     override val sleepRequest: Flow<Int> = settingStore.data.map { preferences -> preferences[SLEEP_REQUEST_KEY] ?: 0 }
     override val embedThumbnail: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[EMBED_THUMBNAIL] ?: true }
     override val quickJS: Flow<Boolean> = settingStore.data.map { preferences -> preferences[QUICK_JS] ?: true }
     override val downloaderDebug: Flow<Boolean> = settingStore.data.map { preferences -> preferences[DOWNLOADER_DEBUG] ?: false }
     override val fingerprinting: Flow<Boolean> = settingStore.data.map { preferences -> preferences[FINGERPRINT] ?: true }
     override val aria2cMode: Flow<Aria2cMode> = settingStore.data.map { preferences ->
          preferences[ARIA2C_MODE_KEY]
               ?.let { runCatching { Aria2cMode.valueOf(it) }.getOrNull() }
               ?: Aria2cMode.Always
     }
     override val externalDownloaders: Flow<ExternalDownloaders> = settingStore.data.map { preferences ->
          preferences[EXTERNAL_DOWNLOADER]
               ?.let { runCatching { ExternalDownloaders.valueOf(it) }.getOrNull() } 
               ?: ExternalDownloaders.TurnedOff
     }
     override val audioHistory: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[SHOW_YTDLP_AUDIO_HISTORY] ?: true }
     override val videoHistory: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[SHOW_YTDLP_VIDEO_HISTORY] ?: true }
     override val ytdlpDetails: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[DOWNLOADING_DETAILS] ?: true }
     override val sponsorBlock: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[SPONSOR_BLOCK_IMPLEMENTATION] ?: true }
     override val playlistStatus: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[PLAYLIST_STATUS] ?: false }
     override val sponsorBlockCategories: Flow<Set<String>> = settingStore.data.map { preferences ->
          preferences[SPONSOR_BLOCK_CATEGORIES] ?: DEFAULT_SPONSOR_BLOCK_CATEGORIES
     }
     override val jsonAudioCard: Flow<List<JSONHistoryCards>> = settingStore.data.map { preferences ->
          val json =
               preferences[JSON_AUDIO_HISTORY] ?: "[]"
          Json.decodeFromString<List<JSONHistoryCards>>(json)
     }

     override val jsonVideoCard: Flow<List<JSONHistoryCards>> = settingStore.data.map { preferences ->
          val json =
               preferences[JSON_VIDEO_HISTORY] ?: "[]"
          Json.decodeFromString<List<JSONHistoryCards>>(json)
     }

     override suspend fun setDownloadPath(value: String) {
          settingStore.edit { preferences -> preferences[DOWNLOAD_PATH] = value }
     }

     override suspend fun setDownloaderBellState(value: Boolean) {
          settingStore.edit { preferences -> preferences[DOWNLOADER_BELL_STATE] = value }
     }

     override suspend fun setSleepRequest(value: Int) {
          settingStore.edit { preferences -> preferences[SLEEP_REQUEST_KEY] = value }
     }

     override suspend fun setAria2cMode(value: Aria2cMode) {
          settingStore.edit { preferences ->
               preferences[ARIA2C_MODE_KEY] = value.name
               if (value != Aria2cMode.Disabled) {
                    preferences[EXTERNAL_DOWNLOADER] = ExternalDownloaders.TurnedOff.name
               }
          }
     }

     override suspend fun setExternalDownloader(value: ExternalDownloaders) {
          settingStore.edit { preferences ->
               preferences[EXTERNAL_DOWNLOADER] = value.name
               if (value != ExternalDownloaders.TurnedOff) {
                    preferences[ARIA2C_MODE_KEY] = Aria2cMode.Disabled.name
               }
          }
     }

     override suspend fun setQuickJS(value: Boolean) {
          settingStore.edit { preferences -> preferences[QUICK_JS] = value }
     }

     override suspend fun setDownloaderDebug(value: Boolean) {
          settingStore.edit { preferences -> preferences[DOWNLOADER_DEBUG] = value }
     }

     override suspend fun setFingerprinting(value: Boolean) {
          settingStore.edit { preferences -> preferences[FINGERPRINT] = value }
     }

     override suspend fun setEmbedThumbnail(value: Boolean) {
          settingStore.edit { preferences -> preferences[EMBED_THUMBNAIL] = value }
     }

     override suspend fun setAudioHistory(value: Boolean) {
          settingStore.edit { preferences -> preferences[SHOW_YTDLP_AUDIO_HISTORY] = value }
     }

     override suspend fun setPlaylistStatus(value: Boolean) {
          settingStore.edit { preferences -> preferences[PLAYLIST_STATUS] = value }
     }

     override suspend fun setVideoHistory(value: Boolean) {
          settingStore.edit { preferences -> preferences[SHOW_YTDLP_VIDEO_HISTORY] = value }
     }

     override suspend fun setYtdlpDetails(value: Boolean) {
          settingStore.edit { preferences -> preferences[DOWNLOADING_DETAILS] = value }
     }

     override suspend fun setSponsorBlock(value: Boolean) {
          settingStore.edit { preferences -> preferences[SPONSOR_BLOCK_IMPLEMENTATION] = value }
     }

     override suspend fun setSponsorBlockCategories(value: Set<String>) {
          settingStore.edit { preferences -> preferences[SPONSOR_BLOCK_CATEGORIES] = value }
     }

     override suspend fun setJSONAudio(value: JSONHistoryCards) {
          settingStore.edit { preferences ->
               val currentJson = preferences[JSON_AUDIO_HISTORY] ?: "[]"
               val currentList = Json.decodeFromString<List<JSONHistoryCards>>(currentJson)
               val updatedList = currentList + value
               preferences[JSON_AUDIO_HISTORY] = Json.encodeToString(updatedList)
          }
     }

     override suspend fun setJSONVideo(value: JSONHistoryCards) {
          settingStore.edit { preferences ->
               val currentJson = preferences[JSON_VIDEO_HISTORY] ?: "[]"
               val currentList = Json.decodeFromString<List<JSONHistoryCards>>(currentJson)
               val updatedList = currentList + value
               preferences[JSON_VIDEO_HISTORY] = Json.encodeToString(updatedList)
          }
     }

     override suspend fun clearYtdlp() {
          settingStore.edit { preferences ->
               preferences.remove(key = DOWNLOAD_PATH)
               preferences.remove(key = DOWNLOADER_DEBUG)
               preferences.remove(key = SLEEP_REQUEST_KEY)
               preferences.remove(key = ARIA2C_MODE_KEY)
               preferences.remove(key = DOWNLOADING_DETAILS)
               preferences.remove(key = SHOW_YTDLP_VIDEO_HISTORY)
               preferences.remove(key = SHOW_YTDLP_AUDIO_HISTORY)
               preferences.remove(key = EMBED_THUMBNAIL)
               preferences.remove(key = PLAYLIST_STATUS)
               preferences.remove(key = SPONSOR_BLOCK_IMPLEMENTATION)
               preferences.remove(key = SPONSOR_BLOCK_CATEGORIES)
               preferences.remove(key = QUICK_JS)
               preferences.remove(key = DOWNLOADER_DEBUG)
               preferences.remove(key = FINGERPRINT)
               preferences.remove(key = EXTERNAL_DOWNLOADER)
          }
     }

     override suspend fun clearHistory() {
          settingStore.edit { preferences ->
               preferences.remove(key = JSON_AUDIO_HISTORY)
               preferences.remove(key = JSON_VIDEO_HISTORY)
          }
     }
}


