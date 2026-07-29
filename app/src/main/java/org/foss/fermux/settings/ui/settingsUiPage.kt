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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxDivider
import org.foss.fermux.fermuxUIComponents.settingsComponents.FermuxSettingsSwitch
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors


private data class SettingListInfo(
    val settingTitle: String,
    val settingDescription: String,
    val settingIcon: ImageVector,
    val checked: Boolean,
    val onCheckedChange: (Boolean) -> Unit
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

     val notificationState  by settingsViewModel.notificationState. collectAsState()
     val languageState      by settingsViewModel.language.          collectAsState()
     val downloadPath       by settingsViewModel.downloadPath.      collectAsState()
     val audioHistory       by settingsViewModel.audioHistory.      collectAsState()
     val videoHistory       by settingsViewModel.videoHistory.      collectAsState()
     val ytdlpDetails       by settingsViewModel.ytdlpDetails.      collectAsState()
     val sponsorBlock       by settingsViewModel.sponsorBlock.      collectAsState()


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






// TODO. This is the implementation for the button to update ytdlp.

// private val isUpdatingYtdlp = AtomicBoolean(false)
//
//private val _ytdlpUpdateStatus = MutableStateFlow<String?>(null)
//val ytdlpUpdateStatus: StateFlow<String?> = _ytdlpUpdateStatus
//
//fun checkYtdlpUpdate(context: Context) {
//    if (!isUpdatingYtdlp.compareAndSet(false, true)) return
//    viewModelScope.launch(Dispatchers.IO) {
//        _ytdlpUpdateStatus.value = "Checking for update..."
//        try {
//            YoutubeDL.getInstance().updateYoutubeDL(context, YoutubeDL.UpdateChannel.STABLE)
//            _ytdlpUpdateStatus.value = "yt-dlp is up to date"
//        } catch (e: Exception) {
//            Log.e("fermuxYtdlpUpdater", "yt-dlp update failed", e)
//            _ytdlpUpdateStatus.value = "Update check failed"
//        } finally {
//            isUpdatingYtdlp.set(false)
//        }
//    }
//}

