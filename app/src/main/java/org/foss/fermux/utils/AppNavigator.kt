package org.foss.fermux.utils

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import org.foss.fermux.components.downloaderComponents.DownloaderLogs
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.ui.ConverterScreen
import org.foss.fermux.ffmpeg.ui.ffmpegStateCards.FFmpegLogs
import org.foss.fermux.main.HomeScreen
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.ui.SettingsScreen
import org.foss.fermux.settings.ui.aboutPage.AboutPage
import org.foss.fermux.settings.ui.aboutPage.LibraryPage
import org.foss.fermux.settings.ui.converter.SimpleFFmpegSetting
import org.foss.fermux.settings.ui.downloader.SimpleDownloaderPage
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.DownloaderViewModel
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel
import org.foss.fermux.ytdlp.ui.history.History
import org.foss.fermux.ytdlp.ui.mainScreen.DownloadContent
import org.foss.fermux.ytdlp.ui.mainScreen.DownloaderArgs
import org.foss.fermux.ytdlp.ui.sequentialDownloader.SeqLogs
import org.foss.fermux.ytdlp.ui.sequentialDownloader.stateMachine.SequentialContent


sealed class MainScreens(val route: String) {
    object Home : MainScreens("home")
    object Settings : MainScreens("settings")
    object Downloader : MainScreens("downloader")
    object Converter : MainScreens("converter")
}

sealed class SettingsScreens(val route: String) {
    object SimpleDownloader : SettingsScreens(route = "simple downloader")
    object SimpleFFmpeg : SettingsScreens(route = "simple FFmpeg")
    object AboutAppPage : SettingsScreens(route = "about")
    object LibraryPage : SettingsScreens(route = "Library")

}

// Miscellaneous navigation
sealed class Miscellaneous(val route: String) {
    // FFmpeg Screens
    object FFmpegLog : Miscellaneous(route = "FFmpeg Logs")

    // Downloader Screens
    object DownloaderLogs : Miscellaneous(route = "YtdlpLog")
    object DownloaderHistory : Miscellaneous(route = "History")
    object DownloaderArgs : Miscellaneous(route = "Downloader Arguments")
    object SequentialDownloader : Miscellaneous(route = "Sequentially Downloaded")
    object DownloaderSeqLogs : Miscellaneous(route = "Seq Logs")
}

@Composable
fun FermuxAppMainScreen() {

    val navController = rememberNavController()
    val seqView: SequentialViewModel = viewModel()
    val settView: DownloaderSettingsViewModel = viewModel()
    val downView: DownloaderViewModel = viewModel()
    val ffView: FFmpegViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = MainScreens.Home.route,
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300))
        }
    ) {
        // Main Screens
        composable(MainScreens.Home.route) { HomeScreen(navController, downView, settView, ffView ) }
        composable(MainScreens.Settings.route) { SettingsScreen(navController) }
        composable(
            route = MainScreens.Downloader.route,
            deepLinks = listOf(navDeepLink { uriPattern = "fermux://downloader" })
        ) { DownloadContent(navController) }
        composable(route = MainScreens.Converter.route) { ConverterScreen(navController) }

        // Settings Screens
        composable(SettingsScreens.SimpleDownloader.route) { SimpleDownloaderPage(navController) }
        composable(SettingsScreens.SimpleFFmpeg.route) { SimpleFFmpegSetting(navController) }
        composable(SettingsScreens.AboutAppPage.route) { AboutPage(navController) }
        composable(SettingsScreens.LibraryPage.route) { LibraryPage(navController) }

        // FFmpeg
        composable(route = Miscellaneous.FFmpegLog.route) { FFmpegLogs(navController) }

        // Ytdlp
        composable(route = Miscellaneous.DownloaderLogs.route) { DownloaderLogs(navController) }
        composable(route = Miscellaneous.DownloaderArgs.route) { DownloaderArgs(navController) }
        composable(route = Miscellaneous.DownloaderHistory.route) { History(navController) }
        composable(route = Miscellaneous.SequentialDownloader.route) {
            SequentialContent(
                navController,
                sequentialViewModel = seqView
            )
        }
        composable(route = Miscellaneous.DownloaderSeqLogs.route) {
            SeqLogs(
                navController,
                sequentialViewModel = seqView
            )
        }
    }
}