package org.foss.fermux.ffmpeg.logic

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.*
import org.foss.fermux.ytdlp.logic.downloader.copyFileToDownloads
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

class FFmpegWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    @SuppressLint("UseKtx")
    override suspend fun doWork(): Result {
        val tempFile = File(applicationContext.cacheDir, "input_${id}.tmp")
        val targetFormatName = inputData.getString("TARGET_FORMAT") ?: return Result.failure(workDataOf("error" to "Missing TARGET_FORMAT in input data"))
        val targetFormat = FFmpegTargetFormat.valueOf(targetFormatName)
        val outputFile = File(applicationContext.cacheDir, "output_${id}.${targetFormat.workerFile}")
        val originalName = inputData.getString("ORIGINAL_FILE_NAME") ?: "Converted_to_$id"
        val baseName = originalName.substringBeforeLast(".")
        val displayName = "$baseName.${targetFormat.workerFile}"


        return try {
            val fileUriInput = inputData.getString("FFMPEG_URI_FILE") ?: 
            return Result.failure(workDataOf("error" to "Missing FFMPEG_URI_FILE in input data"))

            val uriFile = Uri.parse(fileUriInput)


            applicationContext.contentResolver.openInputStream(uriFile)?.use { inputStream ->
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            } ?: return Result.failure(workDataOf("error" to "Could not open input stream for $uriFile — permission may have been lost"))

            val nativeLibDir = applicationContext.applicationInfo.nativeLibraryDir
            val ffmpegBinary = File(nativeLibDir, "libfermux_ffmpeg.so")

            if (!ffmpegBinary.exists()) {
                Log.d("FFmpegWorkManager", "FFmpeg binary not found at: $ffmpegBinary")
                return Result.failure(
                    workDataOf("error" to "FFmpeg binary not found")
                )
            }

            val extraArgs = inputData.getStringArray("FFMPEG_EXTRA_ARGS")?.toList() ?: emptyList()

            val process = withContext(Dispatchers.IO) {
                val builder = ProcessBuilder(
                    buildList {
                        add(ffmpegBinary.absolutePath)
                        add("-i")
                        add(tempFile.absolutePath)
                        addAll(extraArgs)
                        add("-y")
                        add(outputFile.absolutePath)
                    }
                )

                builder.environment()["LD_LIBRARY_PATH"] = nativeLibDir
                builder.redirectErrorStream(true)
                builder.start()
            }

            currentCoroutineContext()[Job]?.invokeOnCompletion { handler ->
                if (handler is CancellationException)
                    process.destroy()
            }

            val output = StringBuilder()
            withContext(Dispatchers.IO) {
                BufferedReader(InputStreamReader(process.inputStream)).use { reader ->
                    var line: String?
                    var lastUpdateAt = 0L
                    while (reader.readLine().also { line = it } != null) {
                        output.appendLine(line)
                        Log.d("Fermux FFmpeg Output", line!!)
                        val now = System.currentTimeMillis()
                        if (now - lastUpdateAt >= 500L) {
                            lastUpdateAt = now
                            setProgress(workDataOf("line" to line))
                        }
                    }
                }
            }

            val exitCode = withContext(Dispatchers.IO) {
                process.waitFor()
            }
            if (exitCode == 0) {
                withContext(Dispatchers.IO) {
                    copyFileToDownloads(
                        applicationContext,
                        outputFile,
                        displayName
                    )
                    Result.success()
                }
            } else {
                val logs = output.toString()
                Log.e("fermux", "FFmpeg failed with rc: $exitCode\n$logs")
                Result.failure(workDataOf("error" to logs))
            }
        } catch (e: Exception) {
            Log.e("fermuxFFmpeg","FFmpeg worker crashed", e)
            Result.failure(workDataOf("error" to (e.message ?: "unknown error")))
        } finally {
            if (tempFile.exists()) tempFile.delete()
            if (outputFile.exists()) outputFile.delete()
        }
    }
}
