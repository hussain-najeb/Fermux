package org.foss.fermux.ytdlp.ui.ytdlpMainScreen


import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.border
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
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.fermuxUIComponents.settingsComponents.FermuxSettingsSwitch
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.settings.ui.SettingListInfo
import org.foss.fermux.ui.theme.FermuxColors


@SuppressLint("ContextCastToActivity")
@Composable
fun SponsorBlockOptions(onDismissRequest: () -> Unit) {
val settingsViewModel: SettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)

val sponsorBlockCategories by settingsViewModel.sponsorBlockCategories.collectAsStateWithLifecycle()

val sponsorBlockFlags = listOf(
     "sponsor",
     "selfpromo",
     "interaction",
     "intro",
     "outro",
     "preview",
     "filler",
     "music_offtopic",
)

//     val flagList = listOf(
//          SettingListInfo(
//             settingTitle = "Sponsor Skip",
//
//
//          )
//     )

     
     BasicAlertDialog(
          onDismissRequest = onDismissRequest,
          modifier = Modifier.wrapContentSize(),
          properties = DialogProperties(
               dismissOnBackPress = true,
               dismissOnClickOutside = true,
               usePlatformDefaultWidth = false
               ),
          ) {
          Surface(
               modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .border(1.dp, FermuxColors.fermuxGenericBorder, RoundedCornerShape(14.dp))
                    .wrapContentHeight(),
               shape = MaterialTheme.shapes.large,
               tonalElevation = 6.dp,
               color = FermuxColors.fermuxComponents
          ) {
               Column(modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
               ) {

                    Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.TopStart) {
                         Text(
                              text = "Sponsor Categories",
                              fontSize = 19.sp,
                              color = FermuxColors.fermuxWhiteColor,
                              style = MaterialTheme.typography.headlineMediumEmphasized
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

 //                   sponsorBlockFlags.forEach { (flag, label) ->
    //                     val isChecked = flag in sponsorBlockCategories
                         Row(
                              modifier = Modifier
                                   .padding(10.dp)
                                   .fillMaxWidth(),
                              verticalAlignment = Alignment.CenterVertically,
                              horizontalArrangement = Arrangement.SpaceEvenly
                         ) {
                              FermuxSettingsSwitch(
                                   modifier = TODO(),
                                   imageModifier = TODO(),
                                   iconModifier = TODO(),
                                   settingIcon = TODO(),
                                   settingTitle = TODO(),
                                   settingDescription = TODO(),
                                   settingImage = TODO(),
                                   color = TODO(),
                                   border = TODO(),
                                   onChecked = TODO(),
                                   onCheckedChange = TODO(),
                                   content = TODO()
                              )
                         }
                    }


               }
          }
     }




@Preview (heightDp = 800, showBackground = true, backgroundColor = 0xFF22243E )
@Composable
fun DialogPreview() {
     SponsorBlockOptions(onDismissRequest = {})
}