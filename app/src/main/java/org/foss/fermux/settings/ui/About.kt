package org.foss.fermux.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.outlined.ImageNotSupported
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavController
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingLists
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.getAppVersionName
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun AboutPage(navController: NavController) {
     val context = LocalContext.current
     val versionName = remember { context.getAppVersionName() }


     val uriHandler = LocalUriHandler.current
     fun openUrl(url: String) {
          uriHandler.openUri(url)
     }


     val aboutSettingLists = listOf(
          SettingListInfo(
               title = "README Page",
               description = "Check the Github Repository for more information",
               icon = Icons.Default.Description,
               onClick = {
                         openUrl("https://github.com/hussain-najeb/Fermux")
               }
          ),
          SettingListInfo(
               // TODO. Make so it checks if there is an update an have it tell the user. Maybe an auto updater for the app.
               title = "App Version",
               description = "The current version of the app is $versionName",
               icon = Icons.Outlined.Info
          )
     )




     LargeTopBarScaffold(
          title = "Settings",
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
                         onClick = { aboutList.onClick?.invoke() } ,
                         content = aboutList.content,
                         trailingContent = aboutList.trailingContent
                    )
               }






          }
     }
}