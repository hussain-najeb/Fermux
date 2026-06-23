package org.foss.fermux.ytdlp.logic

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


suspend fun downloaderLogic(context: Context, url: String, musicQuality: AudioQuality? = null, videoQuality: DownloadQuality? = null,
                            onProgress: (Float) -> Unit,

                              )

{
    val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    val outputPath = "${downloadDir?.absolutePath}/%(title)s.%(ext)s"
    val request = YoutubeDLRequest(url)



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
        val response = YoutubeDL.getInstance().execute(request) { progress, _, _ ->
            onProgress(progress)
        }
        downloadDir?.listFiles()?.forEach {
            file -> copyToDownloadFolder(context, file)
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


suspend fun copyToDownloadFolder (context: Context, sourceFile: File) {
    withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, sourceFile.name)
            put(MediaStore.Downloads.MIME_TYPE, "video/mp4")
            put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/fermux")
        }
        val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values

        ) ?: throw Exception("Error while opening download directory")

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            sourceFile.inputStream().use { inputOfSourceFile ->
                inputOfSourceFile.copyTo(outputStream)
            }
sourceFile.delete()
        }


    }
}