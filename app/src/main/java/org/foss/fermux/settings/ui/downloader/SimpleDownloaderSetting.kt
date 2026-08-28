package org.foss.fermux.settings.ui.downloader

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.SettingsResetButton
import org.foss.fermux.fermuxUIComponents.downloaderComponents.Aria2cModeSelector
import org.foss.fermux.fermuxUIComponents.downloaderComponents.DownloaderVersionSwap
import org.foss.fermux.fermuxUIComponents.downloaderComponents.RequestTimeSlider
import org.foss.fermux.fermuxUIComponents.downloaderComponents.SponsorBlockChoices
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingLists
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SimpleDownloaderPage(
     navController: NavHostController,
     @SuppressLint("ContextCastToActivity") settingsViewModel: DownloaderSettingsViewModel = viewModel(
          viewModelStoreOwner = LocalContext.current as ComponentActivity
     )
) {

     /**
      * TODO:
      *       * Make the animation smooth when the Slider appears and the Logs Surface Goes down and up, currently its janky. I have an idea for a solution. Maybe wrap all the settings in an AnimateContent
      *       * Add the cookies option in the Downloader Page
      */

     val sleepRequest by settingsViewModel.sleepRequest.collectAsStateWithLifecycle()
     val ytdlpDetails by settingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()
     val audioHistory by settingsViewModel.audioHistory.collectAsStateWithLifecycle()
     val videoHistory by settingsViewModel.videoHistory.collectAsStateWithLifecycle()
     val isCheckingForUpdate by settingsViewModel.isCheckingForUpdate.collectAsStateWithLifecycle()
     val updateChecker by settingsViewModel.upToDate.collectAsStateWithLifecycle()
     val ytdlpUpdateStatus by settingsViewModel.ytdlpUpdateStatus.collectAsStateWithLifecycle()
     val currentVersionName by settingsViewModel.currentVersionName.collectAsStateWithLifecycle()
     val sponsorBlock by settingsViewModel.sponsorBlock.collectAsStateWithLifecycle()
     val quickJS by  settingsViewModel.quickJS.collectAsStateWithLifecycle()

     val thumbnail by settingsViewModel.embedThumbnail.collectAsStateWithLifecycle()
     val notificationState by settingsViewModel.notificationState.collectAsStateWithLifecycle() // TODO. Add this at some point.
     val playlist by settingsViewModel.playlistState.collectAsStateWithLifecycle()

     var sponsorExpansion by remember { mutableStateOf(false) }
     var expanded by remember { mutableStateOf(false) }
     var aria2cExpansion by remember { mutableStateOf(false) }
     var resetDownloader by remember { mutableStateOf(false) }
     var ytdlpUpdaterExpanded by remember { mutableStateOf(false) }


     val infiniteTransition = rememberInfiniteTransition(label = "update transition")
     val rotation by infiniteTransition.animateFloat(
          initialValue = 0f,
          targetValue = 1800f,
          animationSpec = infiniteRepeatable(
               animation = tween(10000, easing = LinearEasing),
               repeatMode = RepeatMode.Restart
          ),
          label = "update rotation"
     )


     val simpleDownloaderSettings = listOf(
          SettingListInfo(
               title = "Update yt-dlp",
               description = if (isCheckingForUpdate) {
                    ytdlpUpdateStatus ?: "Checking for update..."
               } else {
                    "Current version is $currentVersionName"
               },
               icon = Icons.Default.Update,
               onClick = { ytdlpUpdaterExpanded = !ytdlpUpdaterExpanded },
               trailingContent = {
                    DownloaderVersionSwap(
                         settingsViewModel = settingsViewModel,
                         expanded = ytdlpUpdaterExpanded
                    )
               }
          ),
//          SettingListInfo(
//               title = "Download Notifications",
//               description = "Notify me when the downloaded files finish downloading",
//               image = if (notificationState) R.drawable.bell_on else R.drawable.bell_off,
//               content = {
//                    SettingsSwitch(
//                         checked = notificationState,
//                         onCheckedChange = { settingsViewModel.setNotificationState(it) }
//                    )
//               }
//          ),
          SettingListInfo(
               title = "Audio History",
               description = "Enable/Disable audio history",
               image = if (audioHistory) R.drawable.library_music_on else R.drawable.library_music_off,
               content = {
                    SettingsSwitch(
                         checked = audioHistory,
                         onCheckedChange = { settingsViewModel.setAudioHistory(it) }
                    )
               }
          ),
          SettingListInfo(
               title = "Video History",
               description = "Enable/Disable video history",
               image = if (videoHistory) R.drawable.video_library_on else R.drawable.video_library_off,
               content = {
                    SettingsSwitch(
                         checked = videoHistory,
                         onCheckedChange = { settingsViewModel.setVideoHistory(it) }
                    )
               }
          ),
          SettingListInfo(
               title = if (playlist) "Playlist On" else "Playlist Off",
               description = if (playlist) "Playlists will be downloaded when the url is copied from a playlist" else "Playlists will not be downloaded when the url is copied from a playlist",
               image = if (playlist) R.drawable.playlist_on else R.drawable.playlist_off,
               content = {
                    SettingsSwitch(
                         checked = playlist,
                         onCheckedChange = { settingsViewModel.setPlaylistState(it) }
                    )
               }
          ),
          SettingListInfo(
               title = if (ytdlpDetails) "Shown Logs" else "Hidden Logs",
               description = if (ytdlpDetails) "Shown the downloader Logs" else "Hidden the downloader Logs",
               image = if (ytdlpDetails) R.drawable.eye_open else R.drawable.eye_closed,
               content = {
                    SettingsSwitch(
                         checked = ytdlpDetails,
                         onCheckedChange = {
                              settingsViewModel.setYtdlpDetails(it)
                         }
                    )
               }
          ),
     )

     val advancedSettings = listOf(
          SettingListInfo(
               title = "SponsorBlock",
               description = "SponsorBlock API integration for cutting promotions when downloading",
               image = R.drawable.sponsorblock,
               onClick = { sponsorExpansion = !sponsorExpansion },
               content = {
                    SettingsSwitch(
                         checked = sponsorBlock,
                         onCheckedChange = { settingsViewModel.setSponsorBlock(it) }
                    )
               },
               trailingContent = {
                    SponsorBlockChoices(
                         expanded = sponsorExpansion,
                         downloaderSettingsViewModel = settingsViewModel
                    )
               }
          ),
          SettingListInfo(
               title = "Aria2c",
               description = "Aria2c Implementation for better download speeds, especially for large files. Use the Edge Case option when downloading on the highest setting in the downloader",
               image = R.drawable.layers,
               onClick = { aria2cExpansion = !aria2cExpansion },
               trailingContent = {
                    Aria2cModeSelector(
                         expanded = aria2cExpansion,
                         downloaderSettingsViewModel = settingsViewModel
                    )
               }
          ),
          SettingListInfo(
               title = if (thumbnail) "Uncut Thumbnail" else "Cut Thumbnail",
               description = if (thumbnail) "The thumbnail of the downloaded media will be embedded and will be saved"
               else "The thumbnail of the downloaded media will be removed and won't be saved",
               image = if (thumbnail) R.drawable.scissors_off else R.drawable.scissors_on,
               content = {
                    SettingsSwitch(
                         checked = thumbnail,
                         onCheckedChange = {
                              settingsViewModel.setEmbedThumbnail(it)
                         }
                    )
               }
          ),
          SettingListInfo(
               title = "Sleep Request Ytdlp Flag",
               description = "Sleep request is a flag for delayed download between each request, each number represents a second. 0 means the flag is off",
               icon = if (sleepRequest > 0) Icons.Filled.Flag else Icons.Outlined.Flag,
               onClick = { expanded = !expanded },
               trailingContent = {
                    RequestTimeSlider(
                         expanded = expanded
                    )
               }
          ),
          SettingListInfo(
               title = "Quick JS Framework",
               description = "QuickJS is a JavaScript engine yt-dlp uses to solve YouTube's PO token challenges and bypass Google's anti-bot measures",
               image = if (quickJS) R.drawable.flash_on else R.drawable.flash_off,
               content = {
                    SettingsSwitch(
                         checked = quickJS,
                         onCheckedChange = {
                              settingsViewModel.setQuickJS(it)
                         }
                    )
               }
          ),
          SettingListInfo(
               title = "Reset Downloader Settings",
               description = "Reset the downloader settings to there original state",
               icon = Icons.Default.SettingsBackupRestore,
               onClick = { resetDownloader = !resetDownloader },
               trailingContent = {
                    SettingsResetButton(
                         expanded = resetDownloader,
                         onClick = { settingsViewModel.setClearYtdlp() } // TODO. Add toast here so the user knows its been done
                    )
               }
          )
     )

     LargeTopBarScaffold(
          title = "Downloader Settings",
          onBack = { navController.popBackStack() }
     ) { paddingValues ->
          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
          ) {
               Text(
                    text = "General",
                    modifier = Modifier.padding(
                         start = 16.dp,
                         top = 20.dp,
                         bottom = 8.dp
                    ),
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               simpleDownloaderSettings.forEach { setting ->
                    SettingLists(
                         title = setting.title,
                         description = setting.description,
                         icon = setting.icon,
                         image = setting.image,
                         content = setting.content,
                         trailingContent = setting.trailingContent,
                         onClick = {
                              setting.onClick?.invoke()
                              setting.route?.let { navController.navigate(it) }
                         },
                    )
               }

               Text(
                    text = "Advanced",
                    modifier = Modifier.padding(
                         start = 16.dp,
                         top = 20.dp,
                         bottom = 8.dp
                    ),
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               advancedSettings.forEach { setting ->
                    SettingLists(
                         title = setting.title,
                         description = setting.description,
                         icon = setting.icon,
                         image = setting.image,
                         content = setting.content,
                         trailingContent = setting.trailingContent,
                         onClick = {
                              setting.onClick?.invoke()
                              setting.route?.let { navController.navigate(it) }
                         },
                    )
               }
          }
     }
}
