package org.foss.fermux

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.foss.fermux.ffmpeg.ui.ConverterScreen
import org.foss.fermux.homeScreen.HomeScreen
import org.foss.fermux.settings.ui.SettingsScreen
import org.foss.fermux.terminal.terminal.ui.FermuxMainScreen
import org.foss.fermux.ytdlp.ui.DownloaderScreen

@Composable
fun FermuxAppMainScreen() {
val navigationController = rememberNavController()

    NavHost(navController = navigationController,
        startDestination = "home") {

        composable("home") { HomeScreen(navigationController) }
        composable("terminal") { FermuxMainScreen() }
        composable("downloader") { DownloaderScreen(navigationController) }
        composable("converter") { ConverterScreen() }
        composable("settings") { SettingsScreen() }

    }

}