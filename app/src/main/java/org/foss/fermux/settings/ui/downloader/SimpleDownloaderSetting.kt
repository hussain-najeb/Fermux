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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Update
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.outlined.Terminal
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.buttons.AppIconButton
import org.foss.fermux.fermuxUIComponents.buttons.ImageButton
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.fermuxUIComponents.settingsComponents.RequestTimeSlider
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingLists
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.settings.logic.getAppVersionName
import org.foss.fermux.settings.ui.UpdateState
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SimpleDownloaderPage(
     navController: NavHostController,
     @SuppressLint("ContextCastToActivity") settingsViewModel: SettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {

     /**
      * TODO. Make the aniamtion smooth when the Slider appears and the Logs Surface Goes down and up, currently its janky. I have an idea for a solution. Maybe wrap all the settings in an AnimateContent
      */

     val ytdlpDetails by settingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()
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
     val notificationState by settingsViewModel.notificationState.collectAsStateWithLifecycle()

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
     var expanded by remember { mutableStateOf(false) }

     val simpleDownloaderSettings = listOf(
          SettingListInfo(
               title = "Update Ytdlp",
               description = "Update your current version of ytdlp. Current version is ${settingsViewModel.currentVersionName}",
               icon = Icons.Default.Update,
               content = {
                    val updatingIcon = updateIconPainter(updateState)
                    ImageButton(
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
               title = "Download Notifications",
               description = "Notify me when the downloaded files finish downloading",
               image = if (notificationState) painterResource(R.drawable.bell_on) else painterResource(R.drawable.bell_off),
               content = {
                    SettingsSwitch(
                         checked = notificationState,
                         onCheckedChange = { settingsViewModel.setNotificationState(it) }
                    )
               }
          ),
          SettingListInfo(
               title = "Audio History",
               description = "Enable/Disable audio history",
               icon = Icons.Default.AudioFile,
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
               icon = Icons.Default.VideoFile,
               content = {
                    SettingsSwitch(
                         checked = videoHistory,
                         onCheckedChange = { settingsViewModel.setVideoHistory(it) }
                    )
               },
          ),
          SettingListInfo(
               title = "Sleep Request Ytdlp Flag",
               description = "Sleep Request is a ytdlp flag for delayed download between each request",
               icon = Icons.Outlined.Terminal,
               content = {
                    AppIconButton(
                         modifier = Modifier.size(50.dp),
                         icon = Icons.Default.ExpandMore,
                         iconRotation = if (expanded) 180f else 0f ,
                         onClick = {
                              expanded = !expanded
                         }
                    )
               },
               trailingContent = {
                    RequestTimeSlider(
                         expanded = expanded
                    )
               }
          ),
          SettingListInfo(
               title = if (ytdlpDetails) "Hide Logs" else "Show Logs",
               description = if (ytdlpDetails) "Hide the downloader Logs" else "Show the downloader Logs",
               image = if (ytdlpDetails) painterResource(id = R.drawable.eye_open) else painterResource(id = R.drawable.eye_closed),
               content = {
                    SettingsSwitch(
                         checked = ytdlpDetails,
                         onCheckedChange = {
                              settingsViewModel.setYtdlpDetails(it)
                         }
                    )
               }
          )
     )


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


               simpleDownloaderSettings.forEach { setting ->
                    SettingLists(
                         title = setting.title,
                         description = setting.description,
                         icon = setting.icon,
                         image = setting.image,
                         content = setting.content,
                         trailingContent = setting.trailingContent,
                         onClick = {
                              setting.route?.let { navController.navigate(it) }
                         },
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
