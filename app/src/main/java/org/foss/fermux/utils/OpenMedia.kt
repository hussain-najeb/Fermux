package org.foss.fermux.utils

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import java.io.File

fun Context.openMedia(media: Uri): Boolean {
    return try {
        val exists = when (media.scheme) {
            "file" -> media.path?.let { File(it).exists() } ?: false
            "content" -> contentResolver.openFileDescriptor(media, "r")?.use { true } ?: false
            else -> false
        }
        if (!exists) {
            Log.e("OpenMedia", "Media file does not exist or cannot be read: $media")
            return false
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(media, contentResolver.getType(media) ?: "*/*")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(intent)
        true
    } catch (e: ActivityNotFoundException) {
        Log.e("OpenMedia", "No app available to open media URI: $media", e)
        false
    } catch (e: Exception) {
        Log.e("OpenMedia", "Failed to open media URI: $media", e)
        false
    }
}