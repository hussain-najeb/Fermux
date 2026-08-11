package org.foss.fermux.settings.ui

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingLists
import org.foss.fermux.main.SettingsScreens
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.settings.logic.getAppVersionName
import org.foss.fermux.settings.ui.downloader.downlaoderComponents.SponsorBlockOptions
import org.foss.fermux.ui.theme.FermuxColors



enum class UpdateState {
    IDLE, UPDATING, SUCCESS, FAILED
}

/**
 *  TODO. Add an option to reset to default settings
 */

@Composable
fun SettingsScreen(
     navController: NavHostController,
     @SuppressLint("ContextCastToActivity") settingsViewModel: SettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {

    val uriHandler = LocalUriHandler.current
    var showSponsorDialog by remember { mutableStateOf(false) }
    var showAria2cDialog by remember { mutableStateOf(false) }



    // Text(
    //    text = "My section",
    //    modifier = Modifier.padding(
    //        start = 16.dp,
    //        top = 20.dp,
    //        bottom = 8.dp
    //    ),
    //    color = MaterialTheme.colorScheme.primary,
    //    style = MaterialTheme.typography.labelLarge,
    //)

// TODO. Add the cookies option in the Advanced Downloader Page


    val aria2c by settingsViewModel.aria2c.collectAsStateWithLifecycle()
    val aria2cEdgeCase by settingsViewModel.aria2cEdgeCase.collectAsStateWithLifecycle()


    val generalSettings = listOf(
        SettingListInfo(
            title = "Downloader Settings",
            description = "Changing the settings for Ytdlp",
            image = painterResource(id = R.drawable.yt_dlp),
            route = SettingsScreens.SimpleDownloader.route
        ),
        SettingListInfo(
            title = "Converter Settings",
            description = "Changing the settings for FFmpeg",
            image = painterResource(id = R.drawable.ffmpeg),
            route = SettingsScreens.SimpleFFmpeg.route
        ),
        SettingListInfo(
            title = "Terminal Settings",
            description = "Changing the settings for the Terminal",
            image = painterResource(id = R.drawable.terminal_screen),
            route = SettingsScreens.SimpleTerminal.route
        ),
        SettingListInfo(
            title = "Themes",
            description = "Changing the theme of the app",
            icon = Icons.Default.Brush,
            route = SettingsScreens.Themes.route
        ),
        SettingListInfo(
            title = "About",
            description = "About page of the app",
            icon = Icons.Default.Info,
            route = SettingsScreens.AboutAppPage.route
        )
    )



//    val aboutSettingLists = listOf(
//        SettingListInfo(
//            settingTitle = "README Page",
//            settingDescription = "Check the Github Repository for more information",
//            settingIcon = Icons.Default.Description,
//            content = {
//                Box(modifier = Modifier.padding(top = 12.dp)) {
//                    AppIconButton(
//                        modifier = Modifier.size(40.dp),
//                        contentPadding = PaddingValues(5.dp),
//                        icon = Icons.Default.Link,
//                        onClick = {
//                            uriHandler.openUri("https://github.com/hussain-najeb/Fermux")
//                        }
//                    )
//                }
//            }
//        ),


    LargeTopBarScaffold(
        title = "Settings",
        onBack = { navController.popBackStack() }
    ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
            ) {

                generalSettings.forEach {settingsList ->
                    SettingLists(
                        title = settingsList.title,
                        description = settingsList.description,
                        image = settingsList.image,
                        icon = settingsList.icon,
                        onClick = {
                             settingsList.route?.let { navController.navigate(it) }
                        },
                        content = settingsList.content
                    )
                }

//                Text(
//                    "About",
//                    fontFamily = FontFamily.Default,
//                    fontWeight = FontWeight.W500,
//                    fontSize = 20.sp,
//                    color = FermuxColors.fermuxInActiveTextColor,
//                    modifier = Modifier.padding(6.dp)
//                )

                Spacer(Modifier.height(10.dp))
            }


            if (showSponsorDialog) {
                SponsorBlockOptions(
                    onDismissRequest = {
                        showSponsorDialog = false
                    }
                )
            }
            if (showAria2cDialog) {
                Aria2cOptions(
                    onDismissRequest = {
                        showAria2cDialog = false
                    }
                )
            }
        }
    }
