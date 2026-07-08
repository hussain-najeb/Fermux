package org.foss.fermux.ffmpeg.logic

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import kotlinx.coroutines.runBlocking
import org.foss.fermux.ytdlp.logic.downloader.copyFileToDownloads


class FFmpegWorker(context: Context, params: WorkerParameters) :

    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val tempFile = java.io.File(applicationContext.cacheDir, "input.tmp")


        val targetFormatName = inputData.getString("TARGET_FORMAT") ?: return Result.failure()
        val targetFormat = FFmpegTargetFormat.valueOf(targetFormatName)

        val outputFile = java.io.File(applicationContext.cacheDir, "output.${targetFormat.workerFile}")

        try {
            val fileUriInput = inputData.getString("FFMPEG_URI_FILE") ?: return Result.failure()

            val uriFile = Uri.parse(fileUriInput)

            applicationContext.contentResolver.openInputStream(uriFile)?.use { inputStream ->
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            val ffmpegRequest = YoutubeDLRequest(url = "")
            ffmpegRequest.addOption("-i", tempFile.absolutePath)
            ffmpegRequest.addOption("-y")
            ffmpegRequest.addOption(outputFile.absolutePath)

            val response = YoutubeDL.getInstance().execute(ffmpegRequest) { progress, duration, line ->
                runBlocking {
                    setProgress(
                        workDataOf(
                            "progress" to progress, "duration" to duration,
                            "line" to line
                        )
                    )
                }
            }
            // TODO. add a "duration" and a progress bar here later.

            if (response.exitCode == 0) {
                val context = applicationContext
                copyFileToDownloads(
                    context,
                    outputFile,
                    "converted_${System.currentTimeMillis()}.${targetFormat.workerFile}",
                    "application/octet-stream"
                )
                return Result.success()
            } else {
                return Result.failure()
            }
        } catch (e: Exception) {
            Log.d("fermux", "error in ffmpeg file", e)
        } finally {
            if (tempFile.exists()) tempFile.delete()
            if (outputFile.exists()) outputFile.delete()
        }
        return Result.failure()
    }
}
