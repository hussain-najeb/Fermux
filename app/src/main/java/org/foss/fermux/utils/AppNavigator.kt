package org.foss.fermux.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.foss.fermux.fermuxUIComponents.downloaderComponents.DownloaderLogs
import org.foss.fermux.ffmpeg.ui.ConverterScreen
import org.foss.fermux.ffmpeg.ui.ffmpegStateCards.FFmpegLogs
import org.foss.fermux.main.HomeScreen
import org.foss.fermux.settings.ui.AboutPage
import org.foss.fermux.settings.ui.LibraryPage
import org.foss.fermux.settings.ui.SettingsScreen
import org.foss.fermux.settings.ui.converter.SimpleFFmpegSetting
import org.foss.fermux.settings.ui.downloader.SimpleDownloaderPage
import org.foss.fermux.terminal.main.ui.FermuxTerminalScreen
import org.foss.fermux.ytdlp.ui.historyPage.DownloadVideoList
import org.foss.fermux.ytdlp.ui.historyPage.DownloadedAudioScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.DownloadContent


sealed class MainScreens(val route: String, val descriptor: String?) {
     object Home : MainScreens("home", "Home")
     object Settings : MainScreens("settings", "Settings")
     object Downloader : MainScreens("downloader", "Downloader")
     object Converter : MainScreens("converter", "Converter")
     object Terminal : MainScreens("terminal", "Terminal")
}

sealed class SettingsScreens(val route: String, val descriptor: String?) {
     object SimpleDownloader : SettingsScreens(route = "simple downloader", descriptor = "Main Downloader Page")
     object SimpleFFmpeg : SettingsScreens(route = "simple FFmpeg", descriptor = "Main FFmpeg Page")
     object SimpleTerminal : SettingsScreens(route = "simple terminal", descriptor = "Terminal Main Page")
     object Themes : SettingsScreens(route = "themes", descriptor = "Themes Page")
     object AboutAppPage : SettingsScreens(route = "about", descriptor = "About Page")
     object LibraryPage: SettingsScreens(route = "Library", descriptor = "The main page for dependencies and library")

}

// Miscellaneous navigation
sealed class Miscellaneous(val route: String) {
     object FFmpegLog : Miscellaneous(route = "FFmpegLogs")

     // Downloader Screens
     object DownloaderLogs : Miscellaneous(route = "YtdlpLog")

     object DownloaderVideosList : Miscellaneous(route = "History Video List")
     object DownloaderMusicList : Miscellaneous(route = "History Audio List")
}
@SuppressLint("ViewModelConstructorInComposable")
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
     }
}
