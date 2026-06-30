package org.foss.fermux.settings.ui

import android.annotation.SuppressLint
import android.app.Application
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material.icons.filled.FileOpen
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.StoreMallDirectory
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.settings.logic.SettingsViewModel


@SuppressLint("ContextCastToActivity")
@Composable
fun SettingsScreen()

 {
    val context = LocalContext.current
    val settingsViewModel: SettingsViewModel = viewModel(
        viewModelStoreOwner = LocalContext.current as ComponentActivity, factory =
            ViewModelProvider.AndroidViewModelFactory.getInstance(context.applicationContext as Application)
)

     val listItemModifier = Modifier
         .padding(5.dp)
         .clip(RoundedCornerShape(8.dp))
         .border(1.5.dp, Color(0xFF17DB6F), RoundedCornerShape(8.dp))
         .border(1.5.dp, Color(0xFF20B161), RoundedCornerShape(8.dp))
         .border(0.8.dp, Color(0xFF20bf6b), RoundedCornerShape(8.dp))



     val notificationState  by settingsViewModel.notificationState. collectAsState()
     val languageState      by settingsViewModel.language.          collectAsState()
     val downloadPath       by settingsViewModel.downloadPath.      collectAsState()
     val audioHistory       by settingsViewModel.audioHistory.      collectAsState()
     val videoHistory       by settingsViewModel.videoHistory.      collectAsState()
     val ytdlpDetails       by settingsViewModel.ytdlpDetails.      collectAsState()
     val sponserBlock       by settingsViewModel.sponserBlock.      collectAsState()
     val ytdlpUpdater       by settingsViewModel.ytdlpUpdater.      collectAsState()




     Column(modifier = Modifier
         .fillMaxSize()
         .background(Color(0xFF181825))
         .verticalScroll(rememberScrollState())

     ) {
         Row {
             Text(
                 "Settings", fontFamily = FontFamily.Default,
                 fontSize = 40.sp,
                 color = Color.White,
                 modifier = Modifier.padding(16.dp)
             )
         }
         Spacer(Modifier.height(16.dp))

         Text(
             "General",
             fontFamily = FontFamily.Default,
             fontSize = 20.sp,
             color = Color(0xFF638FFC),
             modifier = Modifier
                 .padding(start = 6.dp, top = 6.dp)
         )

         Spacer(Modifier.height(16.dp))

         SettingsListItemSwitches(
             "Download Notifications",
             "Notify me when the downloaded files finish downloading",
             Color(0xFF1f2034),
             (Icons.Default.Notifications),
             true
         ) {settingsViewModel.setNotificationState(it)}

         ListItem(
             modifier = listItemModifier,
             headlineContent = { Text("Display Language", fontFamily = FontFamily.Default, fontSize = 17.sp) },
             colors = ListItemDefaults.colors(Color(0xFF1f2034)),
             supportingContent = {
                 Text(
                     "",
                     fontFamily = FontFamily.Default
                 )
             }, // TODO. in seal, the chosen language is showing in the supporting content, do it.
             leadingContent = { Icon(Icons.Default.Language, contentDescription = null) },
             trailingContent = {

             }
         )

         Spacer(Modifier.height(10.dp))

         Text(
             "Downloader",
             fontFamily = FontFamily.Default,
             fontSize = 20.sp,
             color = Color(0xFF638FFC),
             modifier = Modifier
                 .padding(start = 6.dp, top = 6.dp)
         )

         Spacer(Modifier.height(10.dp))

         SettingsListItemSwitches(
             "Auto Update Yt-dlp",
             "Auto update the ty-dlp API for better compatibility",
             Color(0xFF1f2034),
             (Icons.Default.Downloading),
             ytdlpUpdater
         ) { settingsViewModel.setYtdlpUpdater(it) }

         ListItem(
             modifier = listItemModifier,
             headlineContent = { Text("Set download ", fontFamily = FontFamily.Default, fontSize = 17.sp) },
             supportingContent = {Text("Changing the download directory")},
             colors = ListItemDefaults.colors(Color(0xFF1f2034)),
             leadingContent = { Icon(Icons.Default.Folder, contentDescription = null) },
             trailingContent = {

             }
         )

         SettingsListItemSwitches(
             "Audio History",
             "Enable/Disable audio history",
             Color(0xFF1f2034),
             (Icons.Default.AudioFile),
             audioHistory
         ) {settingsViewModel.setAudioHistory(it)}

         SettingsListItemSwitches(
             "Video History",
             "Enable/Disable video history",
             Color(0xFF1f2034),
             (Icons.Default.VideoFile),
             videoHistory
         ) {settingsViewModel.setVideoHistory(it)}

         SettingsListItemSwitches(
             "Yt-dlp Details",
             "More details to watch the download a bit closer and with more info",
             Color(0xFF1f2034),
             (Icons.Default.Details),
             ytdlpDetails
         ) {settingsViewModel.setYtdlpDetails(it)}

         SettingsListItemSwitches(
             "SponserBlock",
             "An implementation of the SponserBlock API from the browser extension",
             Color(0xFF1f2034),
             (Icons.Default.MonetizationOn),
             sponserBlock
         ) {settingsViewModel.setSponsorBlock(it)}




     }
} // main fun

//TODO. change the color and look of the switch later.


@Composable
fun SettingsListItemSwitches (
    title: String,
    subtitle: String,
    color: Color = Color(0xFF1f2034),
    image: ImageVector,
    onCheck: Boolean,
    onChange: (Boolean) -> Unit)
{
    val listItemModifier = Modifier
        .padding(5.dp)
        .clip(RoundedCornerShape(8.dp))
        .border(1.5.dp, Color(0xFF17DB6F), RoundedCornerShape(8.dp))
        .border(1.5.dp, Color(0xFF20B161), RoundedCornerShape(8.dp))
        .border(0.8.dp, Color(0xFF20bf6b), RoundedCornerShape(8.dp))

    ListItem(
        modifier = listItemModifier,
        headlineContent = { Text(title, fontFamily = FontFamily.Default, fontSize = 17.sp) },
        supportingContent = { Text(subtitle, fontFamily = FontFamily.Default) },
        colors = ListItemDefaults.colors(color),
        leadingContent = { Icon(image, contentDescription = null) },
        trailingContent = {

                Switch(
                    checked = onCheck,
                    onCheckedChange = onChange
                )
        }
    )
}