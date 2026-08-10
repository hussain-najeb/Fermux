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
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
import org.foss.fermux.fermuxUIComponents.buttons.FermuxIconButton
import org.foss.fermux.fermuxUIComponents.buttons.FermuxImageButton
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxDivider
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxLargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.FermuxSettingsSwitch
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.settings.logic.getAppVersionName
import org.foss.fermux.ui.theme.FermuxColors

data class SettingListInfo(
    val settingTitle: String,
    val settingDescription: String,
    val settingIcon: ImageVector? = null,
    val settingImage: Painter? = null,
    val borderBoolean: Boolean = false,
    val border: BorderStroke? = if (borderBoolean) BorderStroke(1.dp, FermuxColors.fermuxSecondaryBorder) else BorderStroke(1.dp, FermuxColors.fermuxGenericBorder),
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
    var showSponsorDialog by remember { mutableStateOf(false) }
    var showAria2cDialog by remember { mutableStateOf(false) }



    val ytdlpDetails by settingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()
    val notificationState by settingsViewModel.notificationState.collectAsStateWithLifecycle()
    val sleepRequest by settingsViewModel.sleepRequest.collectAsStateWithLifecycle()
    val aria2c by settingsViewModel.aria2c.collectAsStateWithLifecycle()
    val aria2cEdgeCase by settingsViewModel.aria2cEdgeCase.collectAsStateWithLifecycle()
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
        rememberInfiniteTransition(label = "update transition")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1800f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "update rotation"
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


    FermuxLargeTopBarScaffold(
        title = "Settings",
        onBack = { navController.popBackStack() }
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
}
@Composable
private fun updateIconPainter(updateState: UpdateState): Painter {
   return when (updateState) {
        UpdateState.IDLE, UpdateState.UPDATING -> painterResource(id = R.drawable.update_icon)
        UpdateState.SUCCESS -> painterResource(id = R.drawable.check)
        UpdateState.FAILED -> rememberVectorPainter(image = Icons.Default.Close)
    }
}
