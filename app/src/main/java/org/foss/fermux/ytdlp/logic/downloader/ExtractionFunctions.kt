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
     downloadDir: File?,
     request: YoutubeDLRequest,
     taskId: String,
     onUpdate: (Float, String) -> Unit,
     context: Context,
) {
     withContext(Dispatchers.IO) {
          val response = try {
               YoutubeDL.getInstance().execute(request, taskId) { progress, _, line ->
                    onUpdate(progress, line)
               }
          } catch (error: Exception) {
               val message = error.message.orEmpty()
               val thumbnailEmbeddingFailed =
                    "EmbedThumbnailPPError" in message ||
                         "Unable to embed using ffprobe & ffmpeg" in message

               if (!thumbnailEmbeddingFailed) throw error

               Log.w("downloadWorker", "Thumbnail embedding failed; keeping media without artwork")
               onUpdate(100f, "[EmbedThumbnail] Failed; kept download without artwork")
               null
          }

          fileCopyFilter(context, downloadDir, subfolderName = "downloader")

          response?.let {
               Log.d("fermux", "exit=${it.exitCode}")
               Log.d("fermux", "out=${it.out}")
               Log.d("fermux", "err=${it.err}")
          }
     }
}
/**
 * This function is used by [DownloadMetadata] to fill the metadata from the given url handed by [downloaderLogic] that later gets saved as a JSON file.
 */

suspend fun fetchingTheMetadata(url: String): DownloadMetadata = withContext(Dispatchers.IO) {
     val request = YoutubeDLRequest(url).apply {
          addOption("--no-playlist")
     }
     val info = YoutubeDL.getInstance().getInfo(request)
     DownloadMetadata(
          title = info.title ?: "Unknown title",
          thumbnail = info.thumbnail ?: "",
          duration = info.duration,
          uploader = info.uploader
     )
}
