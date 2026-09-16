package org.foss.fermux.fermuxUIComponents.downloaderComponents

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.ImageButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.MainScreens
import org.foss.fermux.utils.Miscellaneous

private data class AppIcons (
     val image: Int,
     val onClick: () -> Unit
)

@Composable
fun SideBar(
     modifier: Modifier = Modifier,
     navController: NavController
) {
     var isSideBarOpen by remember { mutableStateOf(false) }

     val sideBarEntries = listOf(
          AppIcons(
               image = R.drawable.download,
               onClick = { navController.navigate(MainScreens.Downloader.route) }
            ),
          AppIcons(
               image = R.drawable.library_music_off,
               onClick = { navController.navigate(Miscellaneous.DownloaderMusicList.route) }
          ),
          AppIcons(
               image = R.drawable.video_library_off,
               onClick = { navController.navigate(Miscellaneous.DownloaderVideosList.route) }
          )
     )

     Box(
          modifier = modifier.fillMaxSize().padding(3.dp),
          contentAlignment = Alignment.BottomStart,
     ) {
          AnimatedVisibility(
               visible = isSideBarOpen,
               enter = slideInHorizontally(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
                    initialOffsetX = { fullWidth -> -fullWidth },
               ) + fadeIn(),
               exit = slideOutHorizontally(
                    animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
                    targetOffsetX = { fullWidth -> -fullWidth },
               ) + fadeOut(),
               modifier = Modifier.align(Alignment.BottomStart).padding(bottom = 80.dp),
          ) {
               Column(
                    modifier = Modifier
                         .padding(start = 2.dp)
                         .clip(RoundedCornerShape(8.dp))
                         .border(1.0.dp, FermuxColors.fermuxHelperBorder, RoundedCornerShape(8.dp))
                         .width(70.dp)
                         .background(FermuxColors.fermuxSurface),
               ) {
                    Spacer(modifier = Modifier.height(6.dp))
                    sideBarEntries.forEach { option ->
                         ImageButton(
                              modifier = Modifier
                                   .size(60.dp)
                                   .padding(2.dp)
                                   .align(Alignment.CenterHorizontally),
                              image = option.image,
                              onClick = option.onClick
                         )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
               }
          }
          ImageButton(
               modifier = Modifier.size(70.dp).align(Alignment.BottomStart),
               imageModifier = Modifier.size(32.dp),
               image = if (isSideBarOpen) R.drawable.sidebar_right else R.drawable.sidebar_left,
               onClick = { isSideBarOpen = !isSideBarOpen },
          )
     }
}
