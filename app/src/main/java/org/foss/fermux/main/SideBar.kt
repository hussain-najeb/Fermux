package org.foss.fermux.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.fermuxComponents.FermuxImageButton
import org.foss.fermux.fermuxComponents.FermuxTextWithIconButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.ui.historyPage.DownloadVideoList
import org.foss.fermux.ytdlp.ui.historyPage.DownloadedAudioScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.DownloadContent

enum class Page(val image: ImageVector, val descriptor: String) {
    DownloadPage(Icons.Default.DownloadDone, "Download Page"),
    AudioListPage(Icons.Filled.LibraryMusic, "Audio Page"),
    VideoListPage(Icons.Filled.VideoLibrary, "Video Page"),
}
@SuppressLint("SuspiciousIndentation")
@Composable
fun SideBar (navigationController: NavHostController) {

    var isSideBarOpen by remember { mutableStateOf(false) }

    var currentPage by remember { mutableStateOf(Page.DownloadPage) }

    Box( contentAlignment = Alignment.CenterStart
        ,modifier = Modifier.fillMaxSize()
            .background(FermuxColors.fermuxBackground)
    ) {


        Column(  // Content column
            modifier = Modifier
                .fillMaxHeight()
                .background(FermuxColors.fermuxBackground)
        ) {

            when (currentPage) {
                Page.DownloadPage -> DownloadContent()
                Page.AudioListPage -> DownloadedAudioScreen()
                Page.VideoListPage -> DownloadVideoList()
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
            .padding(start = 5.dp)
            .clip(RoundedCornerShape(8.dp))
            .border(1.5.dp, FermuxColors.fermuxPrimaryBorder, (RoundedCornerShape(8.dp)))
            .width(90.dp)
            .background(FermuxColors.fermuxSurface)
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Page.entries.forEach { page ->
            val selectedPage = currentPage == page

            FermuxTextWithIconButton(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally),
                icon = page.image,
                contentDescription  = page.descriptor,
                onClick = { currentPage = page}
            )

                Spacer(modifier = Modifier.height(10.dp))

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

            FermuxImageButton(
                iconRotation = if (isSideBarOpen) 180f else 0f,
                modifier = Modifier.size(70.dp),
                image = if (isSideBarOpen) painterResource(id = R.drawable.sidebar_opened) else painterResource(id = R.drawable.sidebar_closed),
                onClick = { isSideBarOpen = !isSideBarOpen },
            )
        }
    }