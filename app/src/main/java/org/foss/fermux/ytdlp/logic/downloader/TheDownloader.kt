package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import android.os.Environment
import com.yausername.youtubedl_android.YoutubeDLRequest
import org.foss.fermux.utils.CopiedFile
import java.io.File

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

/*
TODO. I probably need BG-Utils with deno JS runtime with ytdlp PO solver in the main yausername lib, could be a PR and some change, just chuck in the whole thing
THEMOSTCOMPLETEYTDLPCLIENTINTHEWORLD
 */

suspend fun downloaderLogic(
     context: Context,
     showDetails: Boolean,
     aria2cMode: Aria2cMode = Aria2cMode.Always,
     thumbnail: ThumbnailFormat = ThumbnailFormat.Png,
     audioFormats: AudioFormat = AudioFormat.Mp3Format,
     videoFormats: VideoFormat = VideoFormat.Mp4Format,
     videoComp: Boolean,
     externalDownloaders: ExternalDownloaders = ExternalDownloaders.Disabled,
     url: String,
     taskId: String,
     sleepRequest: Int = 0,
     playlistStatus: Boolean = false,
     quickJs: Boolean = true,
     fingerprinting: Boolean = true,
     audioQuality: AudioQuality? = null,
     videoQuality: VideoQuality? = null,
     ipv: IpvConnection,
     sponsorBlock: Boolean = true,
     embedThumbnail: Boolean = true,
     sponsorBlockCategories: Set<String> = emptySet(),
     fragRetry: Int,
     retries: Int,
     quickDownloadFormats: QuickDownloadFormats = QuickDownloadFormats.QuickAudio,
     quickAudioQuality: QuickAudioQuality? = null,
     quickVideoQuality: QuickVideoQuality? = null,
     onUpdate: (Float, String) -> Unit
): List<CopiedFile> {

     val baseDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.filesDir
     val downloadDir = File(baseDir, taskId).apply { mkdirs() }
     val outputPath = "${downloadDir.absolutePath}/%(title)s.%(ext)s"
     val request = YoutubeDLRequest(url)

     if (quickJs) {
          request.addOption(
               "--js-runtimes",
               "quickjs:${context.applicationInfo.nativeLibraryDir}/libqjs-cli.so"
          )
     }

     if (fingerprinting) {
          request.addOption("--impersonate", "chrome")
     }

     when (ipv) {
          IpvConnection.Ipv6 -> request.addOption("--force-ipv6")
          IpvConnection.Ipv4 -> request.addOption("--force-ipv4")
          IpvConnection.Disabled -> Unit
     }

     request.addOption("--no-simulate")

     request.addOption("--print",
          "before_dl:$FERMUX_METADATA_MARKER%(.{id,extractor_key,title,thumbnail,duration,uploader,filesize,filesize_approx,resolution,abr,ext,webpage_url})j"
     )

     request.addOption("--progress")

     if (sleepRequest > 0) {
          request.addOption("--sleep-requests", sleepRequest)
     }

     if (fragRetry > 10) {
          request.addOption("--fragment-retries", argument = fragRetry)
     }

     if (retries > 10) {
          request.addOption("--retries", retries)
     }

     if (sponsorBlock && sponsorBlockCategories.isNotEmpty()) {
          request.addOption("--sponsorblock-remove", sponsorBlockCategories.joinToString(","))
     }

     if (showDetails) {
          request.addOption("-v")
     }

     val shouldUseAria2c = when (aria2cMode) {
          Aria2cMode.Always -> true
          Aria2cMode.EdgeCaseOnly -> videoQuality != VideoQuality.HD1080
          Aria2cMode.Disabled -> false
     }

     val aria2Argument = "aria2c:--summary-interval=1 -x 12 -s 12 -k 1M"

     if (shouldUseAria2c) {
          request.addOption("--downloader", "libaria2c.so")
          request.addOption(
               "--external-downloader-args", aria2Argument
          )
     }

     val hlsConcurrentFragments = 8

     when {
          shouldUseAria2c -> Unit
          externalDownloaders == ExternalDownloaders.FFmpegAsExternal -> {
               request.addOption("--hls-prefer-ffmpeg")
          }

          externalDownloaders == ExternalDownloaders.YtdlpNativeDownloader -> {
               request.addOption("--concurrent-fragments", hlsConcurrentFragments)
          }

          else -> Unit
     }

     if (embedThumbnail &&
          thumbnail != ThumbnailFormat.Off &&
          videoFormats != VideoFormat.AviFormat &&
          videoFormats != VideoFormat.WebMFormat
     ) {
          request.addOption("--convert-thumbnails", argument = thumbnail.thumbnailFormat)
          request.addOption("--embed-thumbnail")
     }

     if (playlistStatus) {
          request.addOption("--yes-playlist")
     } else {
          request.addOption("--no-playlist")
     }

     audioQuality?.let {
          request.addOption("-x")
          request.addOption("--audio-format", audioFormats.audioFormats)
          request.addOption("--audio-quality", it.audioQuality)
     }
     videoQuality?.let {
          request.addOption("-f", it.videoQuality)
     }

     quickAudioQuality?.let {
          request.addOption("-x")
          request.addOption("--audio-format", quickDownloadFormats.quickDownloadFormats)
          request.addOption("--audio-quality", it.quickDownloadAudioQuality)
     }

     quickVideoQuality?.let {
          request.addOption("-f", it.quickDownloadVideoQuality)
          request.addOption("--merge-output-format", quickDownloadFormats.quickDownloadFormats)
     }


     if (videoQuality != null && videoComp) {
          request.addOption("--recode-video", videoFormats.videoFormat)
     }

     request.addOption("-i")
     request.addOption("--embed-metadata")
     request.addOption("-o", outputPath)

     try {

          return execution(downloadDir, request, taskId, onUpdate, context)

     } finally {

          downloadDir.deleteRecursively()

     }
}
