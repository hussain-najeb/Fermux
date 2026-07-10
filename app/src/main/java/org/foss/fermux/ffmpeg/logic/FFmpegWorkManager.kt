package org.foss.fermux.ffmpeg.logic

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.foss.fermux.ytdlp.logic.downloader.copyFileToDownloads
import java.io.File


class FFmpegWorker(context: Context, params: WorkerParameters) :

    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {

        val tempFile = File(applicationContext.cacheDir, "input_${id}.tmp")

        val targetFormatName = inputData.getString("TARGET_FORMAT") ?: return Result.failure()

        val targetFormat = FFmpegTargetFormat.valueOf(targetFormatName)

        val outputFile = File(applicationContext.cacheDir,
            "output_${id}.${targetFormat.workerFile}")

        try {
            val fileUriInput = inputData.getString("FFMPEG_URI_FILE") ?: return Result.failure()

            val uriFile = Uri.parse(fileUriInput)

            applicationContext.contentResolver.openInputStream(uriFile)?.use { inputStream ->
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return Result.failure()


            val ffmpegBinary = File(applicationContext.applicationInfo.nativeLibraryDir, "libffmpeg.so")

                val exitCode = withContext(Dispatchers.IO) {
                    val ffmpegProcess =
                        ProcessBuilder(
                            ffmpegBinary.absolutePath,
                            "-i", tempFile.absolutePath,
                            "-y", outputFile.absolutePath
                        )
                            .redirectErrorStream(true)
                            .start()

                    ffmpegProcess.inputStream.bufferedReader().useLines { lines ->
                        for (line in lines) {
                                setProgress(
                                    workDataOf(
                                        "line" to line
                                    )
                                )
                        }
                    }

                    ffmpegProcess.waitFor()
                }

            // TODO. add a "duration" and a progress bar here later.

            if (exitCode == 0) {
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
