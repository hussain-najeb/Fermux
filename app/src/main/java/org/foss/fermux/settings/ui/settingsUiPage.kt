package org.foss.fermux.settings.ui

import android.annotation.SuppressLint
import android.app.Application
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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxDivider
import org.foss.fermux.fermuxUIComponents.buttons.FermuxMainActionButton
import org.foss.fermux.fermuxUIComponents.settingsComponents.FermuxSettingsSwitch
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors


private data class SettingListInfo(
    val settingTitle: String,
    val settingDescription: String,
    val settingIcon: ImageVector? = null,
    val settingImage: Painter? = null,
    val checked: Boolean? = null,
    val onCheckedChange: ((Boolean) -> Unit)? = null
)



@SuppressLint("ContextCastToActivity")
@Composable
fun SettingsScreen()

    {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity,
        factory = ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    )

     val notificationState  by settingsViewModel.notificationState. collectAsStateWithLifecycle()
     val languageState      by settingsViewModel.language.          collectAsStateWithLifecycle()
     val downloadPath       by settingsViewModel.downloadPath.      collectAsStateWithLifecycle()
     val audioHistory       by settingsViewModel.audioHistory.      collectAsStateWithLifecycle()
     val videoHistory       by settingsViewModel.videoHistory.      collectAsStateWithLifecycle()
     val ytdlpDetails       by settingsViewModel.ytdlpDetails.      collectAsStateWithLifecycle()
     val sponsorBlock       by settingsViewModel.sponsorBlock.      collectAsStateWithLifecycle()
     val ytdlpUpdateStatus  by settingsViewModel.ytdlpUpdateStatus. collectAsStateWithLifecycle()


     val  settingLists = listOf(
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
             settingIcon = Icons.Default.Info,
             checked = ytdlpDetails,
             onCheckedChange = {settingsViewModel.setYtdlpDetails(it)}
         ),

         SettingListInfo(
             settingTitle = "SponsorBlock API",
             settingDescription = "Enable to use the SponserBlock API to cut ads off of downloaded media",
             settingIcon = Icons.Default.MonetizationOn,
             checked = sponsorBlock,
             onCheckedChange = {settingsViewModel.setSponsorBlock(it)}
         )

     )


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
             color = FermuxColors.fermuxInActiveTextColor,
             modifier = Modifier
                 .padding(6.dp)
         )

         FermuxSettingsSwitch(
             modifier = Modifier.align(Alignment.CenterHorizontally),
             settingTitle = "Update Downloader",
             settingDescription = "Press the update button to update your current version of ytdlp. Current version is ${settingsViewModel.currentVersion} ${settingsViewModel.currentVersionName}",
             settingImage = painterResource(id = R.drawable.icon_download_active),
             imageModifier = Modifier.size(35.dp)
         ) {
             Column {
                 FermuxMainActionButton(
                     modifier = Modifier.padding(5.dp),
                     icon = Icons.Default.CloudDownload,
                     onClick = { settingsViewModel.checkYtdlpUpdate() },
                     enabled = ytdlpUpdateStatus != "Checking for update..."
                 )
             }
         }

         Spacer(Modifier.height(10.dp))

         settingLists.forEach{ lists ->
             FermuxSettingsSwitch(
                 modifier = Modifier.align(Alignment.CenterHorizontally),
                 settingTitle = lists.settingTitle,
                 settingDescription = lists.settingDescription,
                 settingIcon = lists.settingIcon,
                 onChecked = lists.checked,
                 onCheckedChange = lists.onCheckedChange
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
             color = FermuxColors.fermuxInActiveTextColor,
             modifier = Modifier
                 .padding(6.dp)
         )

         Spacer(Modifier.height(10.dp))

     }

}
