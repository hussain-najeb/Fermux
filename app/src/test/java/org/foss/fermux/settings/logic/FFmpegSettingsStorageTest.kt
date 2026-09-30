package org.foss.fermux.settings.logic

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.foss.fermux.storage.DataStoreFFmpegSettings
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Path
import java.util.*


class FFmpegSettingsStorageTest {
     @field:TempDir
     lateinit var tempDir: Path

     private data class Fixture(val repositoryOfTheProdCode: DataStoreFFmpegSettings)

     private fun TestScope.newFixture(): Fixture {
          val file = tempDir.resolve("${UUID.randomUUID()}.preferences_pb").toFile()
          val store = PreferenceDataStoreFactory.create(
               scope = backgroundScope,
               produceFile = { file },
          )
          return Fixture(repositoryOfTheProdCode = DataStoreFFmpegSettings(store))
     }

     @Test
     fun `test for production settings`() = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setAudioBitrate("192k")
          setting.setNormalizeAudio(true)
          setting.setMonoDownmix(true)
          setting.setEnableVideoCompression(true)
          setting.setVideoResolution("720")
          setting.setVideoCrf(21)
          setting.setUseHardwareEncoder(true)
          setting.setThreadLimit(2)

          assertEquals("192k", setting.audioBitrate.first())
          assertTrue(setting.normalizeAudio.first())
          assertTrue(setting.monoDownmix.first())
          assertTrue(setting.enableVideoCompression.first())
          assertEquals("720", setting.videoResolution.first())
          assertEquals(21, setting.videoCrf.first())
          assertTrue(setting.useHardwareEncoder.first())
          assertEquals(2, setting.threadLimit.first())
     }

     @Test
     fun `defaults match shipped settings`() = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          assertEquals("", setting.audioBitrate.first())
          assertFalse(setting.normalizeAudio.first())
          assertFalse(setting.monoDownmix.first())
          assertFalse(setting.enableVideoCompression.first())
          assertEquals("", setting.videoResolution.first())
          assertEquals(23, setting.videoCrf.first())
          assertFalse(setting.useHardwareEncoder.first())
          assertEquals(0, setting.threadLimit.first())
     }

     @Test
     fun `clearing settings restores defaults`() = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setAudioBitrate("192k")
          setting.setNormalizeAudio(true)
          setting.setMonoDownmix(true)
          setting.setEnableVideoCompression(true)
          setting.setVideoResolution("720")
          setting.setVideoCrf(21)
          setting.setUseHardwareEncoder(true)
          setting.setThreadLimit(2)

          setting.resetFFmpeg()

          assertEquals("", setting.audioBitrate.first())
          assertFalse(setting.normalizeAudio.first())
          assertFalse(setting.monoDownmix.first())
          assertFalse(setting.enableVideoCompression.first())
          assertEquals("", setting.videoResolution.first())
          assertEquals(23, setting.videoCrf.first())
          assertFalse(setting.useHardwareEncoder.first())
          assertEquals(0, setting.threadLimit.first())
     }

     @Test
     fun `reset snapshot restores settings atomically`() = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setAudioBitrate("192k")
          setting.setFFmpegDebug(true)
          setting.setNormalizeAudio(true)
          setting.setMonoDownmix(true)
          setting.setEnableVideoCompression(true)
          setting.setVideoResolution("720")
          setting.setVideoCrf(21)
          setting.setUseHardwareEncoder(false)
          setting.setThreadLimit(2)

          val snapshot = setting.resetFFmpeg()
          setting.restoreFFmpeg(snapshot)

          assertEquals("192k", setting.audioBitrate.first())
          assertTrue(setting.ffmpegDebug.first())
          assertTrue(setting.normalizeAudio.first())
          assertTrue(setting.monoDownmix.first())
          assertTrue(setting.enableVideoCompression.first())
          assertEquals("720", setting.videoResolution.first())
          assertEquals(21, setting.videoCrf.first())
          assertFalse(setting.useHardwareEncoder.first())
          assertEquals(2, setting.threadLimit.first())
     }
}
