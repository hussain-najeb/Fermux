package org.foss.fermux.ytdlp.ui.ytdlpMainScreen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun SponsorBlockPage() {

     var showSponsorBlockDialog by remember { mutableStateOf(false) }

     if (showSponsorBlockDialog) {
          AlertDialog(
               onDismissRequest = { showSponsorBlockDialog = false },
               title = { Text("SponsorBlock Settings") },
               text = { Text("Configure your SponsorBlock preferences here") },
               confirmButton = {
                    Button(onClick = { showSponsorBlockDialog = false }) {
                         Text("OK")
                    }
               }
          )
     }


}