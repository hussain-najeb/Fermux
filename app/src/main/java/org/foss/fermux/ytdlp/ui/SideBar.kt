package org.foss.fermux.ytdlp.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.ViewSidebar
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.foss.fermux.settings.ui.SettingsScreen




enum class Page {

    DownloadPage,
    AudioListPage,
    VideoListPage,

    SettingsPage,

}

@Composable
fun SideBar (navigationController: NavHostController) {

    var isSideBarOpen by remember { mutableStateOf(false) }

    var currentPage by remember { mutableStateOf(Page.DownloadPage) }

    val rotate by animateFloatAsState(
        if (isSideBarOpen) 90f else 0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
    )





    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color(0xFF181825))
    ) {


        Column(  // Content column
            modifier = Modifier
                .fillMaxHeight()
                .background(Color(0xFF181825))
        ) {

            when (currentPage) {
                Page.DownloadPage -> DownloadContent()
                Page.AudioListPage -> DownloadedAudioScreen()
                Page.VideoListPage -> DownloadVideoList()
                Page.SettingsPage -> SettingsScreen()
            }
        }



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
                    .width(70.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1a1d24))
                    .shadow(
                        elevation = 3.dp,


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


    }



        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {


            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .size(74.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(16.dp),
                onClick = { isSideBarOpen = !isSideBarOpen }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ViewSidebar,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .rotate(rotate)
                )
            }
        }
}
