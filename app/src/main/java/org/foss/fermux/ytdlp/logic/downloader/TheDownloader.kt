package org.foss.fermux.ytdlp.logic.downloader

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


suspend fun downloaderLogic(logText: (String) -> Unit, showDetails: Boolean, context: Context, url: String,
                            musicQuality: AudioQuality? = null, videoQuality: VideoQuality? =
                                 null, onProgress: (Float) -> Unit) {
    val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)

    val outputPath = "${downloadDir?.absolutePath}/%(title)s.%(ext)s"

    val request = YoutubeDLRequest(url)

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
        // Capture what's already there before yt-dlp runs
        val existingFiles = downloadDir
            ?.listFiles()
            ?.map { it.absolutePath }
            ?.toSet()
            ?: emptySet()

        val response = YoutubeDL.getInstance().execute(request) { progress, _, line ->
            onProgress(progress)
            logText(line)
        }

        // Only copy files that weren't there before
        downloadDir
            ?.listFiles()
            ?.filter { it.absolutePath !in existingFiles }
            ?.forEach { file ->
                copyFileToDownloads(context, file, file.name, "video/mp4")
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
        mimeType: String = "application/octet-stream"
    ) {
        withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, displayName)
                put(MediaStore.Downloads.MIME_TYPE, mimeType)
                put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/fermux")
            }
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI, values
            ) ?: throw Exception("Error while opening download directory")

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                sourceFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
                sourceFile.delete()

                val deleted = sourceFile.delete()
                if (!deleted) {
                    Log.w("fermux", "Failed to delete file: ${sourceFile.absolutePath}")
                }
            }
        }
    }
