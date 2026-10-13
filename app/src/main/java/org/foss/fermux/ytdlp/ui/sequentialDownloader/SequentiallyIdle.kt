package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import org.foss.fermux.R
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.components.generalComponents.SettingAlertDialog

@Composable
fun SequentialIdle(
     navController: NavController
) {

     var dialogDismiss by remember { mutableStateOf(false) }

     LargeTopBarScaffold(
          title = "Sequential Downloads",
          onBack = navController::popBackStack,
          firstButton = { dialogDismiss = !dialogDismiss },
          firstImage = R.drawable.info
     ) { innerPadding ->

          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
          ) {













          }
     }

     SettingAlertDialog(
          onDismissRequest = { dialogDismiss },
          title = "Sequential Download Info",
          settingImage = R.drawable.sequence,
          description = "Add a url for each text field and press the download button to download each one at a time, you don't have to fill all the text fields"
     )
}


