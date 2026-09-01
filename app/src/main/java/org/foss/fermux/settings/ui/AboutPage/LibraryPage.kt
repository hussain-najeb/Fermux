package org.foss.fermux.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingLists
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.openUrl


@Composable
fun LibraryPage(navController: NavController) {

	val context = LocalContext.current 

	val libraryList = listOf(
        SettingListInfo(
        	title = "FFmpeg Version",
        	description = "FFmpeg in this app goes through the converter and the downloader, it's 8.1.2 on both tabs. Press to go to ffmepg page",
        	image = R.drawable.ffmpeg,
        	onClick = { context.openUrl("https://www.ffmpeg.org/about.html") }
        	),


		)

	LargeTopBarScaffold(
          title = "Library list",
          onBack = { navController.popBackStack() }
     ) { paddingValues ->
          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
          ) {

               libraryList.forEach { aboutList ->
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
          }
     }
}