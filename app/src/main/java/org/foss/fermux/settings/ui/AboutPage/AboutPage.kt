package org.foss.fermux.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingLists
import org.foss.fermux.main.Miscellaneous
import org.foss.fermux.main.SettingsScreens
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.getAppVersionName
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.openUrl

@Composable
fun AboutPage(navController: NavController) {
     val context = LocalContext.current
     val versionName = remember { context.getAppVersionName() }

     val aboutSettingLists = listOf(
          SettingListInfo(
               title = "README Page",
               description = "Check the Github Repository for more information",
               icon = Icons.Default.Description,
               onClick = {
                    context.openUrl("https://github.com/hussain-najeb/Fermux")
               }
          ),
          SettingListInfo(
               // TODO. Make so it checks if there is an update an have it tell the user. Maybe an auto updater for the app.
               title = "App Version",
               description = "The current version of the app is $versionName",
               icon = Icons.Outlined.Info
          ),
          SettingListInfo(
               title = "Dependency And Library Versions",
               description = "Press to see all the versions of dependencies and libraries the app uses",
               image = R.drawable.library,
               onClick = { navController.navigate(SettingsScreens.LibraryPage) }
          )
     )




     LargeTopBarScaffold(
          title = "About Page",
          onBack = { navController.popBackStack() }
     ) { paddingValues ->
          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
          ) {

               aboutSettingLists.forEach { aboutList ->
                    SettingLists(
                         title = aboutList.title,
                         description = aboutList.description,
                         icon = aboutList.icon,
                         image = aboutList.image,
                         onClick = { aboutList.onClick?.invoke() },
                         content = aboutList.content,
                         trailingContent = aboutList.trailingContent
                    )
               }
// TODO. Add ffmpeg, ytdlp, aria2c and QuickJS.js info and version here as well.

          }
     }
}