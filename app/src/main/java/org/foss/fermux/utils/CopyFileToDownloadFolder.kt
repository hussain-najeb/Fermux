package org.foss.fermux.utils

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

data class CopiedFile(val uri: Uri, val sizeBytes: Long, val extension: String)

suspend fun copyFileToDownloads(
     context: Context,
     sourceFile: File,
     displayName: String = sourceFile.name,
     subFolder: String = "fermux"
): CopiedFile = withContext(Dispatchers.IO) {
     val extension = sourceFile.extension.lowercase()
     val mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
          ?: "application/octet-stream"
     val size = sourceFile.length()

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
          MediaStore.Downloads.EXTERNAL_CONTENT_URI,
          values
     ) ?: throw Exception("Error while opening download directory")

     try {
          val outputStream = context.contentResolver.openOutputStream(uri)
               ?: throw Exception("Failed to open output stream for $uri")

          outputStream.use { output ->
               sourceFile.inputStream().use { input ->
                    input.copyTo(output)
               }
          }
          CopiedFile(uri, size, extension)
     } catch (e: Exception) {
          context.contentResolver.delete(uri, null, null)
          throw e
     }

}