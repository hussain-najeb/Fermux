package org.foss.fermux.main

import android.content.ContentValues
import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun copyFileToDownloads(
     context: Context,
     sourceFile: File,
     displayName: String = sourceFile.name,
     subFolder: String = "fermux"
) {
     withContext(Dispatchers.IO) {
          val extension = sourceFile.extension.lowercase()
          val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
               ?: "application/octet-stream"

          val relativePath = if (subFolder.isNotBlank()) {
               "${Environment.DIRECTORY_DOWNLOADS}/$subFolder"
          } else {
               Environment.DIRECTORY_DOWNLOADS
          }

          val values = ContentValues().apply {
               put(MediaStore.Downloads.DISPLAY_NAME, displayName)
               put(MediaStore.Downloads.MIME_TYPE, mimeType)
               put(MediaStore.Downloads.RELATIVE_PATH, relativePath)
          }

          val uri = context.contentResolver.insert(
               MediaStore.Downloads.EXTERNAL_CONTENT_URI, values
          ) ?: throw Exception("Error while opening download directory")

          context.contentResolver.openOutputStream(uri)?.use { outputStream ->
               sourceFile.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
               }
          }

          val deleted = sourceFile.delete()
          Log.d("fermux", "success at deleting $deleted")
          if (!deleted && sourceFile.exists()) {
               Log.w("fermux", "Failed to delete file: ${sourceFile.absolutePath}")
          }
     }
}