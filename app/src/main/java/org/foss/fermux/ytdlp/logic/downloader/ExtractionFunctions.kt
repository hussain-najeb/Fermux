package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext
import org.foss.fermux.utils.DebugLog
import org.foss.fermux.utils.fileCopyFilter
import org.json.JSONObject
import java.io.File


/**
 *  Use the information given by [downloaderLogic] to execute the given request and save it to the downloads folder on the phone.
 *  The rest is copying the file from the main app's dir to the public dir.
 */

suspend fun execution(
     downloadDir: File?,
     request: YoutubeDLRequest,
     taskId: String,
     onUpdate: (Float, String) -> Unit,
     context: Context,
) {
     val response = runInterruptible(Dispatchers.IO) {
          try {
               YoutubeDL.getInstance().execute(request, taskId) { progress, _, line ->
                    onUpdate(progress, line)
               }
          } catch (e: Exception) {
               val message = e.message.orEmpty()
               val thumbnailEmbeddingFailed =
                    "EmbedThumbnailPPError" in message || "Unable to embed using ffprobe & ffmpeg" in message
               if (!thumbnailEmbeddingFailed) throw e

               DebugLog.debugDownloader("downloadWorker", "Thumbnail embedding failed; keeping media without artwork")

               onUpdate(100f, "[EmbedThumbnail] Failed; kept download without the thumbnail")
               null
          }
     }

     fileCopyFilter(context, downloadDir, subfolderName = "downloader")

     response?.let {
          DebugLog.debugDownloader("fermux", "exit=${it.exitCode}")
          DebugLog.debugDownloader("fermux", "out=${it.out}")
          DebugLog.debugDownloader("fermux", "err=${it.err}")
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
          uploader = info.uploader,
          size = info.fileSizeApproximate,
          resolution = info.resolution
     )
}

fun parseYtdlpMetadataJson(json: String): DownloadMetadata? {
     return try {
          val obj = JSONObject(json)

          fun optStringOrNull(key: String): String? {
               if (!obj.has(key) || obj.isNull(key)) return null
               return obj.optString(key).ifBlank { null }
          }

          val approxSize = obj.optDouble("filesize_approx")
               .takeIf { !it.isNaN() && it > 0 }
               ?.toLong()

          DownloadMetadata(
               title = optStringOrNull("title") ?: "Unknown title",
               thumbnail = optStringOrNull("thumbnail") ?: "",
               duration = obj.optInt("duration", 0),
               uploader = optStringOrNull("uploader"),
               size = approxSize,
               resolution = optStringOrNull("resolution")
          )
     } catch (e: Exception) {
          DebugLog.errorDownloader("downloader JSON metadata parsing", "JSON metadata failed to be parsed in some way", e)
          null
     }
}