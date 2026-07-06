package org.foss.fermux.ffmpeg.logic

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest


class FFmpegWorker(context: Context, params: WorkerParameters,  ) :

    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val tempFile = java.io.File(applicationContext.cacheDir, "input.tmp")

        val outputFile = java.io.File(applicationContext.cacheDir, "output")

        val fileUriInput = inputData.getString("FFMPEG_URI_FILE") ?: return Result.failure()

        val uriFile = Uri.parse(fileUriInput)

     applicationContext.contentResolver.openInputStream(uriFile)?.use { inputStream ->
         tempFile.outputStream().use { outputStream ->
             inputStream.copyTo(outputStream)
         }
     }

        val ffmpegRequest = YoutubeDLRequest(url = "")
        ffmpegRequest.addOption("-i", tempFile.absolutePath)
        ffmpegRequest.addOption(outputFile.absolutePath)

        val response = YoutubeDL.getInstance().execute(ffmpegRequest) {progress, duration, line ->
            kotlinx.coroutines.runBlocking {
                setProgress(androidx.work.workDataOf("progress" to progress))
            }
        }
        // TODO. add a "duration" and a progress bar here later.


        if (response.exitCode == 0) {
            return Result.success()
        } else {
            return Result.failure()
        }
    }
}
