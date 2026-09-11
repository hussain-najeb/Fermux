package org.foss.fermux.settings.logic

import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
import org.foss.fermux.ffmpeg.logic.FFmpegUserPrefs
import org.foss.fermux.ffmpeg.logic.MediaKind


fun buildDynamicFFmpegArgs(
     targetFormat: FFmpegTargetFormat,
     prefs: FFmpegUserPrefs
): List<String> {
     val args = targetFormat.ffmpegExtraArgs.toMutableList()

     when (targetFormat.category) {

          MediaKind.VIDEO -> {
               val profile = requireNotNull(targetFormat.videoEncodingProfile) {
                    "Missing video encoding profile for $targetFormat"
               }
               val wantsVideoReencode = prefs.enableVideoCompression || prefs.videoResolution != null
               val wantsAudioReencode = prefs.audioBitrate != null || prefs.normalizeAudio || prefs.monoDownmix

               if (wantsVideoReencode) {
                    if (prefs.useHardwareEncoder) {
                         args += profile.hardwareVideoArgs
                    } else {
                         args += profile.softwareVideoArgs
                         profile.softwareQualityOption?.let {
                              args += listOf(it, (prefs.videoCrf ?: 23).coerceIn(1, 31).toString())
                         }
                    }
                    prefs.videoResolution?.let {
                         args += listOf("-vf", "scale=-2:$it")
                    }
               } else {
                    args += listOf("-c:v", "copy")
               }

               if (wantsAudioReencode) {
                    args += profile.audioArgs
                    prefs.audioBitrate?.let { args += listOf("-b:a", it) }
                    if (prefs.monoDownmix) args += listOf("-ac", "1")
                    if (prefs.normalizeAudio) args += listOf("-af", "loudnorm")
               } else {
                    args += listOf("-c:a", "copy")
               }
          }

          MediaKind.AUDIO -> {
               prefs.audioBitrate?.let { args += listOf("-b:a", it) }
               if (prefs.monoDownmix) args += listOf("-ac", "1")
               if (prefs.normalizeAudio) args += listOf("-af", "loudnorm")
          }

          MediaKind.IMAGE -> {
               // Format-specific base arguments were added above.
          }

          MediaKind.IDLE -> {}
     }

     prefs.threadLimit?.let {
          args += listOf("-threads", it.toString())
     }

     return args
}
