package org.foss.fermux.ytdlp.logic.oneTimeDownloader

import android.content.Context
import android.util.Log
import kotlinx.coroutines.CancellationException
import org.foss.fermux.utils.CopiedFile
import org.foss.fermux.utils.fileCopyFilter
import org.foss.youtube.next.YoutubeDL
import org.foss.youtube.next.YoutubeDLRequest
import org.json.JSONObject
import java.io.File
import java.util.*


/**
 *  Use the information given by [downloaderLogic] to execute the given request and save it to the downloads folder on the phone.
 *  The rest is copying the file from the main app's dir to the public dir.
 */

suspend fun execution(
    downloadDir: File?,
    request: YoutubeDLRequest,
    taskId: UUID,
    onUpdate: (Float, String) -> Unit,
    context: Context,
): List<CopiedFile> {

    val response = try {
        YoutubeDL.getInstance().execute(request, processId = taskId) { progress, _, line ->
            onUpdate(progress, line)
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        val message = e.message.orEmpty()
        val thumbnailEmbeddingFailed =
            "EmbedThumbnailPPError" in message || "Unable to embed using ffprobe & ffmpeg" in message
        if (!thumbnailEmbeddingFailed) throw e
        Log.d("downloadWorker", "Thumbnail embedding failed; keeping media without artwork")
        onUpdate(100f, "[EmbedThumbnail] Failed; kept download without the thumbnail")
        null
    }

    response?.let {
        Log.d("fermux", "exit=${it.exitCode}")
        Log.d("fermux", "out=${it.out}")
        Log.d("fermux", "err=${it.err}")
    }

    return fileCopyFilter(context, privateDirectory = downloadDir, subfolderName = "downloader")
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
        val format = optStringOrNull("ext")
        val mediaId = optStringOrNull("id") ?: return null
        val extractorName = optStringOrNull("extractor_key") ?: "Unknown"
        val title = optStringOrNull("title") ?: "Unknown title"
        val thumbnail = optStringOrNull("thumbnail")
        val duration = obj.optDouble("duration").takeIf { !it.isNaN() }?.toInt()
        val uploader = optStringOrNull("uploader")
        val resolution = optStringOrNull("resolution")
        val url = optStringOrNull("webpage_url")

        DownloadMetadata(
            url = url ?: return null,
            mediaId = mediaId,
            extractor = extractorName,
            title = title,
            thumbnail = thumbnail,
            duration = duration,
            uploader = uploader,
            size = approxSize,
            format = format ?: "",
            audioQuality = audioQuality,
            resolution = resolution ?: ""
        )

        // TODO. IF you add any flags, ADD ITS JSON ENTRY HERE!
        // request.addOption("--no-simulate")
        //     request.addOption(
        //          "--print",
        //          "before_dl:$FERMUX_METADATA_MARKER%(.{id,extractor_key,title,thumbnail,duration,uploader,filesize,filesize_approx,resolution,abr,ext,webpage_url})j"
        //     )

    } catch (e: Exception) {
        Log.e("downloader JSON metadata parsing", "JSON metadata failed to be parsed", e)
        null
    }
}