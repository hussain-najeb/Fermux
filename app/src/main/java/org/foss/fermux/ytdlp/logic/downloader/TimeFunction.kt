package org.foss.fermux.ytdlp.logic.downloader

import android.annotation.SuppressLint

@SuppressLint("DefaultLocale")
fun videoTime(seconds: Int): String {
     val hours = seconds / 3600
     val minutes = (seconds % 3600) / 60
     val remainingSeconds = seconds % 60

     return if (hours > 0) {
          String.format("%02d:%02d:%02d", hours, minutes, remainingSeconds)
     } else {
          String.format("%02d:%02d", minutes, remainingSeconds)
     }
}