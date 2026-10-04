package org.foss.fermux.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openMedia(media: Uri) {
     val intent = Intent(Intent.ACTION_VIEW).apply {
          setDataAndType(media, "*/*")
          addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
     }
     startActivity(intent)
}