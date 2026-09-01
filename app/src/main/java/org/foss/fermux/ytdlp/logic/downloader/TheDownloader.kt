package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import android.os.Environment
import com.yausername.youtubedl_android.YoutubeDLRequest

/*
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
     aria2cMode: Aria2cMode = Aria2cMode.Always,
     url: String,
     taskId: String,
     sleepRequest: Int = 0,
//     audioFormat: AudioFormat,
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


     if (quickJs) {
          request.addOption(
               "--js-runtimes",
               "quickjs:${context.applicationInfo.nativeLibraryDir}/libqjs-cli.so"
          )
     }

     request.addOption("--impersonate", "chrome")


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

     //if (embedThumbnail && audioFormat != AudioFormat.OpusFormat) {
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
          request.addOption("--audio-quality", "mp3")
//          request.addOption("--audio-format", audioFormat.ytdlpFormat)
          request.addOption("--audio-quality", it.musicQuality)
     }
     videoQuality?.let {
          request.addOption("--merge-output-format", "mp4")
          request.addOption("-f", it.videoQuality)
     }

     request.addOption("--restrict-filenames")
     request.addOption("-i")
     request.addOption("--embed-metadata")

     request.addOption("-o", outputPath)

     execution(downloadDir, request, taskId, onUpdate, context)
}
