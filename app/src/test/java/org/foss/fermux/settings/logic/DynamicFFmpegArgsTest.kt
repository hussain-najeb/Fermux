package org.foss.fermux.settings.logic

import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
import org.foss.fermux.ffmpeg.logic.FFmpegUserPrefs
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource



// TODO. This is a test for ffmpeg pre: Crop, resizing, and any effect past the default impl that the app shipped with, test these features later when implemented

class DynamicFFmpegArgsTest {

     private fun assertOption(args: List<String>, option: String, expected: String) {
          assertEquals(1, args.count { it == option }, "Expected exactly one $option in $args")
          assertEquals(expected, args.getOrNull(args.indexOf(option) + 1), option)
     }

     @ParameterizedTest
     @CsvSource(
          "MP4, h264_mediacodec",
          "MKV, h264_mediacodec",
          "MOV, h264_mediacodec",
          "AVI, mpeg4_mediacodec",
          "WEBM, vp8_mediacodec",
     )
     fun hardwareEncodingSelectsTheTargetEncoder(format: FFmpegTargetFormat, encoder: String) {
          val args = buildDynamicFFmpegArgs(
               format,
               FFmpegUserPrefs(enableVideoCompression = true, useHardwareEncoder = true),
          )

          assertOption(args, "-c:v", encoder)
          assertOption(args, "-b:v", "4M")
          assertOption(args, "-c:a", "copy")
          assertFalse("-q:v" in args)
          assertFalse("-crf" in args)
     }

     @Test
     fun hardwareToggleAloneDoesNotTriggerReencoding() {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.MP4,
               FFmpegUserPrefs(useHardwareEncoder = true),
          )

