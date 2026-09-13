package org.foss.fermux.settings.logic

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.foss.fermux.storage.DEFAULT_SPONSOR_BLOCK_CATEGORIES
import org.foss.fermux.storage.DataStoreDownloaderSettings
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode
import org.foss.fermux.ytdlp.logic.downloader.ExternalDownloaders
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.nio.file.Path
import java.util.*


class DownloaderSettingsTest {
     @field:TempDir
     lateinit var tempDir: Path

     private data class Fixture(val repositoryOfTheProdCode: DataStoreDownloaderSettings)

     private fun TestScope.newFixture(): Fixture {
          val file = tempDir.resolve("${UUID.randomUUID()}.preferences_pb").toFile()
          val store = PreferenceDataStoreFactory.create (
               scope = backgroundScope,
               produceFile = { file }
          )
          return Fixture(repositoryOfTheProdCode = DataStoreDownloaderSettings(store))
     }

     @Test
     fun `test for production settings`() = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          val jsonInfo = JSONHistoryCards(
               title = "Something Something Title",
               thumbnail = "Something Something thumbnail",
               url = "Something Something URL",
               videoDuration = 4632533,
               downloadTime = 335632
          )
          val categories = setOf("something something categories", "Something Something categories")

          setting.setDownloadPath("/downloads")
          setting.setJSONVideo(jsonInfo)
          setting.setJSONAudio(jsonInfo)
          setting.setVideoHistory(false)
          setting.setAudioHistory(false)
          setting.setPlaylistStatus(false)
          setting.setSponsorBlockCategories(categories)
          setting.setSponsorBlock(false)
          setting.setYtdlpDetails(false)
          setting.setFingerprinting(false)
          setting.setQuickJS(false)
          setting.setEmbedThumbnail(false)
          setting.setSleepRequest(1)
          setting.setNotificationState(false)

          assertEquals("/downloads", setting.downloadPath.first())
          assertEquals(listOf(jsonInfo), setting.jsonVideoCard.first())
          assertEquals(listOf(jsonInfo), setting.jsonAudioCard.first())
          assertFalse(setting.videoHistory.first())
          assertFalse(setting.audioHistory.first())
          assertFalse(setting.playlistStatus.first())
          assertEquals(categories,setting.sponsorBlockCategories.first())
          assertFalse(setting.sponsorBlock.first())
          assertFalse(setting.ytdlpDetails.first())
          assertFalse(setting.fingerprinting.first())
          assertFalse(setting.quickJS.first())
          assertFalse(setting.embedThumbnail.first())
          assertEquals(1, setting.sleepRequest.first())
          assertFalse(setting.notificationState.first())
     }

     @ParameterizedTest
     @EnumSource(ExternalDownloaders::class)
     fun `test external downloaders and aria2 enums`(externalDownloaders: ExternalDownloaders) = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setExternalDownloader(externalDownloaders)
          assertEquals(externalDownloaders, setting.externalDownloaders.first())

          val expectedAria2cMode = if (externalDownloaders == ExternalDownloaders.TurnedOff) Aria2cMode.Always else Aria2cMode.Disabled
          assertEquals(expectedAria2cMode, setting.aria2cMode.first())

     }

     @ParameterizedTest
     @EnumSource(Aria2cMode::class)
     fun `text aria2 enums with external downloaders`(aria2cMode: Aria2cMode) = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setExternalDownloader(ExternalDownloaders.FFmpegAsExternal)
          setting.setAria2cMode(aria2cMode)

          assertEquals(aria2cMode, setting.aria2cMode.first())

          val externalDownloaderTests = if (aria2cMode == Aria2cMode.Disabled) ExternalDownloaders.FFmpegAsExternal else ExternalDownloaders.TurnedOff

          assertEquals(externalDownloaderTests, setting.externalDownloaders.first())
     }

     @Test
     fun `clearing settings restores defaults`() = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setDownloadPath("/downloads")
          setting.setNotificationState(false)
          setting.setSleepRequest(10)
          setting.setExternalDownloader(ExternalDownloaders.FFmpegAsExternal)
          setting.setYtdlpDetails(false)
          setting.setVideoHistory(false)
          setting.setAudioHistory(false)
          setting.setEmbedThumbnail(false)
          setting.setPlaylistStatus(false)
          setting.setSponsorBlock(false)
          setting.setSponsorBlockCategories(setOf("custom-category"))
          setting.setQuickJS(false)
          setting.setFingerprinting(false)


          setting.clearYtdlp()


          assertEquals("", setting.downloadPath.first())
          assertTrue(setting.notificationState.first())
          assertEquals(0, setting.sleepRequest.first())
          assertEquals(Aria2cMode.Always, setting.aria2cMode.first())
          assertEquals(ExternalDownloaders.TurnedOff, setting.externalDownloaders.first())
          assertTrue(setting.ytdlpDetails.first())
          assertTrue(setting.videoHistory.first())
          assertTrue(setting.audioHistory.first())
          assertTrue(setting.embedThumbnail.first())
          assertFalse(setting.playlistStatus.first())
          assertTrue(setting.sponsorBlock.first())
          assertEquals(DEFAULT_SPONSOR_BLOCK_CATEGORIES, setting.sponsorBlockCategories.first())
          assertTrue(setting.quickJS.first())
          assertTrue(setting.fingerprinting.first())
     }

     @Test
     fun `testing the turned off state of eternal downloader`() = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setAria2cMode(Aria2cMode.EdgeCaseOnly)
          setting.setExternalDownloader(ExternalDownloaders.TurnedOff)

          assertEquals(Aria2cMode.EdgeCaseOnly, setting.aria2cMode.first())
     }



}