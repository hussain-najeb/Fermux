package org.foss.fermux.dataStore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.foss.fermux.ytdlp.logic.downloader.*


interface DownloaderSettingsRepo {

     val downloadPath: Flow<String>
     val downloaderBellState: Flow<Boolean>
     val sleepRequest: Flow<Int>
     val embedThumbnail: Flow<Boolean>
     val quickJS: Flow<Boolean>
     val downloaderDebug: Flow<Boolean>
     val fingerprinting: Flow<Boolean>
     val aria2cMode: Flow<Aria2cMode>
     val thumbnailFormat: Flow<ThumbnailFormat>
     val audioFormat: Flow<AudioFormat>
     val videoFormat: Flow<VideoFormat>
     val videoComp: Flow<Boolean>
     val externalDownloaders: Flow<ExternalDownloaders>
     val ytdlpDetails: Flow<Boolean>
     val sponsorBlock: Flow<Boolean>
     val sponsorBlockCategories: Flow<Set<String>>
     val playlistStatus: Flow<Boolean>
     val history: Flow<Boolean>
     val upToDate: Flow<Boolean>
     val ytdlpChannel: Flow<YtdlpChannel>
     val wifi: Flow<Connectivity>
     val fragRetries: Flow<Int>
     val retries: Flow<Int>
     val quickDownloads: Flow<Boolean>
     val ipvConnection: Flow<IpvConnection>
     val quickFormat: Flow<QuickDownloadFormats>
     val quickVideo: Flow<QuickVideoQuality>
     val quickAudio: Flow<QuickAudioQuality>

     suspend fun setDownloadPath(value: String)
     suspend fun setDownloaderBellState(value: Boolean)
     suspend fun setSleepRequest(value: Int)
     suspend fun setAria2cMode(value: Aria2cMode)
     suspend fun setThumbnail(value: ThumbnailFormat)
     suspend fun setAudioFormat(value: AudioFormat)
     suspend fun setVideoFormat(value: VideoFormat)
     suspend fun setVideoComp(value: Boolean)
     suspend fun setExternalDownloader(value: ExternalDownloaders)
     suspend fun setQuickJS(value: Boolean)
     suspend fun setDownloaderDebug(value: Boolean)
     suspend fun setFingerprinting(value: Boolean)
     suspend fun setEmbedThumbnail(value: Boolean)
     suspend fun setHistory(value: Boolean)
     suspend fun setPlaylistStatus(value: Boolean)
     suspend fun setYtdlpDetails(value: Boolean)
     suspend fun setSponsorBlock(value: Boolean)
     suspend fun setSponsorBlockCategories(value: Set<String>)
     suspend fun setUpToDate(value: Boolean)
     suspend fun setWifi(value: Connectivity)
     suspend fun setFragRetries(value: Int)
     suspend fun setRetries(value: Int)
     suspend fun setQuickDownloads(value: Boolean)
     suspend fun setIpvConnection(value: IpvConnection)
     suspend fun setYtdlpChannel(value: YtdlpChannel)
     suspend fun resetArgs(): DownloaderArgumentsSnapshot
     suspend fun restoreArgs(snapshot: DownloaderArgumentsSnapshot)
     suspend fun resetYtdlp(): DownloaderSettingsSnapshot
     suspend fun restoreYtdlp(snapshot: DownloaderSettingsSnapshot)
     suspend fun setQuickFormat(value: QuickDownloadFormats)
     suspend fun setQuickVideo(value: QuickVideoQuality)
     suspend fun setQuickAudio(value: QuickAudioQuality)
}

data class DownloaderSettingsSnapshot(
     val downloadPath: String?,
     val downloaderDebug: Boolean?,
     val sleepRequest: Int?,
     val aria2cMode: String?,
     val ytdlpDetails: Boolean?,
     val history: Boolean?,
     val embedThumbnail: Boolean?,
     val playlistStatus: Boolean?,
     val sponsorBlock: Boolean?,
     val sponsorBlockCategories: Set<String>?,
     val quickJS: Boolean?,
     val fingerprinting: Boolean?,
     val externalDownloader: String?,
     val wifi: String?,
     val quickDownloads: Boolean?,
     val quickDownloadFormats: QuickDownloadFormats,
     val quickAudioQuality: QuickAudioQuality,
     val quickVideoQuality: QuickVideoQuality,
)

