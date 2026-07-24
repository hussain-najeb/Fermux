package org.foss.fermux.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yausername.youtubedl_android.YoutubeDL
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.ui.ConverterScreen
import org.foss.fermux.ffmpeg.ui.formatSheet.AudioFormatSheet
import org.foss.fermux.ffmpeg.ui.formatSheet.ImageFormatSheet
import org.foss.fermux.ffmpeg.ui.formatSheet.VideoFormatSheet
import org.foss.fermux.settings.ui.SettingsScreen
import org.foss.fermux.storage.SettingsViewModel
import org.foss.fermux.terminal.main.ui.FermuxTerminalScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.DownloaderScreen


sealed class Screen (val route: String, val descriptor: String?) {
    object Home            :  Screen("home"      ,"Home")
    object Settings        :  Screen("settings"  ,"Settings")
    object Downloader      :  Screen("downloader", "Downloader")
    object Converter       :  Screen("converter" , "Converter")
    object Terminal        :  Screen("terminal"  , "Terminal")
    object AudioFormatSheet:  Screen("audio"     , "Audio")
    object VideoFormatSheet:  Screen("video"     , "Video")
    object ImageFormatSheet:  Screen("Image"     , "Image")
}

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun FermuxAppMainScreen() {
    val context = LocalContext.current

    val settingsViewModel: SettingsViewModel = viewModel (
        viewModelStoreOwner = context as ComponentActivity,
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.application)
    )

    val ytdlpUpdater by settingsViewModel.ytdlpUpdater.collectAsState()

    LaunchedEffect(Unit) {
        if (ytdlpUpdater) {
            try {
                YoutubeDL.getInstance().updateYoutubeDL(context as MainActivity, YoutubeDL.UpdateChannel.STABLE)
                Log.d("fermux", "yt-dlp updated successfully")
            } catch (e: Exception) {
                Log.e("fermux", "yt-dlp update failed", e)
            }
        }
    }















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
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.Downloader.route) { DownloaderScreen(navigationController) }
        composable(Screen.Converter.route) { ConverterScreen(navigationController = navigationController) }
    }
}