package org.foss.fermux.main

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.ui.ConverterScreen
import org.foss.fermux.ffmpeg.ui.formatSheet.AudioFormatSheet
import org.foss.fermux.ffmpeg.ui.formatSheet.ImageFormatSheet
import org.foss.fermux.ffmpeg.ui.formatSheet.VideoFormatSheet
import org.foss.fermux.settings.ui.SettingsScreen
import org.foss.fermux.terminal.main.ui.FermuxTerminalScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.DownloaderScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.SponsorBlockPage


sealed class Screen (val route: String, val descriptor: String?) {
    object Home            :  Screen("home"      ,"Home")
    object Settings        :  Screen("settings"  ,"Settings")
    object Downloader      :  Screen("downloader", "Downloader")
    object Converter       :  Screen("converter" , "Converter")
    object Terminal        :  Screen("terminal"  , "Terminal")
    object AudioFormatSheet:  Screen("audio"     , "Audio")
    object VideoFormatSheet:  Screen("video"     , "Video")
    object ImageFormatSheet:  Screen("Image"     , "Image")
    object YtdlpDetailsPage:  Screen("Details"   , "Ytdlp details for the settings")
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun FermuxAppMainScreen() {

    val navigationController = rememberNavController()
    val ffmpegViewModel: FFmpegViewModel = viewModel()

    NavHost(
        navController = navigationController,
        startDestination = Screen.Home.route
        )
    {
        composable(Screen.VideoFormatSheet.route) { VideoFormatSheet(navigationController, ffmpegViewModel) }
        composable(Screen.AudioFormatSheet.route) { AudioFormatSheet(navigationController, ffmpegViewModel) }
        composable(Screen.ImageFormatSheet.route) { ImageFormatSheet(navigationController, ffmpegViewModel) }
        composable(Screen.Home.route) { HomeScreen(navigationController) }
        composable(Screen.Terminal.route) { FermuxTerminalScreen() }
        composable(Screen.Settings.route) { SettingsScreen(navController = navigationController) }
        composable(Screen.Downloader.route) { DownloaderScreen(navController = navigationController) }
        composable(Screen.Converter.route) { ConverterScreen(navigationController = navigationController) }
        composable(Screen.YtdlpDetailsPage.route) { SponsorBlockPage() }
    }
}