data class DownloaderArgumentsSnapshot(
     val playlist: Boolean?,
     val sleepRequest: Int?,
     val fragRetries: Int?,
     val retries: Int?,
     val thumbnailFormat: String?,
     val videoFormat: String?,
     val audioFormat: String?,
     val thumbnail: Boolean?,
     val videoComp: Boolean?
)

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("settings_tab")

// ytdlp downloader tab.

// TODO. for args
//  1- Add encoder options, like AV1/H.264/VP9
//  4- notification on failure
//  5- Queue behavior: pause/resume whole queue, queue ordering, priority, auto-start queued downloads, maximum active jobs.


// TODO. for settings.
//  1- sequential downloads, one after the other, so one is done, the other is executed right after

val DOWNLOAD_PATH = stringPreferencesKey("download_path")
val DOWNLOADER_BELL_STATE = booleanPreferencesKey("bellState")
val SLEEP_REQUEST_KEY = intPreferencesKey("sleep_request_seconds")
val ARIA2C_MODE_KEY = stringPreferencesKey("aria2c_mode")
val THUMBNAIL_FORMATS = stringPreferencesKey("thumbnail_selection")
val AUDIO_FORMATS = stringPreferencesKey("audio_formats")
val VIDEO_FORMATS = stringPreferencesKey("video_formats")
val VIDEO_COMP = booleanPreferencesKey("video_comp")
val EXTERNAL_DOWNLOADER = stringPreferencesKey("set_external_downloaders_for_ytdlp")
val DOWNLOADING_DETAILS = booleanPreferencesKey("download_details")
val HISTORY = booleanPreferencesKey("history")
val QUICK_JS = booleanPreferencesKey("quickJs")
val DOWNLOADER_DEBUG = booleanPreferencesKey("debug_button")
val FINGERPRINT = booleanPreferencesKey("fingerprint")
val EMBED_THUMBNAIL = booleanPreferencesKey("embed_thumbnail")
val PLAYLIST_STATUS = booleanPreferencesKey("playlist_status")
val SPONSOR_BLOCK_IMPLEMENTATION = booleanPreferencesKey("sponsor_block")
val DEFAULT_SPONSOR_BLOCK_CATEGORIES = setOf("sponsor", "selfpromo", "interaction")
val SPONSOR_BLOCK_CATEGORIES = stringSetPreferencesKey("sponsor_block_categories")
val UP_TO_DATE = booleanPreferencesKey("up_to_date")
val YTDLP_CHANNEL = stringPreferencesKey("ytdlp_channels")
val WIFI = stringPreferencesKey("wifi")
val IPV = stringPreferencesKey("ipv")
val FRAG_RETRIES = intPreferencesKey("frag_retries")
val RETRIES = intPreferencesKey("retries")
val QUICK_DOWNLOADS = booleanPreferencesKey("quick_downloads")
val QUICK_DOWNLOADS_FORMAT = stringPreferencesKey("quick_format")
val QUICK_DOWNLOADS_VIDEO = stringPreferencesKey("quick_video")
val QUICK_DOWNLOADS_AUDIO = stringPreferencesKey("quick_audio")

class DataStoreDownloaderSettings(private val settingStore: DataStore<Preferences>) : DownloaderSettingsRepo {

     constructor(context: Context) : this(context.dataStore)

