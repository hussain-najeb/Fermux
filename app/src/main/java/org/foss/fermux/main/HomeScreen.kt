package org.foss.fermux.main


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.MainAppCard
import org.foss.fermux.ffmpeg.logic.FFmpegStatus
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.MainScreens
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.DownloadStatus
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.DownloaderViewModel
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.ScreenInfo
import org.foss.fermux.ytdlp.ui.quickDownloads.QuickDownloadsStateMachine

@Composable
fun HomeScreen(
    navController: NavHostController,
    downloaderViewModel: DownloaderViewModel,
    downloaderSettingsViewModel: DownloaderSettingsViewModel,
    ffmpegViewModel: FFmpegViewModel
) {

    val context = LocalContext.current
    val clipboard = LocalClipboard.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val scroll = rememberScrollState()

    val enabled = downloaderViewModel.state is DownloadStatus.Idle || downloaderViewModel.state is DownloadStatus.Error
    val quickDownloads by downloaderSettingsViewModel.quickDownloads.collectAsStateWithLifecycle()
    val speed = (downloaderViewModel.state as? DownloadStatus.Downloading)?.downloadProgress ?: 0f
    val iconRotate by animateFloatAsState(
        targetValue = speed,
        animationSpec = tween(),
        label = "Fermux Icon Rotation"
    )

    val screens = listOf(
        ScreenInfo(
            screen = MainScreens.Downloader,
            title = "Downloader",
            description = "A modern implementation of ytdlp to android with powerful additions.",
            image = R.drawable.download,
            buttonIcon = if (quickDownloads) R.drawable.speed else null,
            iconModifier = Modifier.rotate(iconRotate),
            onClick = {
                if (quickDownloads) {
                    scope.launch {
                        clipboard.getClipEntry()?.clipData?.getItemAt(0)?.text?.toString()
                            .let { text ->
                                if (text != null) {
                                    downloaderViewModel.downloadUrl = text
                                    downloaderViewModel.quickDownloads()
                                    downloaderViewModel.startingDownload(context)
                                }
                                if (text.isNullOrBlank() || text.isEmpty()) {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "Empty clipboard",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }
                    }
                }
            },
            trailingContent = {
                QuickDownloadsStateMachine(
                    downloaderViewModel.state,
                    downloaderViewModel,
                    navController = navController
                )
            },
            enabled = enabled,
            backColor = FermuxColors.fermuxErrorCardColor
        ),
        ScreenInfo(
            screen = MainScreens.Converter,
            title = "Converter",
            description = "A hardware accelerated, powerful conversion tab based on FFmpeg",
            image = R.drawable.ffmpeg,
            onClick = null,
            enabled = true,
            backColor = FermuxColors.ffCoreGreen
        ),
        ScreenInfo(
            screen = MainScreens.Settings,
            title = "Preferences",
            description = "An extensive Preferences tab for all your options",
            image = R.drawable.prefs,
            onClick = null,
            enabled = true,
            backColor = FermuxColors.deepBlue
        ),
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FermuxColors.background)
            .padding(top = 60.dp)
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home",
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 22.sp,
            color = Color.White,
            modifier = Modifier.padding(30.dp)
        )

        screens.forEach { screen ->
            MainAppCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 5.dp, end = 5.dp, bottom = 8.dp, top = 3.dp),
                iconModifier = screen.iconModifier,
                title = screen.title,
                description = screen.description,
                backColor = screen.backColor,
                image = screen.image,
                buttonImage = screen.buttonIcon,
                buttonOnClick = screen.onClick,
                route = screen.screen,
                enabled = screen.enabled,
                trailingContent = screen.trailingContent,
                navController = navController
            )
        }
    }

    Box(contentAlignment = Alignment.BottomStart) {
        val ffState = ffmpegViewModel.state is FFmpegStatus.Converting
        val ytState = downloaderViewModel.state is DownloadStatus.Downloading
        HomeCanv(ffColor = ffState, ytColor = ytState)
    } // looks good enough

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AppSnackBar(snackbarHostState)
    }

}