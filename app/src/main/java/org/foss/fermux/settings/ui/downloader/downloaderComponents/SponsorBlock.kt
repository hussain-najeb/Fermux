@file:Suppress("SpellCheckingInspection")

package org.foss.fermux.settings.ui.downloader.downloaderComponents


import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingLists
import org.foss.fermux.fermuxUIComponents.settingsComponents.SettingsSwitch
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors


@SuppressLint("ContextCastToActivity")
@Composable
fun SponsorBlockOptions(onDismissRequest: () -> Unit) {
     val settingsViewModel: SettingsViewModel =
          viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)

     val sponsorBlockCategories by settingsViewModel.sponsorBlockCategories.collectAsStateWithLifecycle()

     val sponsorBlockFlags = listOf(
          "sponsor" to "Skipping Sponsor",
          "selfpromo" to "Self Promotion",
          "intro" to "Intro",
          "outro" to "Outro",
          "preview" to "Preview/Recap",
          "music_offtopic" to "Non-Music Section",
     )
     val sponsorDescriptions = listOf(
          "Skip the in-video sponsor section",
          "Skip the self promotion section",
          "Skip the into of the video",
          "Skip the outro of the video",
          "Skip the preview section",
          "Skip talking at the end of songs"
     )


     val sponsorBlockTintColors = mapOf(
          "sponsor" to Color(0xFF00D400),
          "selfpromo" to Color(0xFFFFFF00),
          "intro" to Color(0xFF00FFFF),
          "outro" to Color(0xFF0202ED),
          "preview" to Color(0xFF008FD6),
          "music_offtopic" to Color(0xFFFF9900),
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
                         text = "Sponsor Categories",
                         fontSize = 25.sp,
                         color = FermuxColors.fermuxWhiteColor,
                         style = MaterialTheme.typography.headlineMediumEmphasized,
                         modifier = Modifier.padding(7.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                         text = "Turn On/Off SponsorBlock add for Video Segments. Default is fine for most media",
                         color = FermuxColors.fermuxOffWhiteTextColor,
                         fontSize = 15.sp,
                         style = MaterialTheme.typography.bodyMedium,
                         modifier = Modifier.padding(10.dp)
                    )

                    sponsorBlockFlags.forEachIndexed { index, (flag, label) ->
                         val isChecked = flag in sponsorBlockCategories
                         SettingLists(
                              title = label,
                              description = sponsorDescriptions[index],
                              leadingContent = {
                                   Box(
                                        modifier = Modifier
                                             .clip(RoundedCornerShape(4.dp))
                                             .padding(end = 10.dp)
                                             .size(12.dp)
                                             .background(sponsorBlockTintColors.getValue(flag))
                                   )
                              },
                              onClick = {
                                   val updated = if (isChecked) sponsorBlockCategories - flag
                                   else sponsorBlockCategories + flag
                                   settingsViewModel.setSponsorBlockCategories(updated)
                              },
                              content = {
                                   SettingsSwitch(
                                        checked = isChecked,
                                        onCheckedChange = { checked ->
                                             val updated = if (checked) sponsorBlockCategories + flag
                                             else sponsorBlockCategories - flag
                                             settingsViewModel.setSponsorBlockCategories(updated)
                                             }
                                        )
                                   },
                              )
                    }
               }
          }
          }
     }
}
