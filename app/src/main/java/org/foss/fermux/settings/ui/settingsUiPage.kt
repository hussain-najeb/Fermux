package org.foss.fermux.settings.ui

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.FermuxBackButton
import org.foss.fermux.fermuxUIComponents.buttons.FermuxIconButton
import org.foss.fermux.fermuxUIComponents.buttons.FermuxImageButton
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxDivider
import org.foss.fermux.fermuxUIComponents.settingsComponents.FermuxSettingsSwitch
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.settings.logic.getAppVersionName
import org.foss.fermux.ui.theme.FermuxColors

data class SettingListInfo(
    val settingTitle: String,
    val settingDescription: String,
    val settingIcon: ImageVector? = null,
    val settingImage: Painter? = null,
    val border: BorderStroke? = BorderStroke(1.dp, FermuxColors.fermuxGenericBorder),
    val checked: Boolean? = null,
    val onCheckedChange: ((Boolean) -> Unit)? = null,
    val content: @Composable (() -> Unit)? = null
)

enum class UpdateState {
    IDLE, UPDATING, SUCCESS, FAILED
}

@Composable
fun SettingsScreen(
     navController: NavHostController,
     @SuppressLint("ContextCastToActivity") settingsViewModel: SettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    val versionName = remember { context.getAppVersionName() }

    val ytdlpDetails by settingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()
    val notificationState by settingsViewModel.notificationState.collectAsStateWithLifecycle()
    val audioHistory by settingsViewModel.audioHistory.collectAsStateWithLifecycle()
    val videoHistory by settingsViewModel.videoHistory.collectAsStateWithLifecycle()
    val isCheckingForUpdate by settingsViewModel.isCheckingForUpdate.collectAsStateWithLifecycle()
    val updateChecker by settingsViewModel.upToDate.collectAsStateWithLifecycle()
    val updateState = when {
        isCheckingForUpdate -> UpdateState.UPDATING
        updateChecker == true -> UpdateState.SUCCESS
        updateChecker == false -> UpdateState.FAILED
        else -> UpdateState.IDLE
    }

    val infiniteTransition =
        rememberInfiniteTransition(label = "update_transition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1800f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "update_rotation"
    )
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState(),
        canScroll = { true }
    )

    var showSponsorDialog by remember { mutableStateOf(false) }

    val downloaderSettingLists = listOf(
        SettingListInfo(
            settingTitle = "Update Downloader",
            settingDescription = "Press the update button to update your current version of ytdlp. Current version is ${settingsViewModel.currentVersionName}",
            settingIcon = Icons.Default.Update,
            content = {

                val updatingIcon = updateIconPainter(updateState)

                FermuxImageButton(
                    modifier = Modifier.size(50.dp),
                    imageRotation = if (updateState == UpdateState.UPDATING) rotation else 0f,
                    contentPadding = PaddingValues(9.dp),
                    image = updatingIcon,
                    enabled = updateState != UpdateState.UPDATING,
                    onClick = { settingsViewModel.checkYtdlpUpdate() }
                )
            }
        ),
        SettingListInfo(
            settingTitle = "Download Notifications",
            settingDescription = "Notify me when the downloaded files finish downloading",
            settingImage = if (notificationState) painterResource(R.drawable.bell_on) else painterResource(R.drawable.bell_off),
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
            settingImage = if (ytdlpDetails) painterResource(R.drawable.logs_icon) else painterResource(R.drawable.logs_off),
            checked = ytdlpDetails,
            onCheckedChange = { settingsViewModel.setYtdlpDetails(it) }
        ),
        SettingListInfo(
            settingTitle = "SponsorBlock API",
            settingDescription = "Use the SponsorBlock API to cut Sponsor segments in videos (*Note: Works best with YouTube)",
            settingImage = painterResource(R.drawable.sponsorblock),
            border = BorderStroke(width = 1.dp, color = FermuxColors.fermuxSecondaryBorder),
            content = {
                Box(modifier = Modifier.padding(top = 10.dp)) {
                    FermuxIconButton(
                        modifier = Modifier.size(40.dp),
                        border = BorderStroke(width = 1.dp, color = FermuxColors.fermuxTertiaryBorder),
                        contentPadding = PaddingValues(9.dp),
                        icon = Icons.Default.Settings,
                        onClick = { showSponsorDialog = true }
                    )
                }
            }
        ),

    )

    val aboutSettingLists = listOf(
        SettingListInfo(
            settingTitle = "README Page",
            settingDescription = "Check the Github Repository for more information",
            settingIcon = Icons.Default.Description,
            content = {
                Box(modifier = Modifier.padding(top = 12.dp)) {
                    FermuxIconButton(
                        modifier = Modifier.size(40.dp),
                        contentPadding = PaddingValues(5.dp),
                        icon = Icons.Default.Link,
                        onClick = {
                            uriHandler.openUri("https://github.com/hussain-najeb/Fermux")
                        }
                    )
                }
            }
        ),
        SettingListInfo(
            settingTitle = "App Version",
            settingDescription = "Current app version is $versionName",
            settingIcon = Icons.Default.Android,
            border = BorderStroke(width = 1.dp, color = FermuxColors.fermuxTertiaryBorder)
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
                        onClick = { navController.popBackStack() }
                    )
                }
            )
        }
    ) { paddingValues ->

        Box(modifier = Modifier.fillMaxSize()) {

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
                        imageModifier = Modifier.size(20.dp),
                        settingTitle = lists.settingTitle,
                        settingDescription = lists.settingDescription,
                        settingIcon = lists.settingIcon,
                        settingImage = lists.settingImage,
                        border = lists.border,
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
                        border = lists.border,
                        onChecked = lists.checked,
                        onCheckedChange = lists.onCheckedChange,
                        content = lists.content
                    )
                }

                Spacer(Modifier.height(10.dp))
            }
            if (showSponsorDialog) {
                SponsorBlockOptions(
                    onDismissRequest = {
                        showSponsorDialog = false //TODO. this conflicts with the scaffold
                    }
                )
            }
        }
    }
}

@Composable
private fun updateIconPainter(updateState: UpdateState): Painter {
   return when (updateState) {
        UpdateState.IDLE, UpdateState.UPDATING -> painterResource(id = R.drawable.update_icon)
        UpdateState.SUCCESS -> painterResource(id = R.drawable.check)
        UpdateState.FAILED -> rememberVectorPainter(image = Icons.Default.Close)
    }
}
