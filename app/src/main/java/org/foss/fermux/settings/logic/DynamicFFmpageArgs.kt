package org.foss.fermux.settings.logic

import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
import org.foss.fermux.ffmpeg.logic.FFmpegUserPrefs
import org.foss.fermux.ffmpeg.logic.MediaKind


fun BuildDynamicFFmpegArgs(
     targetFormat: FFmpegTargetFormat,
     prefs: FFmpegUserPrefs
): List<String> {
     val args = mutableListOf<String>()

     when (targetFormat.category) {

          MediaKind.VIDEO -> {

               val wantsVideoReencode = prefs.enableVideoCompression || prefs.videoResolution != null
               val wantsAudioReencode = prefs.audioBitrate != null || prefs.normalizeAudio || prefs.monoDownmix

               if (wantsVideoReencode) {
                    if (prefs.useHardwareEncoder) {
                         args += listOf("-c:v", "h264_mediacodec")
                         args += listOf("-b:v", "4M")
                    } else {
                         args += listOf("-c:v", "libx264")
                         args += listOf("-crf", (prefs.videoCrf ?: 23).toString())
                    }
                    prefs.videoResolution?.let {
                         args += listOf("-vf", "scale=-2:$it")
                    }
               } else {
                    args += listOf("-c:v", "copy")
               }

               if (wantsAudioReencode) {
                    args += listOf("-c:a", "aac")
                    prefs.audioBitrate?.let { args += listOf("-b:a", it) }
                    if (prefs.monoDownmix) args += listOf("-ac", "1")
                    if (prefs.normalizeAudio) args += listOf("-af", "loudnorm")
               } else {
                    args += listOf("-c:a", "copy")
               }
          }

          MediaKind.AUDIO -> {
               args += targetFormat.ffmpegExtraArgs
               prefs.audioBitrate?.let { args += listOf("-b:a", it) }
               if (prefs.monoDownmix) args += listOf("-ac", "1")
               if (prefs.normalizeAudio) args += listOf("-af", "loudnorm")
          }

          MediaKind.IMAGE -> {
               args += targetFormat.ffmpegExtraArgs
          }

          MediaKind.IDLE -> {}
     }

     prefs.threadLimit?.let {
          args += listOf("-threads", it.toString())
     }

     return args
}