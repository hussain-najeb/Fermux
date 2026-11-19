package org.foss.fermux.ffmpeg.logic

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

suspend fun ffprobeProgress(
    ffprobeBin: File,
    inputFile: File,
    libraryPath: String
): Long? = withContext(Dispatchers.IO) {
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
            environment()["LD_LIBRARY_PATH"] = libraryPath
            redirectErrorStream(true)
        }.start()

        val output = progressProcess.inputStream.bufferedReader().use { it.readText() }
        val exitCode = progressProcess.waitFor()
        if (exitCode != 0) {
            Log.d("ffprobe progress error", "failed to parse progress at: $exitCode")
            return@withContext null
        }

        output
            .lineSequence()
            .mapNotNull { it.trim().toDoubleOrNull() }
            .firstOrNull { it.isFinite() && it > 0.0 }
            ?.let { (it * 1_000_000.0).toLong() }

    } catch (e: IOException) {
        Log.e("FFmpegWorkManager", "Could not probe input duration", e)
        null
    }
}