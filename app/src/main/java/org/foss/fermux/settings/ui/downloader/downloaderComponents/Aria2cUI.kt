package org.foss.fermux.settings.ui.downloader.downloaderComponents

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingLists
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.settings.logic.SettingListInfo
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun Aria2cOptions(
     onDismissRequest: () -> Unit,
     @SuppressLint("ContextCastToActivity") settingsViewModel: SettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)
) {
     val aria2c by settingsViewModel.aria2c.collectAsStateWithLifecycle()
     val aria2cEdgeCase by settingsViewModel.aria2cEdgeCase.collectAsStateWithLifecycle()

     val aria2cSettings =
          listOf(
               SettingListInfo(
                    title = "Aria2c Implementation",
                    description = if (aria2c) "Enabled aria2c for the downloader" else "Disabled aria2c for the downloader",
                    image = if (aria2c) R.drawable.zap else R.drawable.zap_off,
                    content = {
                         SettingsSwitch(
                              checked = aria2c,
                              onCheckedChange = { settingsViewModel.setAria2cImpl(it) }
                         )
                    }
               ),
               SettingListInfo(
                    title = "Aria2c Edge Case",
                    description = if (aria2cEdgeCase) "Use aria2c for DASH/HLS stream to skip the Best quality avoiding ffmpeg bugs" else "Using standard downloader for DASH and HLS streams",
                    icon = Icons.Default.Tune,
                    content = {
                         SettingsSwitch(
                              checked = aria2cEdgeCase,
                              onCheckedChange = { settingsViewModel.setAria2cEdgeCase(it) }
                         )
                    }
               )
          )





     BasicAlertDialog(
          onDismissRequest = onDismissRequest,
          properties = DialogProperties(
               usePlatformDefaultWidth = false,
               decorFitsSystemWindows = false
          )
     ) {
          Box(
               modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable(
                         indication = null,
                         interactionSource = remember { MutableInteractionSource() }
                    ) { onDismissRequest() },
               contentAlignment = Alignment.Center
          ) {
               Surface(
                    shape = MaterialTheme.shapes.large,
                    color = FermuxColors.fermuxComponents
               ) {
                    Column(
                         modifier = Modifier
                              .padding(8.dp)
                              .verticalScroll(rememberScrollState())
                    ) {
                         Text(
                              text = "Aria2c Switches",
                              fontSize = 25.sp,
                              color = FermuxColors.fermuxWhiteColor,
                              style = MaterialTheme.typography.headlineMediumEmphasized,
                              modifier = Modifier.padding(7.dp)
                         )

                         Spacer(modifier = Modifier.height(8.dp))

                         Text(
                              text = "Turn On/Off Aria2c Implementation with it's edge case if needed",
                              color = FermuxColors.fermuxOffWhiteTextColor,
                              fontSize = 15.sp,
                              style = MaterialTheme.typography.bodyMedium,
                              modifier = Modifier.padding(10.dp)
                         )

                         aria2cSettings.forEach { setting ->
                              SettingLists(
                                   title = setting.title,
                                   description = setting.description,
                                   icon = setting.icon,
                                   image = setting.image,
                                   content = setting.content,
                                   trailingContent = setting.trailingContent,
                                   onClick = {
                                        setting.onClick?.invoke()
                                   }
                              )
                         }



                    }
               }
          }
     }
}