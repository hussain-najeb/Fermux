package org.foss.fermux.ffmpeg.logic

import android.content.Context
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.foss.fermux.settings.logic.buildDynamicFFmpegArgs
import org.foss.fermux.storage.FFmpegSettingsTab
import org.foss.fermux.utils.DebugLog
import org.foss.fermux.utils.copyFileToDownloads
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import kotlin.coroutines.cancellation.CancellationException

class FFmpegWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

     override suspend fun doWork(): Result {  //TODO. Make this forground and with notif, same as downloader.
          // TODO. Add proper linux pid cancelling, maybe something that looks like the yt-dlp yausername change

          return try {

               val ffmpegSettings = FFmpegSettingsTab(applicationContext)
               val prefs = FFmpegUserPrefs(
                    audioBitrate = ffmpegSettings.audioBitrate.first().takeIf { it.isNotBlank() },
                    normalizeAudio = ffmpegSettings.normalizeAudio.first(),
                    monoDownmix = ffmpegSettings.monoDownmix.first(),
                    enableVideoCompression = ffmpegSettings.enableVideoCompression.first(),
                    videoResolution = ffmpegSettings.videoResolution.first().takeIf { it.isNotBlank() },
                    videoCrf = ffmpegSettings.videoCrf.first(),
                    useHardwareEncoder = ffmpegSettings.useHardwareEncoder.first(),
                    threadLimit = ffmpegSettings.threadLimit.first().takeIf { it > 0 }
               )

               val targetFormatName = inputData.getString("TARGET_FORMAT")
                    ?: return Result.failure(workDataOf("error" to "Missing TARGET_FORMAT in input data"))
               val targetFormat = FFmpegTargetFormat.valueOf(targetFormatName)

               val tempFile = File(applicationContext.cacheDir, "input_${id}.tmp")
               val outputFile = File(applicationContext.cacheDir, "output_${id}.${targetFormat.workerFile}")


               try {

                    val fileUriInput = inputData.getString("FFMPEG_URI_FILE")
                         ?: return Result.failure(workDataOf("error" to "Missing FFMPEG_URI_FILE in input data"))


                    val originalName = inputData.getString("ORIGINAL_FILE_NAME") ?: "Converted_to_$id"
                    val baseName = originalName.substringBeforeLast(".")
                    val displayName = "$baseName.${targetFormat.workerFile}"

                    val args = buildDynamicFFmpegArgs(targetFormat, prefs)

                    val uriFile = fileUriInput.toUri()

                    applicationContext.contentResolver.openInputStream(uriFile)?.use { inputStream ->
                         tempFile.outputStream().use { outputStream ->
                              inputStream.copyTo(outputStream)
                         }
                    }
                         ?: return Result.failure(workDataOf("error" to "Could not open input stream for $uriFile, permission may have been lost"))

                    val nativeLibDir = applicationContext.applicationInfo.nativeLibraryDir
                    val ffmpegBinary = File(nativeLibDir, "libfermux_ffmpeg.so")
                    val ffprobeBinary = File(nativeLibDir, "libfermux_ffprobe.so")

                    if (!ffmpegBinary.exists() || !ffprobeBinary.exists()) {
                         DebugLog.debugFFmpeg("ffmpegBinary", "FFmpeg lib binaries not found at: $ffmpegBinary, $ffprobeBinary"  )
                         return Result.failure(
                              workDataOf("error" to "ffmpeg lib binary not found")
                         )
                    }

                    val ffprobeInfo = ffprobeProgress(
                         ffprobeBinary,
                         tempFile,
                         nativeLibDir
                    )

                    val process = withContext(Dispatchers.IO) {
                         ProcessBuilder(
                              buildList {
                                   add(ffmpegBinary.absolutePath)
                                   add("-progress")
                                   add("pipe:1")
                                   add("-nostats")
                                   add("-hwaccel")
                                   add("none")
                                   add("-i")
                                   add(tempFile.absolutePath)
                                   addAll(args)
                                   add("-y")
                                   add(outputFile.absolutePath)
                              }
                         ).apply {
                              environment()["LD_LIBRARY_PATH"] = nativeLibDir
                              redirectErrorStream(true)

                         }.start()
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
                              var currentProgress = 0f

                              while (reader.readLine().also { line = it } != null) {
                                   val logOutput = line!!
                                   output.appendLine(logOutput)
                                   DebugLog.debugFFmpeg("Fermux FFmpeg Output", logOutput)
                                   val now = System.currentTimeMillis()
                                   var progressDoneSign = false

                                   val separator = logOutput.indexOf("=")
                                   if (separator > 0) {
                                        val progressKey = logOutput.substring(0, separator)
                                        val progressValue = logOutput.substring(separator + 1)

                                        when(progressKey) {
                                             "out_time_us", "out_time_ms" -> {
                                                  val processedTime = progressKey.toLongOrNull()
                                                  if (processedTime != null && ffprobeInfo != null && ffprobeInfo > 0L) {
                                                       currentProgress = (
                                                               processedTime.toDouble() / ffprobeInfo.toDouble() * 100.0
                                                               ).toFloat().coerceIn(0f, 99.9f)
                                                  }
                                             }

                                             "progress" -> {
                                                  if (progressValue == "end") {
                                                       currentProgress = 100f
                                                       progressDoneSign = true
                                                  }
                                             }
                                        }
                                   }

                                   if (now - lastUpdateAt >= 500L || progressDoneSign) {
                                        lastUpdateAt = now
                                        setProgress(
                                             workDataOf(
                                                  "progress" to currentProgress,
                                                  "line" to logOutput,
                                             )
                                        )
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
                                   displayName,
                                   subFolder = "fermux/converter"
                              )
                              Result.success()
                         }
                    } else {
                         val logs = output.toString().take(4_000)
                         DebugLog.debugFFmpeg("fermuxFFmpeg", "FFmpeg failed with rc: $exitCode\n$logs")
                         Result.failure(workDataOf("error" to logs))
                    }
               } catch (e: CancellationException) {
                    DebugLog.errorFFmpeg("", "", e) // TODO. Add the messages
                    throw e
               } finally {
                    if (tempFile.exists()) tempFile.delete()
                    if (outputFile.exists()) outputFile.delete()
               }
          } catch (e: Exception) {
               DebugLog.errorFFmpeg("fermuxFFmpeg", "FFmpeg worker crashed", e)
               val error = e.message
                    ?.take(4_000)
                    ?: "FFmpeg logging failed"
               Result.failure(workDataOf("error" to error))
          }
     }
}

