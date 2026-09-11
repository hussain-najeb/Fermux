package org.foss.fermux.settings.logic

import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
import org.foss.fermux.ffmpeg.logic.FFmpegUserPrefs
import org.junit.Assert.assertEquals
import org.junit.Test

class DynamicFFmpegArgsTest {

     @Test
     fun videoWithoutTransformationsUsesStreamCopy() {
          val args = buildDynamicFFmpegArgs(FFmpegTargetFormat.MP4, FFmpegUserPrefs())
          assertEquals(listOf("-c:v", "copy", "-c:a", "copy"), args)
     }

     @Test
     fun mp4SoftwareEncodingUsesAvailableCompatibleCodecs() {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.MP4,
               FFmpegUserPrefs(
                    enableVideoCompression = true,
                    audioBitrate = "192k",
                    videoCrf = 20,
               ),
          )
          assertEquals(
               listOf("-c:v", "mpeg4", "-q:v", "20", "-c:a", "aac", "-b:a", "192k"),
               args,
          )
     }

     @Test
     fun webmEncodingUsesWebmCompatibleCodecs() {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.WEBM,
               FFmpegUserPrefs(
                    enableVideoCompression = true,
                    normalizeAudio = true,
               ),
          )
          assertEquals(
               listOf(
                    "-c:v", "vp8_mediacodec", "-b:v", "4M",
                    "-c:a", "opus", "-strict", "experimental",
                    "-af", "loudnorm",
               ),
               args,
          )
     }

     @Test
     fun aviAudioEncodingUsesMp3() {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.AVI,
               FFmpegUserPrefs(audioBitrate = "128k"),
          )
          assertEquals(
               listOf("-c:v", "copy", "-c:a", "libmp3lame", "-b:a", "128k"),
               args,
          )
     }
}
