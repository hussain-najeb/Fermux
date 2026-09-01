package org.foss.fermux.settings.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingLists
import org.foss.fermux.utils.SettingsScreens
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun SettingsScreen(
     navController: NavHostController
) {
     val generalSettings = remember {
          listOf(
               SettingListInfo(
                    title = "Downloader Settings",
                    description = "Changing the settings for Ytdlp",
                    image = R.drawable.yt_dlp,
                    route = SettingsScreens.SimpleDownloader.route
               ),
               SettingListInfo(
                    title = "Converter Settings",
                    description = "Changing the settings for FFmpeg",
                    image = R.drawable.ffmpeg,
                    route = SettingsScreens.SimpleFFmpeg.route
               ),
               SettingListInfo(
                    title = "Terminal Settings",
                    description = "Changing the settings for the Terminal",
                    image = R.drawable.terminal_screen,
                    route = SettingsScreens.SimpleTerminal.route
               ),
               SettingListInfo(
                    title = "Themes",
                    description = "Changing the theme of the app",
                    icon = Icons.Default.Brush,
                    route = SettingsScreens.Themes.route
               ),
               SettingListInfo(
                    title = "About",
                    description = "About page of the app",
                    icon = Icons.Default.Info,
                    route = SettingsScreens.AboutAppPage.route
               )
          )
     }

     LargeTopBarScaffold(
          title = "Settings",
          onBack = { navController.popBackStack() }
     ) { paddingValues ->
          LazyColumn(
               modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground),
               contentPadding = paddingValues
          ) {
               items(
                    items = generalSettings,
                    key = { it.title }
               ) { settingsList ->
                    SettingLists(
                         title = settingsList.title,
                         description = settingsList.description,
                         image = settingsList.image,
                         icon = settingsList.icon,
                         onClick = {
                              settingsList.route?.let { navController.navigate(it) }
                         },
                         content = settingsList.content
                    )
               }
          }
     }
}