          assertEquals(listOf("-c:v", "copy", "-c:a", "copy"), args)
     }

     @ParameterizedTest
     @ValueSource(booleans = [false, true])
     fun resolutionTriggersEncodingEvenWithCompressionDisabled(hardware: Boolean) {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.MP4,
               FFmpegUserPrefs(videoResolution = "720", useHardwareEncoder = hardware),
          )

          assertOption(args, "-c:v", if (hardware) "h264_mediacodec" else "mpeg4")
          assertOption(args, "-vf", "scale=-2:720")
          assertOption(args, "-c:a", "copy")
     }

     @ParameterizedTest
     @EnumSource(FFmpegTargetFormat::class, names = ["MP4", "MKV", "MOV", "AVI"])
     fun softwareCompressionCurrentlyUsesMpeg4QualityScale(format: FFmpegTargetFormat) {
          val args = buildDynamicFFmpegArgs(
               format,
               FFmpegUserPrefs(enableVideoCompression = true, videoCrf = 18),
          )

          assertOption(args, "-c:v", "mpeg4")
          assertOption(args, "-q:v", "18")
          assertOption(args, "-c:a", "copy")
          assertFalse("-crf" in args)
          assertFalse("-b:v" in args)
     }

     @ParameterizedTest
     @CsvSource("-10, 1", "1, 1", "20, 20", "31, 31", "60, 31")
     fun softwareQualityIsClampedToEncoderRange(requested: Int, expected: Int) {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.MP4,
               FFmpegUserPrefs(enableVideoCompression = true, videoCrf = requested),
          )

          assertOption(args, "-q:v", expected.toString())
     }

     @Test
     fun softwareCompressionUsesDefaultQualityWhenUnset() {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.MP4,
               FFmpegUserPrefs(enableVideoCompression = true),
          )

          assertOption(args, "-q:v", "23")
     }

     @Test
     fun qualitySettingAloneDoesNotTriggerReencoding() {
          val args = buildDynamicFFmpegArgs(FFmpegTargetFormat.MP4, FFmpegUserPrefs(videoCrf = 18))

          assertEquals(listOf("-c:v", "copy", "-c:a", "copy"), args)
     }

     @Test
     fun hardwareEncodingCurrentlyIgnoresQualitySlider() {
          val prefs = FFmpegUserPrefs(enableVideoCompression = true, useHardwareEncoder = true)
          val low = buildDynamicFFmpegArgs(FFmpegTargetFormat.MP4, prefs.copy(videoCrf = 18))
          val high = buildDynamicFFmpegArgs(FFmpegTargetFormat.MP4, prefs.copy(videoCrf = 28))

          assertEquals(low, high)
          assertOption(low, "-b:v", "4M")
     }

     @Test
     fun webmCurrentlyUsesMediaCodecEvenWhenHardwareEncodingIsOff() {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.WEBM,
               FFmpegUserPrefs(enableVideoCompression = true, useHardwareEncoder = false),
          )

          assertOption(args, "-c:v", "vp8_mediacodec")
          assertOption(args, "-b:v", "4M")
     }

     @ParameterizedTest
     @CsvSource("MP4, aac", "MKV, aac", "MOV, aac", "AVI, libmp3lame", "WEBM, opus")
     fun normalizationReencodesAudioWithoutReencodingVideo(format: FFmpegTargetFormat, encoder: String) {
          val args = buildDynamicFFmpegArgs(format, FFmpegUserPrefs(normalizeAudio = true))

          assertOption(args, "-c:v", "copy")
          assertOption(args, "-c:a", encoder)
          assertOption(args, "-af", "loudnorm")
          assertFalse("-vf" in args)
     }

     @ParameterizedTest
     @CsvSource("WAV, pcm_s16le", "MP3, libmp3lame", "M4A, aac", "FLAC, flac", "OGG, vorbis")
     fun normalizationWorksWithAudioTargetArguments(format: FFmpegTargetFormat, encoder: String) {
          val args = buildDynamicFFmpegArgs(format, FFmpegUserPrefs(normalizeAudio = true))

          assertOption(args, "-c:a", encoder)
          assertOption(args, "-af", "loudnorm")
          assertEquals(1, args.count { it == "-vn" })
          assertFalse("-c:v" in args)
     }

     @Test
     fun monoDownmixAloneReencodesAudio() {
          val args = buildDynamicFFmpegArgs(FFmpegTargetFormat.MP4, FFmpegUserPrefs(monoDownmix = true))

          assertEquals(listOf("-c:v", "copy", "-c:a", "aac", "-ac", "1"), args)
     }

     @Test
     fun disablingNormalizationKeepsOtherAudioSettings() {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.MP3,
               FFmpegUserPrefs(audioBitrate = "128k", monoDownmix = true, normalizeAudio = false),
          )

          assertOption(args, "-b:a", "128k")
          assertOption(args, "-ac", "1")
          assertFalse("-af" in args)
     }

     @ParameterizedTest
     @ValueSource(booleans = [false, true])
     fun videoAndAudioTransformationsCanBeCombined(hardware: Boolean) {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.MP4,
               FFmpegUserPrefs(
                    enableVideoCompression = true,
                    useHardwareEncoder = hardware,
                    videoResolution = "480",
                    videoCrf = 20,
                    audioBitrate = "128k",
                    normalizeAudio = true,
                    monoDownmix = true,
                    threadLimit = 2,
               ),
          )

          assertOption(args, "-c:v", if (hardware) "h264_mediacodec" else "mpeg4")
          assertOption(args, "-vf", "scale=-2:480")
          assertOption(args, "-c:a", "aac")
          assertOption(args, "-b:a", "128k")
          assertOption(args, "-af", "loudnorm")
          assertOption(args, "-ac", "1")
          assertOption(args, "-threads", "2")
          assertFalse("copy" in args)
     }

     @Test
     fun automaticThreadLimitDoesNotEmitAnOverride() {
          val args = buildDynamicFFmpegArgs(FFmpegTargetFormat.MP4, FFmpegUserPrefs())

          assertFalse("-threads" in args)
     }

     @ParameterizedTest
     @EnumSource(FFmpegTargetFormat::class, names = ["GIF", "JPG", "PNG"])
     fun imageTargetsIgnoreVideoAndAudioSettings(format: FFmpegTargetFormat) {
          val args = buildDynamicFFmpegArgs(
               format,
               FFmpegUserPrefs(
                    enableVideoCompression = true,
                    useHardwareEncoder = true,
                    videoResolution = "720",
                    videoCrf = 18,
                    audioBitrate = "128k",
                    normalizeAudio = true,
                    monoDownmix = true,
               ),
          )

          assertEquals(if (format == FFmpegTargetFormat.GIF) emptyList() else listOf("-frames:v", "1"), args)
     }

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

          assertEquals(listOf("-c:v", "mpeg4", "-q:v", "20", "-c:a", "aac", "-b:a", "192k"), args)
     }

     @Test
     fun webmEncodingUsesWebmCompatibleCodecs() {
          val args = buildDynamicFFmpegArgs(
               FFmpegTargetFormat.WEBM,
               FFmpegUserPrefs(enableVideoCompression = true, normalizeAudio = true),
          )

          assertEquals(
               listOf(
                    "-c:v",
                    "vp8_mediacodec",
                    "-b:v",
                    "4M",
                    "-c:a",
                    "opus",
                    "-strict",
                    "experimental",
                    "-af",
                    "loudnorm",
               ),
               args,
          )
     }

     @Test
     fun aviAudioEncodingUsesMp3() {
          val args = buildDynamicFFmpegArgs(FFmpegTargetFormat.AVI, FFmpegUserPrefs(audioBitrate = "128k"))

          assertEquals(listOf("-c:v", "copy", "-c:a", "libmp3lame", "-b:a", "128k"), args)
     }
}
