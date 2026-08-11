package org.foss.fermux.ytdlp.ui.ytdlpMainScreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.ImageButton
import org.foss.fermux.fermuxUIComponents.buttons.TextWithIconButton
import org.foss.fermux.ui.theme.FermuxColors

/**
 * The floating, collapsible navigation rail: a toggle button pinned to the
 * bottom-start corner, and (when open) a column of page icons above it.
 *
 * This composable owns ONLY the rail UI — it does not size or own the
 * screen, and does not know what "page content" is. The caller overlays it
 * on top of their own content within whatever bounds are already sized.
 */
@Composable
fun SideBar(
     currentPage: Page,
     onPageSelected: (Page) -> Unit,
     modifier: Modifier = Modifier,
) {
     var isSideBarOpen by remember { mutableStateOf(false) }

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
                         .padding(start = 5.dp)
                         .clip(RoundedCornerShape(8.dp))
                         .border(1.0.dp, FermuxColors.fermuxSecondaryBorder, RoundedCornerShape(8.dp))
                         .width(70.dp)
                         .background(FermuxColors.fermuxSurface),
               ) {
                    Spacer(modifier = Modifier.height(10.dp))

                    Page.entries.forEach { page ->
                         TextWithIconButton(
                              modifier = Modifier
                                   .size(60.dp)
                                   .padding(3.dp)
                                   .align(Alignment.CenterHorizontally),
                              icon = page.image,
                              contentDescription = page.descriptor,
                              onClick = { onPageSelected(page) },
                         )
                         Spacer(modifier = Modifier.height(10.dp))
                    }
               }
          }

          ImageButton(
               modifier = Modifier.size(70.dp).align(Alignment.BottomStart),
               imageModifier = Modifier.size(32.dp),
               imageRotation = if (isSideBarOpen) 180f else 0f,
               image = if (isSideBarOpen) painterResource(id = R.drawable.sidebar_hide) else painterResource(id = R.drawable.sidebar_show),
               onClick = { isSideBarOpen = !isSideBarOpen },
          )
     }
}
