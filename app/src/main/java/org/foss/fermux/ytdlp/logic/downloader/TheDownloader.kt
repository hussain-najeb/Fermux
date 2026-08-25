package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import android.os.Environment
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.foss.fermux.main.copyFileToDownloads

/**
 * Problem:
 * YouTube has been rolling out PO Token (Proof of Origin Token) requirements more aggressively
 * this is Google's newer anti-bot layer, separate from TLS fingerprinting and separate from
 * something like Instagram-like session checks. It specifically requires either:
 * A valid PO token (generated via a JS challenge, which yt-dlp gets through a plugin), or
 * Cookies from a real logged-in session as a fallback
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


suspend fun downloaderLogic(
     context: Context,
     showDetails: Boolean,
     url: String,
     taskId: String,
     aria2c: Boolean = true,
     aria2cHLSWithDASHCase: Boolean = false,
     sleepRequest: Int = 0,
     playlistStatus: Boolean = true,
     musicQuality: AudioQuality? = null,
     videoQuality: VideoQuality? = null,
     sponsorBlock: Boolean = false,
     embedThumbnail: Boolean = true,
     sponsorBlockCategories: Set<String> = emptySet(),
     onUpdate: (Float, String) -> Unit
) {

     val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
     val outputPath = "${downloadDir?.absolutePath}/%(title)s.%(ext)s"
     val request = YoutubeDLRequest(url)


     val shouldUseAria2c = aria2c || (aria2cHLSWithDASHCase && videoQuality != VideoQuality.BEST)

     if (shouldUseAria2c) {
          request.addOption("--downloader", "libaria2c.so")
          request.addOption(
               "--external-downloader-args", "aria2c:--summary-interval=1 -x 16 -s 16 -k 1M"
          )
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

     if (embedThumbnail) {
          request.addOption("--embed-thumbnail")
     }

     if (playlistStatus) {
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

     withContext(Dispatchers.IO) {
          val existingFiles = downloadDir?.listFiles()?.map { it.absolutePath }?.toSet() ?: emptySet()

          val response = YoutubeDL.getInstance().execute(request, taskId) { progress, _, line ->
               onUpdate(progress, line)
          }

          downloadDir?.listFiles()?.filter { it.absolutePath !in existingFiles }?.forEach { file ->
                    copyFileToDownloads(context, file, file.name, subFolder = "fermux/downloader")
               }
          Log.d("fermux", "exit=${response.exitCode}")
          Log.d("fermux", "out=${response.out}")
          Log.d("fermux", "err=${response.err}")
     }
}


suspend fun fetchingTheMetadata(url: String): DownloadMetadata = withContext(Dispatchers.IO) {
     val info = YoutubeDL.getInstance().getInfo(url)
     DownloadMetadata(
          title = info.title ?: "Unknown title",
          thumbnail = info.thumbnail ?: "",
          duration = info.duration,
          uploader = info.uploader
     )
}
