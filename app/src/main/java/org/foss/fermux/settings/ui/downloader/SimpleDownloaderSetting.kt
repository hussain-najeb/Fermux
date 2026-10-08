package org.foss.fermux.settings.ui.downloader

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.MediumTopBarScaffold
import org.foss.fermux.components.generalComponents.ModularSegmentedButtons
import org.foss.fermux.components.settingsComponents.SettingsSwitch
import org.foss.fermux.components.settingsComponents.TileOptions
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.TilePosition
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.rememberNotificationPermissionRequest
import org.foss.fermux.ytdlp.logic.downloader.YtdlpChannel


@Composable
fun SimpleDownloaderPage(
     navController: NavHostController,
     downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
) {
     // DataStore vals
     val ytdlpDetails by downloaderSettingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()
     val history by downloaderSettingsViewModel.history.collectAsStateWithLifecycle()
     val currentVersionName by downloaderSettingsViewModel.currentVersionName.collectAsStateWithLifecycle()
     val bellState by downloaderSettingsViewModel.downloaderBellState.collectAsStateWithLifecycle()
     val isCheckingForUpdate by downloaderSettingsViewModel.isCheckingForUpdate.collectAsStateWithLifecycle()
     val ytdlpUpdateStatus by downloaderSettingsViewModel.ytdlpUpdateStatus.collectAsStateWithLifecycle()

     // ModularSegmentedButtons vals
     val updateChannel by downloaderSettingsViewModel.ytdlpChannel.collectAsStateWithLifecycle()

     // Miscellaneous vals/funs
     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()


     val requestNotificationPermission = rememberNotificationPermissionRequest(
          onGranted = {
               scope.launch {
                    snackbarHostState.showSnackbar(
                         message = "Notifications enabled",
                         duration = SnackbarDuration.Short
                    )
               }
          },
          onPermissionDenied = {
               downloaderSettingsViewModel.setDownloaderBellState(false)
               scope.launch {
                    snackbarHostState.showSnackbar(
                         message = "Permission denied",
                         duration = SnackbarDuration.Short
                    )
               }
          }
     )

     val simpleDownloaderSettings = listOf(
          SettingListInfo(
               title = "Update Yt-dlp",
               description = if (isCheckingForUpdate) ytdlpUpdateStatus ?: "Checking for update..." else "Current version is $currentVersionName",
               icon = Icons.Default.Update,
               dialogAppearance = true,
               dialogTitle = "Updating Yt-dlp",
               dialogImage = R.drawable.update_icon,
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.white)) { append("You must update") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append(" yt-dlp") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" so you get less bugs, better support, and more features. It's recommended to get the ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("Nightly") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" version.") }
               },
               dialogContent = {
                    ModularSegmentedButtons(
                         enabled = !isCheckingForUpdate,
                         optionsList = listOf(
                              YtdlpChannel.Stable to "Stable",
                              YtdlpChannel.Nightly to "Nightly",
                              YtdlpChannel.Master to "Master"
                         ),
                         selectedOption = updateChannel,
                         onOptionSelected = { downloaderSettingsViewModel.checkYtdlpUpdate(it) }
                    )
               },
               position = TilePosition.TOP
          ),
          SettingListInfo(
               title = "Notifications",
               description = "Press to enable notifications",
               image = if (bellState) R.drawable.bell_on else R.drawable.bell_off,
               onClick = requestNotificationPermission
          ),
          SettingListInfo(
               title = "History",
               description = "Enable/Disable history",
               image = if (history) R.drawable.history else R.drawable.history, // TODO. add in the approprate icon for this
               content = {
                    SettingsSwitch(
                         checked = history, onCheckedChange = { downloaderSettingsViewModel.setHistory(it) })
               },
               position = TilePosition.MIDDLE
          ),
          SettingListInfo(
               title = if (ytdlpDetails) "Shown Logs" else "Hidden Logs",
               liner = true,
               description = if (ytdlpDetails) "Shown the downloader Logs" else "Hidden the downloader Logs",
               image = if (ytdlpDetails) R.drawable.eye_open else R.drawable.eye_closed,
               dialogAppearance = true,
               dialogTitle = "Logging",
               dialogImage = R.drawable.log,
               specialDescription = buildAnnotatedString {
                    withStyle(SpanStyle(FermuxColors.white)) { append("This is the flag for enabling the ") }
                    withStyle(SpanStyle(FermuxColors.warmBlue, fontStyle = FontStyle.Italic)) { append("yt-dlp") }
                    withStyle(SpanStyle(FermuxColors.white)) { append(" logs, for better inspection of what happens under-the-hood.") }
               },
               content = {
                    SettingsSwitch(
                         checked = ytdlpDetails, onCheckedChange = {
                              downloaderSettingsViewModel.setYtdlpDetails(it)
                         }
                    )
               },
               position = TilePosition.BOTTOM
          ),
     )


     MediumTopBarScaffold(
          title = "Downloader Settings",
          onBack = { navController.popBackStack() },
          snackbarHost = { AppSnackBar(snackbarHostState) }
     ) { paddingValues ->
          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.background)
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues),
          ) {
               Text(
                    text = "General",
                    modifier = Modifier.padding(
                         start = 16.dp, top = 20.dp, bottom = 8.dp
                    ),
                    fontSize = 18.sp,
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               simpleDownloaderSettings.forEach { setting ->
                    TileOptions(
                         title = setting.title,
                         description = setting.description,
                         shape = setting.position.TileShaper(),
                         icon = setting.icon,
                         image = setting.image,
                         content = setting.content,
                         trailingContent = setting.trailingContent,
                         onClick = {
                              setting.onClick?.invoke()
                              setting.route?.let { navController.navigate(it) }
                         },
                         dialogShow = setting.dialogAppearance,
                         dialogTitle = setting.dialogTitle,
                         dialogDescription = setting.dialogDescription,
                         dialogImage = setting.dialogImage,
                         dialogContent = setting.dialogContent,
                         specialDescription = setting.specialDescription,
                         liner = setting.liner
                    )
               }

               Text(
                    text = "Advanced",
                    modifier = Modifier.padding(
                         start = 16.dp, top = 20.dp, bottom = 8.dp
                    ),
                    fontSize = 18.sp,
                    color = FermuxColors.fermuxActiveButton,
                    style = MaterialTheme.typography.labelLarge,
               )

               AdvancedDownloaderSettings(
                    downloaderSettingsViewModel = downloaderSettingsViewModel,
                    navController = navController,
                    snackbarHostState = snackbarHostState
               )

               Spacer(modifier = Modifier.padding(top = 10.dp))
          }
     }
}
