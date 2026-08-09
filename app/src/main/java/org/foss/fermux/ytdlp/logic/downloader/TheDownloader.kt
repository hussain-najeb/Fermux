package org.foss.fermux.ytdlp.logic.downloader

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


suspend fun downloaderLogic(
    context: Context,
     showDetails: Boolean,
     url: String,
     taskId: String,
     aria2c: Boolean = true,
     aria2cHLSWithDASHCase: Boolean = false,
     sleepRequest: Int? = null,
     musicQuality: AudioQuality? = null,
     videoQuality: VideoQuality? = null,
     sponsorBlock: Boolean = false,
     sponsorBlockCategories: Set<String> = emptySet(),
     onUpdate: (Float, String) -> Unit) {

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
     *   extractor and whats with it. RegEx should have a value that is "extractorName" called in as a regex when the regex sees
     *   there is the word "cookies" involved, so you get what site is doing the cookies and if its an issue in the first place.
     *   5- an alert dialog for the last point so its a clear thing that explains where and when and how its done!
     *   6- cookies expire!
     */

    val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    val outputPath = "${downloadDir?.absolutePath}/%(title)s.%(ext)s"
    val request = YoutubeDLRequest(url)



    if (aria2cHLSWithDASHCase && videoQuality != VideoQuality.BEST) {
        request.addOption("--downloader", "libaria2c.so")
        request.addOption("--external-downloader-args", "aria2c:--summary-interval=1")
    }
    if (aria2c) {
        request.addOption("--downloader", "libaria2c.so")
        request.addOption("--external-downloader-args", "aria2c:--summary-interval=1")

    }

    if (sleepRequest != null) {
        request.addOption("--sleep-requests", sleepRequest)
    }

    if (sponsorBlock && sponsorBlockCategories.isNotEmpty()) {
        request.addOption("--sponsorblock-remove", sponsorBlockCategories.joinToString(","))
    }
    if (showDetails) {
        request.addOption("-v")
    }


    musicQuality?.let {
        request.addOption("-x")
        request.addOption("--audio-format", "mp3")
        request.addOption("--audio-quality", it.musicQuality)
    }
    videoQuality?.let {
        request.addOption("-f", it.videoQuality)
    }

    request.addOption("-o", outputPath)

    withContext(Dispatchers.IO) {
        val existingFiles = downloadDir
            ?.listFiles()
            ?.map { it.absolutePath }
            ?.toSet()
            ?: emptySet()

        val response = YoutubeDL.getInstance().execute(request, taskId) { progress, _, line ->
            onUpdate(progress, line)
        }

        downloadDir
            ?.listFiles()
            ?.filter { it.absolutePath !in existingFiles }
            ?.forEach { file ->
                copyFileToDownloads(context, file, file.name)
            }
        Log.d("fermux", "exit=${response.exitCode}")
        Log.d("fermux", "out=${response.out}")
        Log.d("fermux", "err=${response.err}")
    }
}



    suspend fun fetchingTheMetadata(url: String): DownloadMetadata =
        withContext(Dispatchers.IO) {
            val info = YoutubeDL.getInstance().getInfo(url)
            DownloadMetadata(
                title = info.title ?: "Unknown title",
                thumbnail = info.thumbnail ?: "",
                duration = info.duration,
                uploader = info.uploader
            )
        }

    suspend fun copyFileToDownloads(
        context: Context,
        sourceFile: File,
        displayName: String,
    ) {
        withContext(Dispatchers.IO) {

            fun getMimeTypeFromFile(file: File): String {
                val extension = file.extension.lowercase()
                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                    ?: "application/octet-stream"
            }

            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, displayName)
                put(MediaStore.Downloads.MIME_TYPE, getMimeTypeFromFile(file = sourceFile))
                put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/fermux")
            }
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI, values
            ) ?: throw Exception("Error while opening download directory")

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                sourceFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }

                val deleted = sourceFile.delete()
                Log.d("fermux", "success at deleting $deleted")
                if (!deleted && sourceFile.exists()) {
                    Log.w("fermux", "Failed to delete file: ${sourceFile.absolutePath}")
                }
            }
        }
    }

@SuppressLint("DefaultLocale")
fun videoTime(seconds: Int): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val remainingSeconds = seconds % 60

    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
    } else {
        String.format("%02d:%02d", minutes, remainingSeconds)
    }
}
