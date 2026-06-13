package org.foss.fermux.ytdlp.logic

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


@RequiresApi(Build.VERSION_CODES.Q)
suspend fun downloaderLogic(context: Context, url: String) {
    val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    val outputPath = "${downloadDir?.absolutePath}/%(title)s.%(ext)s"

    val request = YoutubeDLRequest(url)
    request.addOption("-o", outputPath)

    withContext(Dispatchers.IO) {
        val response = YoutubeDL.getInstance().execute(request)

        downloadDir?.listFiles()?.forEach {
            file -> CopyToDownloadFolder(context, file)
        }








        Log.d("fermux", "exit=${response.exitCode}")
        Log.d("fermux", "out=${response.out}")
        Log.d("fermux", "err=${response.err}")
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
suspend fun CopyToDownloadFolder (context: Context, sourceFile: File) {
    withContext(Dispatchers.IO) {
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, sourceFile.name)
            put(MediaStore.Downloads.MIME_TYPE, "video/mp4")
            put(MediaStore.Downloads.RELATIVE_PATH, "${Environment.DIRECTORY_DOWNLOADS}/fermux")
        }
        val uri = context.contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values

        ) ?: throw Exception("Error while opening download directory")

// WHY THE FUCK WOULD WE PASS TO OUTPUTSTREAM AGAIN???? WHY!!!!?

        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            sourceFile.inputStream().use { inputOfSourceFile ->
                inputOfSourceFile.copyTo(outputStream)
            }
sourceFile.delete()
        }


    }
}