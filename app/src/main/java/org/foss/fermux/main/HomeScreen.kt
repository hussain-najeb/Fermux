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
fun HomeScreen(navigationController: NavHostController) { // TODO. Add in animation between each transition so its smooth.


    val scroll = rememberScrollState()


    val screens = listOf(
        ScreenInfo(
            screen = MainScreens.Terminal,
            title = "Terminal",
            description = "A terminal shell with UX, UI, and a lot of convenience taken into account, based on termux",
            image = R.drawable.terminal_main
        ),
        ScreenInfo(
            screen = MainScreens.Downloader,
            title = "Downloader",
            description = "A modern implementation of ytdlp to android with powerful additions.",
            image = R.drawable.download // TODO. next time you see this, the downloader takes time to display any progress when forcing format conversion, so deal with it, and make a third boolean tha goes on and says to the user that this may take time and if it takes too long they can turn it off, also via text on the downlaoder page
            // TODO. Remove the text in the downloader logs, its useless now.
            // TODO. In the ffmpeg settings, make it so that when vide compression is off, it turns off the hard wear accel option, just an `enalbed` and link both in avar with collectAsStateWithLifecycle
             // TODO. add a color for "disabled" buttons, mainly the downloader button for download. add in a dedicate color
        ),
        ScreenInfo(
            screen = MainScreens.Converter,
            title = "Converter",
            description = "A hardware accelerated, powerful conversion tab based on FFmpeg",
            image = R.drawable.ffmpeg
        ),
        ScreenInfo(
            screen = MainScreens.Settings,
            title = "Preferences",
            description = "An extensive Preferences tab for all your options",
            image = R.drawable.prefs
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
                .padding(start = 6.dp, end = 6.dp),
            title = screen.title,
            description = screen.description,
            image = screen.image,
            route = screen.screen,
            navController = navigationController
        )
    }



    }
}