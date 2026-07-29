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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.fermuxUIComponents.FermuxDivider
import org.foss.fermux.fermuxUIComponents.FermuxSettingsSwitch
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors


@Preview
@Composable
fun PreviewSettingsScreen() {

    Row {
        Text(
            "Settings", fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W500,
            fontSize = 40.sp,
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }

}


@SuppressLint("ContextCastToActivity")
@Composable
fun SettingsScreen()

    {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity, factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
    )

     val notificationState  by settingsViewModel.notificationState. collectAsState()
     val languageState      by settingsViewModel.language.          collectAsState()
     val downloadPath       by settingsViewModel.downloadPath.      collectAsState()
     val audioHistory       by settingsViewModel.audioHistory.      collectAsState()
     val videoHistory       by settingsViewModel.videoHistory.      collectAsState()
     val ytdlpDetails       by settingsViewModel.ytdlpDetails.      collectAsState()
     val sponsorBlock       by settingsViewModel.sponsorBlock.      collectAsState()




     Column(modifier = Modifier
         .fillMaxSize()
         .background(Color(0xFF181825))
         .verticalScroll(rememberScrollState())

     ) {
         Row {
             Text(
                 "Settings",
                 fontFamily = FontFamily.Default,
                 fontWeight = FontWeight.W500,
                 fontSize = 40.sp,
                 color = Color.White,
                 modifier = Modifier.padding(16.dp)
             )
         }

         Spacer(Modifier.height(14.dp))

         FermuxDivider()

         Text(
             "General",
             fontFamily = FontFamily.Default,
             fontWeight = FontWeight.W500,
             fontSize = 20.sp,
             color = FermuxColors.fermuxInActiveTextColor,
             modifier = Modifier
                 .padding(6.dp)
         )

         Spacer(Modifier.height(14.dp))

         FermuxDivider()

         FermuxSettingsSwitch(
             modifier = Modifier.align(Alignment.CenterHorizontally),
             settingIcon = (Icons.Default.Notifications),
             settingTitle = "Download Notifications",
             settingDescription = "Notify me when the downloaded files finish downloading",
             onChecked = notificationState,
             onCheckedChange = {settingsViewModel.setNotificationState(it)}
         )  // TODO. Needs to get wired.

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

         Spacer(Modifier.height(10.dp))

         FermuxDivider()

         FermuxSettingsSwitch(
             modifier = Modifier.align(Alignment.CenterHorizontally),
             settingIcon = (Icons.Default.AudioFile),
             settingTitle = "Audio History",
             settingDescription = "Enable/Disable audio history",
             onChecked = audioHistory,
             onCheckedChange = {settingsViewModel.setAudioHistory(it)}
         )

         FermuxSettingsSwitch(
             modifier = Modifier.align(Alignment.CenterHorizontally),
             settingIcon = (Icons.Default.VideoFile),
             settingTitle = "Video History",
             settingDescription = "Enable/Disable video history",
             onChecked = videoHistory,
             onCheckedChange = {settingsViewModel.setVideoHistory(it)}
         )

         FermuxSettingsSwitch(
             modifier = Modifier.align(Alignment.CenterHorizontally),
             settingIcon = (Icons.Default.Details),
             settingTitle = "Yt-dlp Details",
             settingDescription = "More details to watch the download a bit closer and with more info",
             onChecked = ytdlpDetails,
             onCheckedChange = {settingsViewModel.setYtdlpDetails(it)}
         )  // TODO. This doesn't work btw, needs to be linked to the actual ytdlp details in the surface back at the DownloaderStateCard


         FermuxSettingsSwitch(
             modifier = Modifier.align(Alignment.CenterHorizontally),
             settingIcon = Icons.Default.MonetizationOn,
             settingTitle = "SponsorBlock",
             settingDescription = "An implementation of the SponsorBlock API from the browser extension",
            onChecked = sponsorBlock,
             onCheckedChange = {settingsViewModel.setSponsorBlock(it)},
         )
//         SettingsListItemSwitches(
//             "SponsorBlock",
//             "An implementation of the SponsorBlock API from the browser extension",
//             Color(0xFF1f2034),
//             (Icons.Default.MonetizationOn),
//             sponsorBlock
//         ) {settingsViewModel.setSponsorBlock(it == true)}

         Spacer(Modifier.height(10.dp))

//         Text("Terminal", Modifier
//             .padding(start = 6.dp, top = 6.dp),
//             fontFamily = FontFamily.Default,
//             fontWeight = FontWeight.W500,
//             fontSize = 20.sp,
//             color = Color(0xFF638FFC)
//             )

         Spacer(Modifier.height(10.dp))

     }

} // main function

