package org.foss.fermux.ytdlp.logic.downloader

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*


/**
 *  A function to calculate the time and hand it to [org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates.FinishedCard]
 */
fun videoTime(seconds: Int): String {
     val hours = seconds / 3600
     val minutes = (seconds % 3600) / 60
     val remainingSeconds = seconds % 60

     val twoDigits = DecimalFormat("00", DecimalFormatSymbols(Locale.US))
     return if (hours > 0) {
          "${twoDigits.format(hours)}:${twoDigits.format(minutes)}:${twoDigits.format(remainingSeconds)}"
     } else {
          "${twoDigits.format(minutes)}:${twoDigits.format(remainingSeconds)}"
     }
}

fun likeFormatting(like: String?): String {
     return like?.toLongOrNull()?.let {
          DecimalFormat("#,###").format(it)
     } ?: "0"
}

fun sizeFormatting(byte: Long): String {

     val kb = 1024.0
     val mb = kb * 1024.0
     val gb = mb * 1024.0
     val tb = gb * 1024.0

     val sizeFormatter = DecimalFormat("#,##0.#")

     return when {
          byte >= tb -> "${sizeFormatter.format(byte / tb)} TB"
          byte >= gb -> "${sizeFormatter.format(byte / gb)} GB"
          byte >= mb -> "${sizeFormatter.format(byte / mb)} MB"
          byte >= kb -> "${sizeFormatter.format(byte / kb)} KB"
          else -> "$byte B"
     }
}