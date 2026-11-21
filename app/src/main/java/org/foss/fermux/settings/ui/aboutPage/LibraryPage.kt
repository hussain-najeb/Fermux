package org.foss.fermux.settings.ui.aboutPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import org.foss.fermux.R
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.InfoListClass
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.openUrl


@Composable
fun LibraryPage(navController: NavController) {
    val context = LocalContext.current

    val libraryList = listOf(
        InfoListClass(
            title = "FFmpeg Version",
            description = "FFmpeg in this app goes through the converter and the downloader, it's 9.0.2 on both tabs. Press to view to the ffmpeg page",
            image = R.drawable.ffmpeg,
            onClick = { context.openUrl("https://www.ffmpeg.org/about.html") },
            position = TilePosition.TOP
        ),
        InfoListClass(
            title = "QuickJS-NG Version",
            description = "QuickJS-NG in this app goes through the PO tokens in the downloader and solves js challenges for YouTube, at least for now. This version is NG, it's on version QuickJS-ng 0.18.0. Press to view the github page",
            image = R.drawable.flash_on,
            onClick = { context.openUrl("https://github.com/quickjs-ng/quickjs") },
            position = TilePosition.MIDDLE
        ),
        InfoListClass(
            title = "Aria2-Next Version",
            description = "Aria2-Next in this app goes through the downloader to use on large downloads to speed things up, it's on version 3.0.10 ",
            image = R.drawable.layers,
            onClick = { context.openUrl("https://github.com/AnInsomniacy/aria2-next") },
            position = TilePosition.MIDDLE
        ),
        InfoListClass(
            title = "YoutubeDL-Android Version",
            description = "This app uses the YoutubeDL-Android-Next library to make the downloader work, it's version 0.1.6. Tap to view the github page",
            image = R.drawable.yt_dlp,
            onClick = { context.openUrl("https://github.com/hussain-najeb/youtubedl-android-next") },
            position = TilePosition.MIDDLE
        ),
        InfoListClass(
            title = "Curl impersonation",
            description = "This app bundles curl_impersonate and curl as core features, which is needed since a lot of the web has been using it, both sit on version 2.1.1 for cffi and 0.17.0b2 for curl_impersonate. Check the github page for curl_impersonate",
            icon = Icons.Filled.Fingerprint,
            onClick = { context.openUrl("https://github.com/lexiforest/curl-impersonate") },
            position = TilePosition.BOTTOM
        )
    )

    LargeTopBarScaffold(
        title = "Library list",
        onBack = navController::popBackStack
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FermuxColors.background)
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            libraryList.forEach { aboutList ->
                TileOptions(
                    title = aboutList.title,
                    description = aboutList.description,
                    shape = aboutList.position.TileShaper(),
                    icon = aboutList.icon,
                    image = aboutList.image,
                    onClick = {
                        aboutList.onClick?.invoke()
                        aboutList.route?.let { navController.navigate(it) }
                    },
                    content = aboutList.content,
                    trailingContent = aboutList.trailingContent
                )
            }
        }
    }
}