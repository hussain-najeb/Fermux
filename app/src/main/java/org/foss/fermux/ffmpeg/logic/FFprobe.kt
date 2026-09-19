package org.foss.fermux.ffmpeg.logic

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.foss.fermux.utils.DebugLog
import java.io.File
import java.io.IOException

suspend fun ffprobeProgress(
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