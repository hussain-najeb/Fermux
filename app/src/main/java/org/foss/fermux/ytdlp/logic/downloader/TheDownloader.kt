package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import android.os.Environment
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDLRequest
import java.io.File

/*
 *
 * Todo:
 *  1- Cookies implementation in the settings tab.
 *  2- UI for easy cookie extraction.
 *  3- A startup reminder and dialog for the cookies method and why its a must for sites that use cookies.
 *  4- a RegEx for if "WARNING: [youtube] Unable to fetch GVS PO Token for web_safari client:
 *  Missing required Visitor Data. You may need to pass Visitor Data with --extractor-args "youtube:visitor_data=XXX"
 *   WARNING: [youtube] Unable to fetch GVS PO Token for web_safari client:
 *   Missing required Visitor Data. You may need to pass Visitor Data with
 *   --extractor-args "youtube:visitor_data=XXX" the user here gets a
 *   dialog and a reminder about the issue, brief rundown and how to fix it, including in that alert dialog the name of the
 *   extractor and whats with it. RegEx or a filter should have a value that is "extractorName" called in as a regex when the regex sees
 *   there is the word "cookies" involved, so you get what site is doing the cookies and if its an issue in the first place.
 *   5- an alert dialog for the last point so its a clear thing that explains where and when and how its done!
 *   6- cookies expire!
 */

/**
 * The completed function of ytdlp that downloads and injects flags in during download.
 * This function has all the settings linked to the [org.foss.fermux.settings.logic.DownloaderSettingsViewModel]  in it to toggle on and off.
 *
 *
 * YouTube has been rolling out PO Token (Proof of Origin Token) requirements more aggressively
 * this is Google's newer anti-bot layer, separate from TLS fingerprinting and separate from
 * something like Instagram-like session checks. It specifically requires either:
 * A valid PO token (generated via a JS challenge, which yt-dlp gets through a plugin), or
 * Cookies from a real logged-in session as a fallback. That's why this function has the Quick.js engine embedded in it.
 * @param showDetails This parameter is to expose the ytdlp logs to the UI.
 * @param aria2cMode This is a boolean that turns the aria2c flag in the downloader.
 * @param url This parameter is to get the url given by the user to be downloaded.
 * @param sleepRequest This parameter is for the user to decide how much they want time added between every ytdlp request.
 * @param quickJs This is a JS framework for impersonation used by ytdlp to get past YouTube.
 * @param sp
 */
suspend fun downloaderLogic(
     context: Context,
     showDetails: Boolean,
     aria2cMode: Aria2cMode = Aria2cMode.Always,
     url: String,
     taskId: String,
     sleepRequest: Int = 0,
     playlistStatus: Boolean = true,
     quickJs: Boolean = true,
     musicQuality: AudioQuality? = null,
     videoQuality: VideoQuality? = null,
     sponsorBlock: Boolean = true,
     embedThumbnail: Boolean = true,
     sponsorBlockCategories: Set<String> = emptySet(),
     onUpdate: (Float, String) -> Unit
) {

     val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
     val outputPath = "${downloadDir?.absolutePath}/%(title)s.%(ext)s"
     val request = YoutubeDLRequest(url)

     val nativeLibDir = context.applicationInfo.nativeLibraryDir
     val quickJsBinary = File(nativeLibDir, "libqjs.so")

     if (quickJsBinary.exists() && quickJs) {
          request.addOption("--js-runtimes", "quickjs:${quickJsBinary.absolutePath}")
     } else {
          Log.w("fermux", "QuickJS binary not found at: $quickJsBinary")
     }

     if (sleepRequest > 0) {
          request.addOption("--sleep-requests", sleepRequest)
     }

     if (sponsorBlock && sponsorBlockCategories.isNotEmpty()) {
          request.addOption("--sponsorblock-remove", sponsorBlockCategories.joinToString(","))
     }
     if (showDetails) {
          request.addOption("-v")
     }

     val shouldUseAria2c = when (aria2cMode) {
               Aria2cMode.Always -> true
               Aria2cMode.EdgeCaseOnly -> videoQuality != VideoQuality.BEST
               Aria2cMode.Disabled -> false
     }

     if (shouldUseAria2c) {
          request.addOption("--downloader", "libaria2c.so")
          request.addOption(
               "--external-downloader-args", "aria2c:--summary-interval=1 -x 12 -s 12 -k 1M"
          )
     }

     if (embedThumbnail) {
          request.addOption("--embed-thumbnail")
     }

     if (playlistStatus) {
          request.addOption("--yes-playlist")
     } else { 
          request.addOption("--no-playlist") 
     }

     musicQuality?.let {
          request.addOption("-x")
          request.addOption("--audio-format", "mp3")
          request.addOption("--audio-quality", it.musicQuality)
     }
     videoQuality?.let {
          request.addOption("--merge-output-format", "mp4")
          request.addOption("-f", it.videoQuality)
     }

     request.addOption("--restrict-filenames")
     request.addOption("-i")
     request.addOption("--convert-thumbnails", "jpg")
     request.addOption("--embed-metadata")

     request.addOption("-o", outputPath)

     execution(downloadDir, request, taskId, onUpdate, context)
}
