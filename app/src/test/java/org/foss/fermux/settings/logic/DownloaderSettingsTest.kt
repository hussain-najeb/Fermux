package org.foss.fermux.settings.logic

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.ytdlp.logic.downloader.Aria2cMode
import org.foss.fermux.ytdlp.logic.downloader.ExternalDownloaders
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class DownloaderSettingsTest {
     private lateinit var setting: FakeDownloaderSettings

     @BeforeEach
     fun setup() {
          setting = FakeDownloaderSettings()
     }

     @Test
     fun `testing downloader path`() = runTest {
          setting.setDownloadPath("/downloads")
          assertEquals("/downloads", setting.downloadPath.first())
     }

     @Test
     fun `testing notifications`() = runTest {
          setting.setNotificationState(false)
          assertFalse(setting.notificationState.first())
     }

     @Test
     fun `testing sleepRequest`() = runTest {
          setting.setSleepRequest(9)
          assertEquals(9, setting.sleepRequest.first())
     }

     @Test
     fun `testing embedThumbnail`() = runTest {
          setting.setEmbedThumbnail(false)
          assertFalse(setting.embedThumbnail.first())
     }

     @Test
     fun `testing quickJS`() = runTest {
          setting.setQuickJS(false)
          assertFalse(setting.quickJS.first())

     }

     @Test
     fun `testing fingerprinting`() = runTest {
          setting.setFingerprinting(false)
          assertFalse(setting.fingerprinting.first())
     }

     @Test
     fun `testing aria2cMode`() = runTest {
          setting.setAria2cMode(Aria2cMode.Disabled)
          assertEquals(Aria2cMode.Disabled, setting.aria2cMode.first())
     }

     @Test
     fun `testing externalDownloaders`() = runTest {
          setting.setExternalDownloader(ExternalDownloaders.TurnedOff)
          assertEquals(ExternalDownloaders.TurnedOff, setting.externalDownloaders.first())
     }

     @Test
     fun `testing ytdlpDetails`() = runTest {
          setting.setYtdlpDetails(false)
          assertFalse(setting.ytdlpDetails.first())
     }

     @Test
     fun `testing sponsorBlock`() = runTest {
          setting.setSponsorBlock(false)
          assertFalse(setting.sponsorBlock.first())
     }

     @Test
     fun `testing sponsorBlockCategories`() = runTest {
          val categories = setOf(
               "sponsor",
               "selfpromo",
               "interaction"
          )
          setting.setSponsorBlockCategories(categories)
          assertEquals(categories, setting.sponsorBlockCategories.first())
     }

     @Test
     fun `testing playlistStatus`() = runTest {
          setting.setPlaylistStatus(false)
          assertFalse(setting.playlistStatus.first())
     }

     @Test
     fun `testing audioHistory`() = runTest {
          setting.setAudioHistory(false)
          assertFalse(setting.audioHistory.first())
     }

     @Test
     fun `testing videoHistory`() = runTest {
          setting.setVideoHistory(false)
          assertFalse(setting.videoHistory.first())
     }

     @Test
     fun `testing jsonAudioCard`() = runTest {
          val audioCard = JSONHistoryCards(
               title = "Something Something title",
               thumbnail = "Something Something Thumbnail",
               url = "Something Something URL",
               uploader = "Some Guy",
               downloadTime = 1257,
               videoDuration = 23567
          )
          setting.setJSONAudio(audioCard)
          assertEquals(listOf(audioCard), setting.jsonAudioCard.first())
     }

     @Test
     fun `testing jsonVideoCard`() = runTest {
          val videoCard = JSONHistoryCards(
               title = "Something Something title",
               thumbnail = "Something Something Thumbnail",
               url = "Something Something URL",
               uploader = "Some Guy",
               downloadTime = 1257,
               videoDuration = 23567
          )
          setting.setJSONVideo(videoCard)
          assertEquals(listOf(videoCard), setting.jsonVideoCard.first())
     }
}