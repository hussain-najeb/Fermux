package org.foss.fermux.main

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.ui.ConverterScreen
import org.foss.fermux.ffmpeg.ui.formatSheet.AudioFormatSheet
import org.foss.fermux.ffmpeg.ui.formatSheet.ImageFormatSheet
import org.foss.fermux.ffmpeg.ui.formatSheet.VideoFormatSheet
import org.foss.fermux.settings.ui.AboutPage
import org.foss.fermux.settings.ui.SettingsScreen
import org.foss.fermux.settings.ui.downloader.SimpleDownloaderPage
import org.foss.fermux.terminal.main.ui.FermuxTerminalScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.DownloaderScreen
import org.foss.fermux.ffmpeg.ui.ffmpegStateCards.FFmpegLogs


sealed class MainScreens (val route: String, val descriptor: String?) {
    object Home: MainScreens("home","Home")
    object Settings: MainScreens("settings"  ,"Settings")
    object Downloader: MainScreens("downloader", "Downloader")
    object Converter: MainScreens("converter" , "Converter")
    object Terminal: MainScreens("terminal"  , "Terminal")
}

sealed class SettingsScreens(val route: String, val descriptor: String?) {
    object SimpleDownloader: SettingsScreens(route = "simple downloader", descriptor = "Main Downloader Page")
    object SimpleFFmpeg: SettingsScreens(route = "simple FFmpeg", descriptor = "Main FFmpeg Page")
    object SimpleTerminal: SettingsScreens(route = "simple terminal", descriptor = "Terminal Main Page")
    object Themes: SettingsScreens(route = "themes", descriptor = "Themes Page")
    object AboutAppPage: SettingsScreens(route = "about", descriptor = "About Page")

}

// Miscellaneous navigation
sealed class Miscellaneous(val route: String) {
object FFmpegLog: Miscellaneous(route = "FFmpegLogs")
}






@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun FermuxAppMainScreen() {

    val navigationController = rememberNavController()
    val ffmpegViewModel: FFmpegViewModel = viewModel()

    NavHost(
        navController = navigationController,
        startDestination = MainScreens.Home.route
        )
    {

        // Main Screens
        composable(MainScreens.Home.route) { HomeScreen(navigationController) }
        composable(MainScreens.Terminal.route) { FermuxTerminalScreen(navigationController) }
        composable(MainScreens.Settings.route) { SettingsScreen(navController = navigationController) }
        composable(MainScreens.Downloader.route) { DownloaderScreen(navController = navigationController) }
        composable(MainScreens.Converter.route) { ConverterScreen(navController = navigationController) }

        // Settings Screens
        composable(SettingsScreens.SimpleDownloader.route) { SimpleDownloaderPage(navController = navigationController) }
        composable(SettingsScreens.SimpleFFmpeg.route) {  }
        composable(SettingsScreens.SimpleTerminal.route) {  }
        composable(SettingsScreens.Themes.route) {  }
        composable(SettingsScreens.AboutAppPage.route) { AboutPage(navController = navigationController) }
        
        // FFmpeg 
        composable(route = Miscellaneous.FFmpegLog.route) { FFmpegLogs(navController = navigationController) }
    }
}
