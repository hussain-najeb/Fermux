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
import org.foss.fermux.settings.ui.SettingsScreen
import org.foss.fermux.settings.ui.downloader.SimpleDownloaderPage
import org.foss.fermux.terminal.main.ui.FermuxTerminalScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.DownloaderScreen


sealed class MainScreens (val route: String, val descriptor: String?) {
    object Home: MainScreens("home","Home")
    object Settings: MainScreens("settings"  ,"Settings")
    object Downloader: MainScreens("downloader", "Downloader")
    object Converter: MainScreens("converter" , "Converter")
    object Terminal: MainScreens("terminal"  , "Terminal")

    // TODO. Add these in there own FFmpeg sealed class navigator
    object AudioFormatSheet: MainScreens("audio", "Audio")
    object VideoFormatSheet: MainScreens("video", "Video")
    object ImageFormatSheet: MainScreens("Image", "Image")
}

sealed class SettingsScreens(val route: String, val descriptor: String?) {
    object SimpleDownloader: SettingsScreens(route = "simple downloader", descriptor = "Main Downloader Page")
    object AdvancedDownloader: SettingsScreens(route = "advanced Downloader", descriptor = "Advanced Downloader Settings")
    object SimpleFFmpeg: SettingsScreens(route = "simple FFmpeg", descriptor = "Main FFmpeg Page")
    object AdvancedFFmpeg: SettingsScreens(route = "advanced FFmpeg", descriptor = "Advanced FFmpeg Settings")
    object SimpleTerminal: SettingsScreens(route = "simple terminal", descriptor = "Terminal Main Page")
    object AdvancedTerminal: SettingsScreens(route = "advanced terminal", descriptor = "Advanced Terminal Page")
    object Themes: SettingsScreens(route = "themes", descriptor = "Themes Page")
    object AboutAppPage: SettingsScreens(route = "about", descriptor = "About Page")

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
        composable(MainScreens.Converter.route) { ConverterScreen(navigationController = navigationController) }

        // Settings Screens
        composable(SettingsScreens.SimpleDownloader.route) { SimpleDownloaderPage(navController = navigationController) }
        composable(SettingsScreens.AdvancedDownloader.route) {  }
        composable(SettingsScreens.SimpleFFmpeg.route) {  }
        composable(SettingsScreens.AdvancedFFmpeg.route) {  }
        composable(SettingsScreens.SimpleTerminal.route) {  }
        composable(SettingsScreens.AdvancedTerminal.route) {  }
        composable(SettingsScreens.Themes.route) {  }
        composable(SettingsScreens.AboutAppPage.route) {  }

        // FFmpeg
        composable(MainScreens.VideoFormatSheet.route) { VideoFormatSheet(navHostController = navigationController, ffmpegViewModel) }
        composable(MainScreens.AudioFormatSheet.route) { AudioFormatSheet(navHostController = navigationController, ffmpegViewModel) }
        composable(MainScreens.ImageFormatSheet.route) { ImageFormatSheet(navHostController = navigationController, ffmpegViewModel) }
    }
}
