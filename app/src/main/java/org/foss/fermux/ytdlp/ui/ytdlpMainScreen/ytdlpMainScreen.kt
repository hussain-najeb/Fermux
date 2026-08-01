package org.foss.fermux.ytdlp.ui.ytdlpMainScreen


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController


@Composable
fun DownloaderScreen() {
        Box(Modifier.fillMaxSize()) {
            SideBar()
        }
}
