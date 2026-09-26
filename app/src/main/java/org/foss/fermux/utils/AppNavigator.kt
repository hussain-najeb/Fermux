package org.foss.fermux.utils

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.foss.fermux.fermuxUIComponents.downloaderComponents.DownloaderLogs
import org.foss.fermux.ffmpeg.ui.ConverterScreen
import org.foss.fermux.ffmpeg.ui.ffmpegStateCards.FFmpegLogs
import org.foss.fermux.main.HomeScreen
import org.foss.fermux.settings.ui.AboutPage
import org.foss.fermux.settings.ui.SettingsScreen
import org.foss.fermux.settings.ui.aboutPage.LibraryPage
import org.foss.fermux.settings.ui.converter.SimpleFFmpegSetting
import org.foss.fermux.settings.ui.downloader.SimpleDownloaderPage
import org.foss.fermux.terminal.main.ui.FermuxTerminalScreen
import org.foss.fermux.ytdlp.ui.historyPage.DownloadVideoList
import org.foss.fermux.ytdlp.ui.historyPage.DownloadedAudioScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.DownloadContent
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.DownloaderArgs


sealed class MainScreens(val route: String ) {
     object Home : MainScreens("home")
     object Settings : MainScreens("settings")
     object Downloader : MainScreens("downloader")
     object Converter : MainScreens("converter")
     object Terminal : MainScreens("terminal")
}

data class ScreenInfo(
     val screen: MainScreens,
     val title: String,
     val description: String,
     val image: Int
)

sealed class SettingsScreens(val route: String) {
     object SimpleDownloader : SettingsScreens(route = "simple downloader")
     object SimpleFFmpeg : SettingsScreens(route = "simple FFmpeg")
     object SimpleTerminal : SettingsScreens(route = "simple terminal")
     object Themes : SettingsScreens(route = "themes")
     object AboutAppPage : SettingsScreens(route = "about")
     object LibraryPage: SettingsScreens(route = "Library")

}

// Miscellaneous navigation
sealed class Miscellaneous(val route: String) {
     // FFmpeg Screens
     object FFmpegLog : Miscellaneous(route = "FFmpeg Logs")
     // Downloader Screens
     object DownloaderLogs : Miscellaneous(route = "YtdlpLog")
     object DownloaderVideosList : Miscellaneous(route = "History Video List")
     object DownloaderMusicList : Miscellaneous(route = "History Audio List")

     object DownloaderArgs : Miscellaneous(route = "Downloader Arguments")
}
@Composable
fun FermuxAppMainScreen() {

     val navController = rememberNavController()

     NavHost(
          navController = navController,
          startDestination = MainScreens.Home.route

     ) {
          // Main Screens
          composable(MainScreens.Home.route) { HomeScreen(navController) }
          composable(MainScreens.Terminal.route) { FermuxTerminalScreen(navController) }
          composable(MainScreens.Settings.route) { SettingsScreen(navController) }
          composable(MainScreens.Downloader.route) { DownloadContent(navController = navController) }
          composable(MainScreens.Converter.route) { ConverterScreen(navController = navController) }

          // Settings Screens
          composable(SettingsScreens.SimpleDownloader.route) { SimpleDownloaderPage(navController) }
          composable(SettingsScreens.SimpleFFmpeg.route) { SimpleFFmpegSetting(navController) }
          composable(SettingsScreens.SimpleTerminal.route) { }
          composable(SettingsScreens.Themes.route) { }
          composable(SettingsScreens.AboutAppPage.route) { AboutPage(navController) }
          composable(SettingsScreens.LibraryPage.route) { LibraryPage(navController) }

          // FFmpeg
          composable(route = Miscellaneous.FFmpegLog.route) { FFmpegLogs(navController) }

          // Ytdlp
          composable(route = Miscellaneous.DownloaderLogs.route) { DownloaderLogs(navController) }
          composable(route = Miscellaneous.DownloaderMusicList.route) { DownloadedAudioScreen(navController) }
          composable(route = Miscellaneous.DownloaderVideosList.route) { DownloadVideoList(navController) }
          composable(route = Miscellaneous.DownloaderArgs.route) { DownloaderArgs(navController) }
     }
}
