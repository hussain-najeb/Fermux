package org.foss.fermux

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.foss.fermux.terminal.terminal.ui.FermuxMainScreen

@Composable
fun FermuxAppMainScreen() {
val navigationController = rememberNavController()

    NavHost(navController = navigationController,
        startDestination = "home") {

        composable("home") { HomeScreen(navigationController) }
        composable("terminal") { FermuxMainScreen() }
        composable("downloader") { DownloaderScreen() }
        composable("converter") { ConverterScreen() }

    }






}