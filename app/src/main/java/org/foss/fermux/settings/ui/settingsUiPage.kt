package org.foss.fermux.settings.ui

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.FermuxIconButton
import org.foss.fermux.fermuxUIComponents.buttons.FermuxImageButton
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxDivider
import org.foss.fermux.fermuxUIComponents.settingsComponents.FermuxSettingsSwitch
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.ui.theme.FermuxColor
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



@SuppressLint("ContextCastToActivity")
@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity,
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    )


    var isPressed by remember { mutableStateOf(false) }


     val notificationState  by settingsViewModel.notificationState. collectAsStateWithLifecycle()
     val languageState      by settingsViewModel.language.          collectAsStateWithLifecycle()
     val downloadPath       by settingsViewModel.downloadPath.      collectAsStateWithLifecycle()
     val audioHistory       by settingsViewModel.audioHistory.      collectAsStateWithLifecycle()
     val videoHistory       by settingsViewModel.videoHistory.      collectAsStateWithLifecycle()
     val ytdlpDetails       by settingsViewModel.ytdlpDetails.      collectAsStateWithLifecycle()
     val sponsorBlock       by settingsViewModel.sponsorBlock.      collectAsStateWithLifecycle()
     val ytdlpUpdateStatus  by settingsViewModel.ytdlpUpdateStatus. collectAsStateWithLifecycle()


     val  downloaderSettingLists = listOf(
         SettingListInfo(
             settingTitle = "Update Downloader",
             settingDescription = "Press the update button to update your current version of ytdlp. Current version is ${settingsViewModel.currentVersionName}",
             settingIcon = Icons.Default.Update,
             content = {
                 FermuxImageButton(
                     modifier = Modifier.size(60.dp),
                     contentPadding = PaddingValues(9.dp),
                     iconRotation = if (isPressed) 180f else 0f,
                     image = painterResource(R.drawable.update_icon),
                     enabled = ytdlpUpdateStatus != "Checking for update...",
                     onClick = { settingsViewModel.checkYtdlpUpdate().also { isPressed = !isPressed } },
                 )
             }
         ),

         SettingListInfo(
             settingTitle = "Download Notifications",
             settingDescription = "Notify me when the downloaded files finish downloading",
             settingIcon = Icons.Default.Notifications,
             checked = notificationState,
             onCheckedChange = {settingsViewModel.setNotificationState(it)}
         ),

         SettingListInfo(
             settingTitle = "Audio History",
             settingDescription = "Enable/Disable audio history",
             settingIcon = Icons.Default.AudioFile,
             checked = audioHistory,
             onCheckedChange = {settingsViewModel.setAudioHistory(it)}
         ),

         SettingListInfo(
             settingTitle = "Video History",
             settingDescription = "Enable/Disable video history",
             settingIcon = Icons.Default.VideoFile,
             checked = videoHistory,
             onCheckedChange = {settingsViewModel.setVideoHistory(it)}
         ),

         SettingListInfo(
             settingTitle = "Yt-dlp Details",
             settingDescription = "Enable to get visual feedback on the download state of the downloaded media",
             settingIcon = Icons.Default.Terminal,
             checked = ytdlpDetails,
             onCheckedChange = {settingsViewModel.setYtdlpDetails(it)}
         ),

//         SettingListInfo(
//             settingTitle = "SponsorBlock API",
//             settingDescription = "Enable to use the SponserBlock API to cut ads off of downloaded media",
//             settingIcon = Icons.Default.MonetizationOn,
//             checked = sponsorBlock,
//             onCheckedChange = {settingsViewModel.setSponsorBlock(it)}
//         ),

     )

    val aboutSettingLists = listOf(
        SettingListInfo(
            settingTitle = "Github Page",
            settingDescription = "Check the Github Repository for more information",
            settingIcon = Icons.Default.FilePresent,
            content = {
                FermuxIconButton(
                    modifier = Modifier.size(50.dp),
                    contentPadding = PaddingValues(5.dp),
                    icon = Icons.Default.Info,
                    onClick  = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            "https://github.com/hussain-najeb/Fermux".toUri()
                        )
                        context.startActivity(intent)
                    }
                )
            }
        )
    )
// ADD app version, github page, an app updater for the app itself.

     Column(modifier = Modifier
         .fillMaxSize()
         .background(FermuxColors.fermuxBackground)
         .verticalScroll(rememberScrollState())

     ) {
         Text(
             "Settings",
             fontFamily = FontFamily.Default,
             fontWeight = FontWeight.W500,
             fontSize = 35.sp,
             color = Color.White,
             modifier = Modifier.padding(10.dp)
         )

         Spacer(Modifier.height(10.dp))

         FermuxDivider()

         Spacer(Modifier.height(10.dp))

         Text(
             "Downloader",
             fontFamily = FontFamily.Default,
             fontWeight = FontWeight.W500,
             fontSize = 20.sp,
             color = FermuxColor().fermuxInActiveTextColor,
             modifier = Modifier
                 .padding(6.dp)
         )

             Spacer(Modifier.height(10.dp))

         downloaderSettingLists.forEach{ lists ->
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

//         Text("Terminal", Modifier
//             .padding(start = 6.dp, top = 6.dp),
//             fontFamily = FontFamily.Default,
//             fontWeight = FontWeight.W500,
//             fontSize = 20.sp,
//             color = Color(0xFF638FFC)
//             )

         Spacer(Modifier.height(10.dp))

         FermuxDivider()

         Spacer(Modifier.height(10.dp))

         Text(
             "About",
             fontFamily = FontFamily.Default,
             fontWeight = FontWeight.W500,
             fontSize = 20.sp,
             color = FermuxColor().fermuxInActiveTextColor,
             modifier = Modifier
                 .padding(6.dp)
         )

         aboutSettingLists.forEach{ lists ->
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
