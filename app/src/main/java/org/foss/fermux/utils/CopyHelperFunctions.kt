package org.foss.fermux.utils

import android.content.Context
import java.io.File

private val IGNORED_EXTENSIONS = setOf("part", "ytdl", "json", "jpg", "jpeg", "png", "webp", "vtt", "srt")

suspend fun fileCopyFilter(
     context: Context,
     privateDirectory: File?,
     subfolderName: String,
): List<CopiedFile> = privateDirectory?.listFiles()
     .orEmpty()
     .filter {
          it.isFile && it.extension.lowercase() !in IGNORED_EXTENSIONS
     }
     .map {
          copyFileToDownloads(context, it, it.name, "fermux/$subfolderName")
     }