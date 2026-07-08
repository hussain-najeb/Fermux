package org.foss.fermux.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.foss.fermux.ffmpeg.ui.ConverterScreen
import org.foss.fermux.ffmpeg.ui.FormatSheet
import org.foss.fermux.settings.ui.SettingsScreen
import org.foss.fermux.terminal.main.ui.FermuxTerminalScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.DownloaderScreen


sealed class Screen (val route: String) {
    object Home       :  Screen("home")
    object Settings   :  Screen("settings")
    object Downloader :  Screen("downloader")
    object Converter  :  Screen("converter")
    object Terminal   :  Screen("terminal")
    object FormatSheet:  Screen("sheet")
}

@Composable
fun FermuxAppMainScreen() {
    val navigationController = rememberNavController()

    NavHost(
        navController = navigationController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.FormatSheet.route) { FormatSheet(navigationController) }
        composable(Screen.Home.route) { HomeScreen(navigationController) }
        composable(Screen.Terminal.route) { FermuxTerminalScreen() }
        composable(Screen.Settings.route) { SettingsScreen() }
        composable(Screen.Downloader.route) { DownloaderScreen(navigationController) }
        composable(Screen.Converter.route) { ConverterScreen() }
    }
}