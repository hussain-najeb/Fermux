package org.foss.fermux.settings.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.SettingsScreens


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
                    route = SettingsScreens.SimpleDownloader.route,
                    position = TilePosition.TOP
               ),
               SettingListInfo(
                    title = "Converter Settings",
                    description = "Changing the settings for FFmpeg",
                    image = R.drawable.ffmpeg,
                    route = SettingsScreens.SimpleFFmpeg.route,
                    position = TilePosition.MIDDLE
               ),
               SettingListInfo(
                    title = "Terminal Settings",
                    description = "Changing the settings for the Terminal",
                    image = R.drawable.terminal,
                    route = SettingsScreens.SimpleTerminal.route,
                    position = TilePosition.MIDDLE
               ),
               SettingListInfo(
                    title = "About",
                    description = "About page of the app",
                    icon = Icons.Default.Info,
                    route = SettingsScreens.AboutAppPage.route,
                    position = TilePosition.BOTTOM
               )
          )
     }

     LargeTopBarScaffold(
          title = "Settings",
          onBack = { navController.popBackStack() },
          modifier = Modifier.padding(bottom = 15.dp)
     ) { paddingValues ->
          LazyColumn(
               modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground),
               contentPadding = paddingValues
          ) {
               item { Spacer(modifier = Modifier.height(10.dp)) }

               items(
                    items = generalSettings,
                    key = { it.title }
               ) { settingsList ->
                    TileOptions(
                         title = settingsList.title,
                         description = settingsList.description,
                         shape = settingsList.position.TileShaper(),
                         image = settingsList.image,
                         icon = settingsList.icon,
                         onClick = {
                              settingsList.onClick?.invoke()
                              settingsList.route?.let { navController.navigate(it) }
                         },
                         content = settingsList.content
                    )
               }
          }
     }
}
