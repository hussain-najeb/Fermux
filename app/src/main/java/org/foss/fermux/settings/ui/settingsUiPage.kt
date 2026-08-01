package org.foss.fermux.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.FermuxBackButton
import org.foss.fermux.fermuxUIComponents.buttons.FermuxIconButton
import org.foss.fermux.fermuxUIComponents.buttons.FermuxImageButton
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxDivider
import org.foss.fermux.fermuxUIComponents.settingsComponents.FermuxSettingsSwitch
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.settings.logic.getAppVersionName
import org.foss.fermux.ui.theme.FermuxColors

private data class SettingListInfo(
    val settingTitle: String,
    val settingDescription: String,
    val settingIcon: ImageVector? = null,
    val settingImage: Painter? = null,
    val checked: Boolean? = null,
    val onCheckedChange: ((Boolean) -> Unit)? = null,
    val content: @Composable (() -> Unit)? = null
)

@Composable
fun SettingsScreen(
    navController: () -> Unit,
    settingsViewModel: SettingsViewModel = viewModel()
) {

    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true }
    )

    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val versionName = remember { context.getAppVersionName() }

    val notificationState by settingsViewModel.notificationState.collectAsStateWithLifecycle()
    val audioHistory by settingsViewModel.audioHistory.collectAsStateWithLifecycle()
    val videoHistory by settingsViewModel.videoHistory.collectAsStateWithLifecycle()
    val ytdlpDetails by settingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()
    val ytdlpUpdateStatus by settingsViewModel.ytdlpUpdateStatus.collectAsStateWithLifecycle()

    val downloaderSettingLists = listOf(
        SettingListInfo(
            settingTitle = "Update Downloader",
            settingDescription = "Press the update button to update your current version of ytdlp. Current version is ${settingsViewModel.currentVersionName}",
            settingIcon = Icons.Default.Update,
            content = {
                FermuxImageButton(
                    modifier = Modifier.size(60.dp),
                    contentPadding = PaddingValues(9.dp),
                    image = painterResource(R.drawable.update_icon),
                    enabled = ytdlpUpdateStatus != "Checking for update...",
                    onClick = { settingsViewModel.checkYtdlpUpdate() }
                )
            }
        ),
        SettingListInfo(
            settingTitle = "Download Notifications",
            settingDescription = "Notify me when the downloaded files finish downloading",
            settingIcon = Icons.Default.Notifications,
            checked = notificationState,
            onCheckedChange = { settingsViewModel.setNotificationState(it) }
        ),
        SettingListInfo(
            settingTitle = "Audio History",
            settingDescription = "Enable/Disable audio history",
            settingIcon = Icons.Default.AudioFile,
            checked = audioHistory,
            onCheckedChange = { settingsViewModel.setAudioHistory(it) }
        ),
        SettingListInfo(
            settingTitle = "Video History",
            settingDescription = "Enable/Disable video history",
            settingIcon = Icons.Default.VideoFile,
            checked = videoHistory,
            onCheckedChange = { settingsViewModel.setVideoHistory(it) }
        ),
        SettingListInfo(
            settingTitle = "Yt-dlp Details",
            settingDescription = "Enable to get feedback on the download state of the downloaded media",
            settingIcon = Icons.Default.Terminal,
            checked = ytdlpDetails,
            onCheckedChange = { settingsViewModel.setYtdlpDetails(it) }
        ),


    )

    val aboutSettingLists = listOf(
        SettingListInfo(
            settingTitle = "README Page",
            settingDescription = "Check the Github Repository for more information",
            settingIcon = Icons.Default.Description,
            content = {
                FermuxIconButton(
                    modifier = Modifier.size(50.dp),
                    contentPadding = PaddingValues(5.dp),
                    icon = Icons.Default.Link,
                    onClick = {
                        uriHandler.openUri("https://github.com/hussain-najeb/Fermux")
                    }
                )
            }
        ),
        SettingListInfo(
            settingTitle = "App Version",
            settingDescription = "Current app version is $versionName",
            settingIcon = Icons.Default.Android
        )
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = FermuxColors.fermuxBackground,
        topBar = {
            LargeTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = FermuxColors.fermuxBackground,
                    scrolledContainerColor = FermuxColors.fermuxBackground,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                ),
                scrollBehavior = scrollBehavior,
                title = {
                    Text(
                        "Settings",
                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.W500,
                        fontSize = 35.sp,
                        color = Color.White,
                        modifier = Modifier.padding(10.dp)
                    )
                },
                navigationIcon = {
                    FermuxBackButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        modifier = Modifier.padding(10.dp).size(44.dp),
                        contentPadding = PaddingValues(3.dp),
                        onClick = {navController.invoke()}
                    )
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FermuxColors.fermuxBackground)
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
        ) {
            Spacer(Modifier.height(10.dp))
            FermuxDivider()
            Spacer(Modifier.height(10.dp))

            Text(
                "Downloader",
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.W500,
                fontSize = 20.sp,
                color = FermuxColors.fermuxInActiveTextColor,
                modifier = Modifier.padding(6.dp)
            )

            Spacer(Modifier.height(10.dp))

            downloaderSettingLists.forEach { lists ->
                FermuxSettingsSwitch(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    settingTitle = lists.settingTitle,
                    settingDescription = lists.settingDescription,
                    settingIcon = lists.settingIcon,
                    onChecked = lists.checked,
                    onCheckedChange = lists.onCheckedChange,
                    content = lists.content
                )
            }

            Spacer(Modifier.height(10.dp))
            FermuxDivider()
            Spacer(Modifier.height(10.dp))

            Text(
                "About",
                fontFamily = FontFamily.Default,
                fontWeight = FontWeight.W500,
                fontSize = 20.sp,
                color = FermuxColors.fermuxInActiveTextColor,
                modifier = Modifier.padding(6.dp)
            )

            aboutSettingLists.forEach { lists ->
                FermuxSettingsSwitch(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    settingTitle = lists.settingTitle,
                    settingDescription = lists.settingDescription,
                    settingIcon = lists.settingIcon,
                    onChecked = lists.checked,
                    onCheckedChange = lists.onCheckedChange,
                    content = lists.content
                )
            }

            Spacer(Modifier.height(10.dp))
        }
    }
}