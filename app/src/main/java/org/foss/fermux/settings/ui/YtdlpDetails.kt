package org.foss.fermux.settings.ui


import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.R
import org.foss.fermux.fermuxUIComponents.settingsComponents.FermuxSettingsSwitch
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors


@SuppressLint("ContextCastToActivity")
@Composable
fun SponsorBlockOptions(onDismissRequest: () -> Unit) {
     BackHandler(onBack = onDismissRequest)

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
          "Skip annoying talking at end of songs"
     )

     val sponsorBlockColors = mapOf(
          "sponsor" to painterResource(id = R.drawable.sponsor),
          "selfpromo" to painterResource(id = R.drawable.selfpromo),
          "intro" to painterResource(id = R.drawable.intro),
          "outro" to painterResource(id = R.drawable.outro),
          "preview" to painterResource(id = R.drawable.preview),
          "music_offtopic" to painterResource(id = R.drawable.music_offtopic),
     )
     val sponsorBlockTintColors = mapOf(
          "sponsor" to Color(0xFF00D400),
          "selfpromo" to Color(0xFFFFFF00),
          "intro" to Color(0xFF00FFFF),
          "outro" to Color(0xFF0202ED),
          "preview" to Color(0xFF008FD6),
          "music_offtopic" to Color(0xFFFF9900),
     )

     Box(
          modifier = Modifier
               .fillMaxSize()
               .background(Color.Black.copy(alpha = 0.6f))
               .clickable { onDismissRequest() },
          contentAlignment = Alignment.Center
     ) {
               Surface(
                    modifier = Modifier
                         .fillMaxWidth(0.9f)
                         .clickable { }
                         .border(1.dp, FermuxColors.fermuxGenericBorder, RoundedCornerShape(14.dp))
                         .wrapContentHeight(),
                    shape = MaterialTheme.shapes.large,
                    tonalElevation = 2.dp,
                    color = FermuxColors.fermuxComponents
               ) {
                    Column(
                         modifier = Modifier
                              .padding(9.dp)
                              .verticalScroll(rememberScrollState())
                    ) {

                         Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.TopStart) {
                              Text(
                                   text = "Sponsor Categories",
                                   fontSize = 25.sp,
                                   color = FermuxColors.fermuxWhiteColor,
                                   style = MaterialTheme.typography.headlineMediumEmphasized,
                                   modifier = Modifier.padding(7.dp).align(Alignment.TopCenter)
                              )
                         }

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
                              Row(
                                   modifier = Modifier
                                        .fillMaxWidth(),
                                   verticalAlignment = Alignment.CenterVertically,
                                   horizontalArrangement = Arrangement.SpaceEvenly
                              ) {
                                   FermuxSettingsSwitch(
                                        settingTitle = label,
                                        settingDescription = sponsorDescriptions[index],
                                        settingImage = sponsorBlockColors[flag],
                                        iconTint = sponsorBlockTintColors[flag] ?: Color.Gray,
                                        onChecked = isChecked,
                                        onCheckedChange = { checked ->
                                             val updated = if (checked) sponsorBlockCategories + flag
                                             else sponsorBlockCategories - flag
                                             settingsViewModel.setSponsorBlockCategories(updated)
                                        }

                                   )
                              }
                         }


                    }
               }
          }
     }
