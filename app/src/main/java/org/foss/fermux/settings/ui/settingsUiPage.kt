package org.foss.fermux.settings.ui

import android.app.AlertDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.foss.fermux.settings.logic.DialogLogic

@Composable
fun SettingsScreen() {

    var showDownloadPath by remember { mutableStateOf(false) }
    var showLicensePage by remember { mutableStateOf(false) }


    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF1c1f24))

    ) {

        if (showDownloadPath) {
            DialogLogic(
             title = "Download path",
                text = "set where the Downloads get saved",
                onDismiss = { showDownloadPath = false }
            )
        }

        if (showLicensePage) {
            DialogLogic(
                title = "License page",
                text = "the URL here",
                onDismiss = { showLicensePage = false }
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray)
                .clickable {showDownloadPath = true}

            // adding a clickapple here to make the thing throw a Dialog here for the thing to appear
        )
        {
            Text("Download path", modifier = Modifier
                .padding(10.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
                .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray)
                .clickable { showLicensePage = true }
        ) {
            Text("License and GitHub page", modifier = Modifier
                .padding(10.dp))
        }

        Spacer(modifier = Modifier.height(10.dp))

    }

}


