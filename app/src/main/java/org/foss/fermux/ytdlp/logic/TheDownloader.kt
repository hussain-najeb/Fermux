package org.foss.fermux.ytdlp.logic

import android.content.Context
import android.os.Environment
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

//import com.yausername.youtubedl_android.YoutubeDLRequest
//
//suspend fun downloaderLogic(context: Context, url: String) {
//
//val dlDirectory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//
//val outputPath = "${dlDirectory?.absolutePath}/%(title)s.%(ext)s,"
//
//    val request = YoutubeDLRequest(url)
//    request.addOption("-o", outputPath)
//
//
//}



suspend fun downloaderLogic(context: Context, url: String) {
    val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    val outputPath = "${downloadDir?.absolutePath}/%(title)s.%(ext)s"

    val request = YoutubeDLRequest(url)
    request.addOption("-o", outputPath)

    withContext(Dispatchers.IO) {
        val response = YoutubeDL.getInstance().execute(request)
        Log.d("fermux", "exit=${response.exitCode}")
        Log.d("fermux", "out=${response.out}")
        Log.d("fermux", "err=${response.err}")
    }
}