package org.foss.fermux.main

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.ytdlp.ui.historyPage.DownloadVideoList
import org.foss.fermux.ytdlp.ui.historyPage.DownloadedAudioScreen
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.DownloadContent
import kotlin.time.Duration


enum class Page(val image: ImageVector, val descriptor: String) {
    DownloadPage(Icons.Default.Download, "Download Page"),
    AudioListPage(Icons.Filled.LibraryMusic, "Audio Page"),
    VideoListPage(Icons.Filled.VideoLibrary, "Video Page"),
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun SideBar (navigationController: NavHostController) {

    var isSideBarOpen by remember { mutableStateOf(false) }

    var currentPage by remember { mutableStateOf(Page.DownloadPage) }

    val pressed by remember { mutableStateOf(false) }

    val rotate by animateFloatAsState(
        if (isSideBarOpen) 180f else 0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
    )





    Box( contentAlignment = Alignment.CenterStart
        ,modifier = Modifier.fillMaxSize()
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
            .border(4.5.dp, Color(0xFF17191F), (RoundedCornerShape(8.dp)))
            .width(90.dp)
            .background(Color(0xFF1a1d24))
    ) {

        Spacer(modifier = Modifier.height(10.dp))

        Page.entries.forEach { page ->
            val selectedPage = currentPage == page

            val buttonColors by animateColorAsState(
                targetValue = if (selectedPage) Color(0xFFadc6ff) else Color(0xFF303258),
                animationSpec = tween(durationMillis = 300)
            )


            val buttonExpansion by animateDpAsState(
                targetValue = if (selectedPage) 75.dp else 60.dp,
                animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
            )

                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(buttonExpansion)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonColors,
                        contentColor = buttonColors,
                    ),
                    border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),
                    contentPadding = PaddingValues(0.dp),
                    onClick = { currentPage = page },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = page.image,
                        contentDescription = page.descriptor,
                        tint = if (currentPage == page) Color(0xFF102f60) else Color(0xFF126ED7)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                     }
                 }
             }
        }

    val backgroundAnimatedColor by animateColorAsState(
        targetValue = if (isSideBarOpen) Color(0xFFadc6ff) else Color(0xFF303258),
        animationSpec = tween(durationMillis = 300))

    val buttonExpansion by animateDpAsState(
        targetValue = if (isSideBarOpen) 80.dp else 70.dp,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
    )

        Box(
            contentAlignment = Alignment.BottomStart,
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp)
        ) {
            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .width(buttonExpansion)
                    .height(70.dp),
                contentPadding = PaddingValues(0.dp),
                border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),
                colors = ButtonDefaults.buttonColors(backgroundAnimatedColor),

                shape = RoundedCornerShape(16.dp),
                onClick = { isSideBarOpen = !isSideBarOpen }
            ) {
                if (isSideBarOpen) {
                    Icon(
                        painter = painterResource(id = R.drawable.side_bar_icon_pressed),
                        tint = if (isSideBarOpen) Color(0xFF102f60) else Color(0xFF727882),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .rotate(rotate)
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.side_bar_icon),
                        tint = if (isSideBarOpen) Color(0xFF102f60) else Color(0xFF727882),
                        contentDescription = null,
                        modifier = Modifier
                            .size(40.dp)
                            .rotate(rotate)
                    )
                }
            }
        }
    }