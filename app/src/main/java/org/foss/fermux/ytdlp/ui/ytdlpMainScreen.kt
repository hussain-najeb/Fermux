package org.foss.fermux.ytdlp.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import kotlin.collections.listOf


// Add unique Font for the Download screen



@Composable
fun DownloaderScreen() {

    var downloadUrl by remember { mutableStateOf("") }

Column(modifier = Modifier
    .fillMaxSize()
    .background(
        Brush.verticalGradient(
        colors = listOf(Color(0xFF1d2026), Color(0xFF1c1f24), Color(0xFF282c34))
        )
    )


) {
    OutlinedTextField(
        modifier = Modifier
            .offset(x = 70.dp, y = 70.dp),
        value = downloadUrl,
        onValueChange = { txt -> downloadUrl = txt },
        label = { Text("Type URL here") },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send,
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false
        )

    )

    Button( modifier = Modifier
        .padding(top = 20.dp)
        .offset(x = 155.dp, y = 70.dp),

        onClick = {
        downloadUrl = ""
    }) {
        Text("Download")
    }

}

    Row(
        modifier = Modifier
            .width(40.dp)
            .fillMaxHeight()
            .background(Color(0xFF1a1d2e))

    ) {


        Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Placeholder!")





    }








}