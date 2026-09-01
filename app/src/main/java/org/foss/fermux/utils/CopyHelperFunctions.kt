package org.foss.fermux.utils

import android.content.Context
import android.os.Environment
import java.io.File

suspend fun fileCopyFilter(
     context: Context,
     privateDirectory: File?,
     subfolderName: String,
) {
     val publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
     val fermuxPublicDirectory = File(publicDirectory, "fermux/$subfolderName")
     val fermuxListfiles = fermuxPublicDirectory.listFiles()?.map { it.name }?.toSet() ?: emptySet()

     privateDirectory?.listFiles()?.forEach { file ->
          if (file.name !in fermuxListfiles) {
               copyFileToDownloads(context, file, file.name, subFolder = "fermux/$subfolderName")
          } else {
               file.delete()
          }
     }
}