     override val downloadPath: Flow<String> = settingStore.data.map { preferences -> preferences[DOWNLOAD_PATH] ?: "" }
     override val downloaderBellState: Flow<Boolean> = settingStore.data.map { preferences -> preferences[DOWNLOADER_BELL_STATE] ?: false }
     override val sleepRequest: Flow<Int> = settingStore.data.map { preferences -> preferences[SLEEP_REQUEST_KEY] ?: 0 }
     override val fragRetries: Flow<Int> = settingStore.data.map { preferences -> preferences[FRAG_RETRIES] ?: 10 }
     override val retries: Flow<Int> = settingStore.data.map { preferences -> preferences[RETRIES] ?: 10 }
     override val embedThumbnail: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[EMBED_THUMBNAIL] ?: true }
     override val quickJS: Flow<Boolean> = settingStore.data.map { preferences -> preferences[QUICK_JS] ?: true }
     override val downloaderDebug: Flow<Boolean> = settingStore.data.map { preferences -> preferences[DOWNLOADER_DEBUG] ?: false }
     override val fingerprinting: Flow<Boolean> = settingStore.data.map { preferences -> preferences[FINGERPRINT] ?: true }
     override val aria2cMode: Flow<Aria2cMode> = settingStore.data.map { preferences ->
          preferences[ARIA2C_MODE_KEY]
               ?.let { runCatching { Aria2cMode.valueOf(it) }.getOrNull() }
               ?: Aria2cMode.Disabled
     }
     override val thumbnailFormat: Flow<ThumbnailFormat> = settingStore.data.map { preferences ->
          preferences[THUMBNAIL_FORMATS]
               ?.let { runCatching { ThumbnailFormat.valueOf(it) }.getOrNull() }
               ?: ThumbnailFormat.Png
     }

     override val audioFormat: Flow<AudioFormat> = settingStore.data.map { preferences ->
          preferences[AUDIO_FORMATS]
               ?.let { runCatching { AudioFormat.valueOf(it) }.getOrNull() }
               ?: AudioFormat.Mp3Format
     }

     override val videoFormat: Flow<VideoFormat> = settingStore.data.map { preferences ->
          preferences[VIDEO_FORMATS]
               ?.let { runCatching { VideoFormat.valueOf(it) }.getOrNull() }
               ?: VideoFormat.Mp4Format
     }

     override val videoComp: Flow<Boolean> = settingStore.data.map { preferences ->
          preferences[VIDEO_COMP] ?: false
     }

     override val externalDownloaders: Flow<ExternalDownloaders> = settingStore.data.map { preferences ->
          preferences[EXTERNAL_DOWNLOADER]
               ?.let { runCatching { ExternalDownloaders.valueOf(it) }.getOrNull() } 
               ?: ExternalDownloaders.YtdlpNativeDownloader
     }
     override val history: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[HISTORY] ?: true }
     override val ytdlpDetails: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[DOWNLOADING_DETAILS] ?: true }
     override val sponsorBlock: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[SPONSOR_BLOCK_IMPLEMENTATION] ?: true }
     override val playlistStatus: Flow<Boolean> =
          settingStore.data.map { preferences -> preferences[PLAYLIST_STATUS] ?: false }
     override val sponsorBlockCategories: Flow<Set<String>> = settingStore.data.map { preferences ->
          preferences[SPONSOR_BLOCK_CATEGORIES] ?: DEFAULT_SPONSOR_BLOCK_CATEGORIES
     }

     override val upToDate: Flow<Boolean> = settingStore.data.map { preferences ->
          preferences[UP_TO_DATE] ?: false
     }

     override val ytdlpChannel: Flow<YtdlpChannel> = settingStore.data.map { preferences ->
          preferences[YTDLP_CHANNEL]
               ?.let { runCatching { YtdlpChannel.valueOf(it) }.getOrNull() }
               ?: YtdlpChannel.Nightly
     }

     override val wifi: Flow<Connectivity> = settingStore.data.map { preferences ->
          preferences[WIFI]
               ?.let { runCatching { Connectivity.valueOf(it) }.getOrNull() }
               ?: Connectivity.Any
     }

     override val ipvConnection: Flow<IpvConnection> = settingStore.data.map { preferences ->
          preferences[IPV]
               ?.let { runCatching { IpvConnection.valueOf(it) }.getOrNull() }
               ?: IpvConnection.Disabled
     }

     override val quickDownloads: Flow<Boolean> = settingStore.data.map { preferences -> preferences[QUICK_DOWNLOADS] ?: true }

     override suspend fun setDownloadPath(value: String) {
          settingStore.edit { preferences -> preferences[DOWNLOAD_PATH] = value }
     }

     override suspend fun setDownloaderBellState(value: Boolean) {
          settingStore.edit { preferences -> preferences[DOWNLOADER_BELL_STATE] = value }
     }

     override suspend fun setSleepRequest(value: Int) {
          settingStore.edit { preferences -> preferences[SLEEP_REQUEST_KEY] = value }
     }

     override suspend fun setFragRetries(value: Int) {
          settingStore.edit { preferences -> preferences[FRAG_RETRIES] = value }
     }

     override suspend fun setRetries(value: Int) {
          settingStore.edit { preferences -> preferences[RETRIES] = value }
     }

     override suspend fun setAria2cMode(value: Aria2cMode) {
          settingStore.edit { preferences ->
               preferences[ARIA2C_MODE_KEY] = value.name
               if (value != Aria2cMode.Disabled) {
                    preferences[EXTERNAL_DOWNLOADER] = ExternalDownloaders.Disabled.name
               }
          }
     }

     override suspend fun setThumbnail(value: ThumbnailFormat) {
          settingStore.edit { preferences ->
               preferences[THUMBNAIL_FORMATS] = value.name
          }
     }

     override suspend fun setAudioFormat(value: AudioFormat) {
          settingStore.edit { preferences ->
               preferences[AUDIO_FORMATS] = value.name
          }
     }

     override suspend fun setVideoFormat(value: VideoFormat) {
          settingStore.edit { preferences ->
               preferences[VIDEO_FORMATS] = value.name
          }
     }

     override suspend fun setVideoComp(value: Boolean) {
          settingStore.edit { preferences ->
               preferences[VIDEO_COMP] = value
          }
     }

     override suspend fun setExternalDownloader(value: ExternalDownloaders) {
          settingStore.edit { preferences ->
               preferences[EXTERNAL_DOWNLOADER] = value.name
               if (value != ExternalDownloaders.Disabled) {
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

     override suspend fun setHistory(value: Boolean) {
          settingStore.edit { preferences -> preferences[HISTORY] = value }
     }

     override suspend fun setPlaylistStatus(value: Boolean) {
          settingStore.edit { preferences -> preferences[PLAYLIST_STATUS] = value }
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

     override suspend fun setUpToDate(value: Boolean) {
          settingStore.edit { preferences -> preferences[UP_TO_DATE] = value }
     }

     override suspend fun setYtdlpChannel(value: YtdlpChannel) {
          settingStore.edit { preferences -> preferences[YTDLP_CHANNEL] = value.name }
     }

     override suspend fun setWifi(value: Connectivity) {
          settingStore.edit { preferences -> preferences[WIFI] = value.name }
     }

     override suspend fun setIpvConnection(value: IpvConnection) {
          settingStore.edit { preferences -> preferences[IPV] = value.name }
     }

     override suspend fun setQuickDownloads(value: Boolean) {
          settingStore.edit { preferences -> preferences[QUICK_DOWNLOADS] = value }
     }

     override suspend fun resetArgs(): DownloaderArgumentsSnapshot {
          lateinit var snapshot: DownloaderArgumentsSnapshot
          settingStore.edit { preferences ->
               snapshot = DownloaderArgumentsSnapshot(
                    playlist = preferences[PLAYLIST_STATUS],
                    sleepRequest = preferences[SLEEP_REQUEST_KEY],
                    thumbnail = preferences[EMBED_THUMBNAIL],
                    thumbnailFormat = preferences[THUMBNAIL_FORMATS],
                    videoFormat = preferences[VIDEO_FORMATS],
                    audioFormat = preferences[AUDIO_FORMATS],
                    videoComp = preferences[VIDEO_COMP],
                    fragRetries = preferences[FRAG_RETRIES],
                    retries = preferences[RETRIES],
               )
               preferences.remove(key = PLAYLIST_STATUS)
               preferences.remove(key = SLEEP_REQUEST_KEY)
               preferences.remove(key = EMBED_THUMBNAIL)
               preferences.remove(key = THUMBNAIL_FORMATS)
               preferences.remove(key = VIDEO_COMP)
               preferences.remove(key = VIDEO_FORMATS)
               preferences.remove(key = AUDIO_FORMATS)
               preferences.remove(key = RETRIES)
               preferences.remove(key = FRAG_RETRIES)
          }
          return snapshot
     }

     override suspend fun restoreArgs(snapshot: DownloaderArgumentsSnapshot) {
               settingStore.edit { preferences ->
                    preferences.restore(key = PLAYLIST_STATUS, snapshot.playlist)
                    preferences.restore(key = SLEEP_REQUEST_KEY, snapshot.sleepRequest)
                    preferences.restore(key = EMBED_THUMBNAIL, snapshot.thumbnail)
                    preferences.restore(key = THUMBNAIL_FORMATS, snapshot.thumbnailFormat)
                    preferences.restore(key = VIDEO_COMP, snapshot.videoComp)
                    preferences.restore(key = VIDEO_FORMATS, snapshot.videoFormat)
                    preferences.restore(key = AUDIO_FORMATS, snapshot.audioFormat)
                    preferences.restore(key = FRAG_RETRIES, snapshot.fragRetries)
                    preferences.restore(key = RETRIES, snapshot.retries)
               }
     }


     override suspend fun resetYtdlp(): DownloaderSettingsSnapshot {
          lateinit var snapshot: DownloaderSettingsSnapshot
          settingStore.edit { preferences ->
               snapshot = DownloaderSettingsSnapshot(
                    downloadPath = preferences[DOWNLOAD_PATH],
                    downloaderDebug = preferences[DOWNLOADER_DEBUG],
                    sleepRequest = preferences[SLEEP_REQUEST_KEY],
                    aria2cMode = preferences[ARIA2C_MODE_KEY],
                    ytdlpDetails = preferences[DOWNLOADING_DETAILS],
                    history = preferences[HISTORY],
                    embedThumbnail = preferences[EMBED_THUMBNAIL],
                    playlistStatus = preferences[PLAYLIST_STATUS],
                    sponsorBlock = preferences[SPONSOR_BLOCK_IMPLEMENTATION],
                    sponsorBlockCategories = preferences[SPONSOR_BLOCK_CATEGORIES],
                    quickJS = preferences[QUICK_JS],
                    fingerprinting = preferences[FINGERPRINT],
                    externalDownloader = preferences[EXTERNAL_DOWNLOADER],
                    wifi = preferences[WIFI],
                    quickDownloads = preferences[QUICK_DOWNLOADS]
               )
               preferences.remove(key = DOWNLOAD_PATH)
               preferences.remove(key = DOWNLOADER_DEBUG)
               preferences.remove(key = SLEEP_REQUEST_KEY)
               preferences.remove(key = ARIA2C_MODE_KEY)
               preferences.remove(key = DOWNLOADING_DETAILS)
               preferences.remove(key = HISTORY)
               preferences.remove(key = EMBED_THUMBNAIL)
               preferences.remove(key = PLAYLIST_STATUS)
               preferences.remove(key = SPONSOR_BLOCK_IMPLEMENTATION)
               preferences.remove(key = SPONSOR_BLOCK_CATEGORIES)
               preferences.remove(key = QUICK_JS)
               preferences.remove(key = FINGERPRINT)
               preferences.remove(key = EXTERNAL_DOWNLOADER)
               preferences.remove(key = WIFI)
               preferences.remove(key = QUICK_DOWNLOADS)
          }
          return snapshot
     }

     override suspend fun restoreYtdlp(snapshot: DownloaderSettingsSnapshot) {
          settingStore.edit { preferences ->
               preferences.restore(DOWNLOAD_PATH, snapshot.downloadPath)
               preferences.restore(DOWNLOADER_DEBUG, snapshot.downloaderDebug)
               preferences.restore(SLEEP_REQUEST_KEY, snapshot.sleepRequest)
               preferences.restore(ARIA2C_MODE_KEY, snapshot.aria2cMode)
               preferences.restore(DOWNLOADING_DETAILS, snapshot.ytdlpDetails)
               preferences.restore(HISTORY, snapshot.history)
               preferences.restore(EMBED_THUMBNAIL, snapshot.embedThumbnail)
               preferences.restore(PLAYLIST_STATUS, snapshot.playlistStatus)
               preferences.restore(SPONSOR_BLOCK_IMPLEMENTATION, snapshot.sponsorBlock)
               preferences.restore(SPONSOR_BLOCK_CATEGORIES, snapshot.sponsorBlockCategories)
               preferences.restore(QUICK_JS, snapshot.quickJS)
               preferences.restore(FINGERPRINT, snapshot.fingerprinting)
               preferences.restore(EXTERNAL_DOWNLOADER, snapshot.externalDownloader)
               preferences.restore(WIFI, snapshot.wifi)
               preferences.restore(QUICK_DOWNLOADS, snapshot.quickDownloads)
          }
     }
}

private fun <T> MutablePreferences.restore(
     key: Preferences.Key<T>,
     value: T?
) {
     if (value == null) remove(key) else this[key] = value
}