private suspend fun ffprobeProgress(
     ffprobeBin: File,
     inputFile: File,
     nativeDir: String
): Long? {

     return withContext(Dispatchers.IO) {

          try {
               val progressProcess = ProcessBuilder(
                    ffprobeBin.absolutePath,
                    "-v",
                    "error",
                    "-show_entries",
                    "format=duration",
                    "-of",
                    "default=noprint_wrappers=1:nokey=1",
                    inputFile.absolutePath
               ).apply {
                    environment()["LD_LIBRARY_PATH"] = nativeDir
                    redirectErrorStream(true)
               }.start()

               val output = progressProcess.inputStream.bufferedReader().use { it.readText() }
               val exitCode = progressProcess.waitFor()
               if (exitCode != 0) {
                    DebugLog.debugFFmpeg("ffprobe progress error", "failed to parse progress at: $exitCode")
                    return@withContext null
               }

               output
                    .lineSequence()
                    .mapNotNull { it.trim().toDoubleOrNull() }
                    .firstOrNull { it.isFinite() && it > 0.0 }
                    ?.let { (it * 1_000_000.0).toLong()}

          } catch (e: IOException) {
               DebugLog.errorFFmpeg("FFmpegWorkManager", "Could not probe input duration", e)
               null
          }

     }
}