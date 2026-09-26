package org.foss.fermux.main


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.generalComponents.MainAppCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.MainScreens
import org.foss.fermux.utils.ScreenInfo

@Composable
fun HomeScreen(navigationController: NavHostController) {


    val scroll = rememberScrollState()


    val screens = listOf(
        ScreenInfo(
            screen = MainScreens.Terminal,
            title = "Terminal",
            description = "A terminal shell with UX, UI, and a lot of convenience taken into account, based on termux",
            image = R.drawable.terminal_blur
        ),
        ScreenInfo(
            screen = MainScreens.Downloader,
            title = "Downloader",
            description = "A modern implementation of ytdlp to android with powerful additions.",
            image = R.drawable.ytdlp_blur
        ),
        ScreenInfo(
            screen = MainScreens.Converter,
            title = "Converter",
            description = "A hardware accelerated, powerful conversion tab based on FFmpeg",
            image = R.drawable.ffmpeg_blur
        ),
        ScreenInfo(
            screen = MainScreens.Settings,
            title = "Preferences",
            description = "An extensive Preferences tab for all your options",
            image = R.drawable.preferences_blur
        ),
    )

    Column( modifier = Modifier
        .fillMaxSize()
        .background(FermuxColors.fermuxBackground)
        .systemBarsPadding()
        .verticalScroll(scroll),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

    screens.forEach { screen ->
        MainAppCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp),
            title = screen.title,
            description = screen.description,
            image = screen.image,
            route = screen.screen,
            navController = navigationController
        )
    }



    }
}