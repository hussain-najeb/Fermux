package org.foss.fermux.ytdlp.ui


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.settings.ui.SettingsScreen


// Add unique Font for the Download screen


enum class Page {

DownloadPage,
    AudioListPage,
    VideoListPage,

    SettingsPage,

}


@Composable
fun DownloaderScreen(navigationController: NavHostController) {


    var currentPage by remember { mutableStateOf(Page.DownloadPage) }

    var isSideBarOpen by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF282c34))
    ) {


        Row(
            modifier = Modifier
                .fillMaxSize()

        )
        {

            AnimatedVisibility(
                visible = isSideBarOpen,
                enter = slideInHorizontally(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
                    initialOffsetX = { fullWidth -> -fullWidth }
                ),
                exit = slideOutHorizontally(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
                    targetOffsetX = { fullWidth -> -fullWidth }
                )
            )
            {


                // Sidebar Column

                Column(


                    modifier = Modifier
                        .padding(top = 280.dp)
                        .height(320.dp)
                        .width(70.dp)
                        .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                        .background(Color(0xFF1a1d24))
                        .shadow(
                            elevation = 3.dp,
                            shape = RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp),
                            ambientColor = Color(0xFF111420),
                            spotColor = Color(0xFF272d45)


                        )


                ) {


                    Spacer(modifier = Modifier.height(10.dp))


                    ToggleButton(
                        modifier = Modifier
                            .offset(x = 6.dp),
                        checked = currentPage == Page.DownloadPage,
                        onCheckedChange = { currentPage = Page.DownloadPage },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Download,
                            contentDescription = "Download",
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    ToggleButton(
                        modifier = Modifier
                            .offset(x = 6.dp),
                        checked = currentPage == Page.VideoListPage,
                        onCheckedChange = { currentPage = Page.VideoListPage },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Movie,
                            contentDescription = "Download List",
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    ToggleButton(
                        modifier = Modifier
                            .offset(x = 6.dp),
                        checked = currentPage == Page.AudioListPage,
                        onCheckedChange = { currentPage = Page.AudioListPage },
                    ) {
                        Icon(
                            imageVector = Icons.Default.LibraryMusic,
                            contentDescription = "Audio List",
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    ToggleButton(
                        modifier = Modifier
                            .offset(x = 6.dp),
                        checked = currentPage == Page.SettingsPage,
                        onCheckedChange = { currentPage = Page.SettingsPage },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))


                    IconButton(
                        modifier = Modifier
                            .offset(x = 6.dp),
                        onClick = { navigationController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Home",
                        )
                    }


                }

            }


// Content Column


            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFF282c34))


            ) {

                when (currentPage) {
                    Page.DownloadPage -> DownloadContent()
                    Page.AudioListPage -> DownloadedAudioScreen()
                    Page.VideoListPage -> DownloadVideoList()
                    Page.SettingsPage -> SettingsScreen()
                }
            }
        }


        Image(
            painter = painterResource( if (isSideBarOpen) {
                R.drawable.icon_sidebar_toggle_active }
                else { R.drawable.icon_sidebar_toggle_default }
            ),
            contentDescription = "Side bar to toggle default",
            modifier = Modifier
                .offset(x = 2.dp, y = 640.dp)
                .padding(top = 12.dp)
                .size(53.dp)
                .clickable { isSideBarOpen = !isSideBarOpen }


        )

    }
}




























