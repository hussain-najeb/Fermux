package org.foss.fermux.ytdlp.logic.downloader

import android.content.Context
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runInterruptible
import org.foss.fermux.utils.DebugLogDownloader
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

               DebugLogDownloader.debugDownloader("downloadWorker", "Thumbnail embedding failed; keeping media without artwork")

               onUpdate(100f, "[EmbedThumbnail] Failed; kept download without the thumbnail")
               null
          }
     }

     fileCopyFilter(context, downloadDir, subfolderName = "downloader")

     response?.let {
          DebugLogDownloader.debugDownloader("fermux", "exit=${it.exitCode}")
          DebugLogDownloader.debugDownloader("fermux", "out=${it.out}")
          DebugLogDownloader.debugDownloader("fermux", "err=${it.err}")
     }
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
          val audioQuality = obj.optDouble("abr")
               .takeIf { !it.isNaN() && it > 0 }
          val audioFormat = obj.optString("ext")

          val videoFormat = obj.optString("ext")

          DownloadMetadata(

               title = optStringOrNull("title") ?: "Unknown title",
               thumbnail = optStringOrNull("thumbnail") ?: "",
               duration = obj.optInt("duration", 0),
               uploader = optStringOrNull("uploader"),
               size = approxSize,
               audioFormat = audioFormat,
               audioQuality = audioQuality,
               videoFormat = videoFormat,
               resolution = optStringOrNull("resolution")
          )
     } catch (e: Exception) {
          DebugLogDownloader.errorDownloader("downloader JSON metadata parsing", "JSON metadata failed to be parsed in some way", e)
          null
     }
}