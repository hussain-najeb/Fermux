package org.foss.fermux.settings.logic

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.foss.fermux.dataStore.DEFAULT_SPONSOR_BLOCK_CATEGORIES
import org.foss.fermux.dataStore.DataStoreDownloaderSettings
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode
import org.foss.fermux.ytdlp.logic.downloader.Connectivity
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


          val categories = setOf("something something categories", "Something Something categories")

          setting.setDownloadPath("/downloads")
          setting.setPlaylistStatus(false)
          setting.setSponsorBlockCategories(categories)
          setting.setSponsorBlock(false)
          setting.setYtdlpDetails(false)
          setting.setFingerprinting(false)
          setting.setQuickJS(false)
          setting.setEmbedThumbnail(false)
          setting.setSleepRequest(1)
          setting.setDownloaderBellState(false)

          assertEquals("/downloads", setting.downloadPath.first())
          assertFalse(setting.playlistStatus.first())
          assertEquals(categories,setting.sponsorBlockCategories.first())
          assertFalse(setting.sponsorBlock.first())
          assertFalse(setting.ytdlpDetails.first())
          assertFalse(setting.fingerprinting.first())
          assertFalse(setting.quickJS.first())
          assertFalse(setting.embedThumbnail.first())
          assertEquals(1, setting.sleepRequest.first())
          assertFalse(setting.downloaderBellState.first())
     }

     @ParameterizedTest
     @EnumSource(ExternalDownloaders::class)
     fun `test external downloaders and aria2 enums`(externalDownloaders: ExternalDownloaders) = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setExternalDownloader(externalDownloaders)
          assertEquals(externalDownloaders, setting.externalDownloaders.first())

          assertEquals(Aria2cMode.Disabled, setting.aria2cMode.first())

     }

     @ParameterizedTest
     @EnumSource(Aria2cMode::class)
     fun `text aria2 enums with external downloaders`(aria2cMode: Aria2cMode) = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setExternalDownloader(ExternalDownloaders.FFmpegAsExternal)
          setting.setAria2cMode(aria2cMode)

          assertEquals(aria2cMode, setting.aria2cMode.first())

          val externalDownloaderTests = if (aria2cMode == Aria2cMode.Disabled) ExternalDownloaders.FFmpegAsExternal else ExternalDownloaders.Disabled

          assertEquals(externalDownloaderTests, setting.externalDownloaders.first())
     }

     @Test
     fun `clearing settings restores defaults`() = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setDownloadPath("/downloads")
          setting.setDownloaderBellState(false)
          setting.setSleepRequest(10)
          setting.setExternalDownloader(ExternalDownloaders.FFmpegAsExternal)
          setting.setYtdlpDetails(false)
          setting.setEmbedThumbnail(false)
          setting.setPlaylistStatus(false)
          setting.setSponsorBlock(false)
          setting.setSponsorBlockCategories(setOf("custom-category"))
          setting.setQuickJS(false)
          setting.setFingerprinting(false)


          setting.resetYtdlp()


          assertEquals("", setting.downloadPath.first())
          assertFalse(setting.downloaderBellState.first())
          assertEquals(0, setting.sleepRequest.first())
          assertEquals(Aria2cMode.Disabled, setting.aria2cMode.first())
          assertEquals(ExternalDownloaders.YtdlpNativeDownloader, setting.externalDownloaders.first())
          assertTrue(setting.ytdlpDetails.first())
          assertTrue(setting.embedThumbnail.first())
          assertFalse(setting.playlistStatus.first())
          assertTrue(setting.sponsorBlock.first())
          assertEquals(DEFAULT_SPONSOR_BLOCK_CATEGORIES, setting.sponsorBlockCategories.first())
          assertTrue(setting.quickJS.first())
          assertTrue(setting.fingerprinting.first())
     }

     @Test
     fun `reset snapshot restores settings atomically`() = runTest {
          val setting = newFixture().repositoryOfTheProdCode
          val categories = setOf("sponsor", "music_offtopic")

          setting.setDownloadPath("/downloads")
          setting.setDownloaderDebug(true)
          setting.setSleepRequest(10)
          setting.setAria2cMode(Aria2cMode.EdgeCaseOnly)
          setting.setYtdlpDetails(false)
          setting.setEmbedThumbnail(false)
          setting.setPlaylistStatus(true)
          setting.setSponsorBlock(false)
          setting.setSponsorBlockCategories(categories)
          setting.setQuickJS(false)
          setting.setFingerprinting(false)
          setting.setWifi(Connectivity.Wifi)

          val snapshot = setting.resetYtdlp()
          setting.restoreYtdlp(snapshot)

          assertEquals("/downloads", setting.downloadPath.first())
          assertTrue(setting.downloaderDebug.first())
          assertEquals(10, setting.sleepRequest.first())
          assertEquals(Aria2cMode.EdgeCaseOnly, setting.aria2cMode.first())
          assertEquals(ExternalDownloaders.Disabled, setting.externalDownloaders.first())
          assertFalse(setting.ytdlpDetails.first())
          assertFalse(setting.embedThumbnail.first())
          assertTrue(setting.playlistStatus.first())
          assertFalse(setting.sponsorBlock.first())
          assertEquals(categories, setting.sponsorBlockCategories.first())
          assertFalse(setting.quickJS.first())
          assertFalse(setting.fingerprinting.first())
          assertEquals(
               Connectivity.Wifi,
               setting.wifi.first()
          )
     }

     @Test
     fun `testing the turned off state of eternal downloader`() = runTest {
          val setting = newFixture().repositoryOfTheProdCode

          setting.setAria2cMode(Aria2cMode.EdgeCaseOnly)
          setting.setExternalDownloader(ExternalDownloaders.Disabled)

          assertEquals(Aria2cMode.EdgeCaseOnly, setting.aria2cMode.first())
     }



}
