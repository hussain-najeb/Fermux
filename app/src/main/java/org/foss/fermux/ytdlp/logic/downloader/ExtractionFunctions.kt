package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import android.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.foss.fermux.utils.fileCopyFilter
import java.io.File


/**
 *  Use the information given by [downloaderLogic] to execute the given request and save it to the downloads folder on the phone.
 */

suspend fun execution(
     downloadDir: File?, request: YoutubeDLRequest, taskId: String, onUpdate: (Float, String) -> Unit, context: Context
) {
     withContext(Dispatchers.IO) {

          val response = YoutubeDL.getInstance().execute(request, taskId) { progress, _, line ->
               onUpdate(progress, line)
          }

          fileCopyFilter(context, downloadDir, subfolderName = "downloader")

          Log.d("fermux", "exit=${response.exitCode}")
          Log.d("fermux", "out=${response.out}")
          Log.d("fermux", "err=${response.err}")
     }
}
/**
 * This function is used by [DownloadMetadata] to fill the metadata from the given url handed by [downloaderLogic] that later gets saved as a JSON file.
 */

suspend fun fetchingTheMetadata(url: String): DownloadMetadata = withContext(Dispatchers.IO) {
     val info = YoutubeDL.getInstance().getInfo(url)
     DownloadMetadata(
          title = info.title ?: "Unknown title",
          thumbnail = info.thumbnail ?: "",
          duration = info.duration,
          uploader = info.uploader
     )
}