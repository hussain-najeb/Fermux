package org.foss.fermux.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log

fun Context.openMedia(media: Uri) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(media, contentResolver.getType(media) ?: "*/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Log.e("OpenMedia", "No app available to open media URI: $media", e)
    }
}