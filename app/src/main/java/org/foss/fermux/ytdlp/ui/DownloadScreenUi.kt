package org.foss.fermux.ytdlp.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.foss.fermux.ytdlp.logic.downloaderLogic


//Also — imePadding() and verticalScroll
// aren't on this Column yet,
// but you don't need them until the TextField actually opens a
// keyboard and pushes content.
// Tomorrow's problem.


@Composable
fun DownloadContent() {

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var downloadUrl by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()

    )

    {

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = downloadUrl,
            onValueChange = { txt -> downloadUrl = txt },
            label = { Text("Type URL here") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false
            )

        )
        Box {
            FilledTonalButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(top = 30.dp),

                onClick = {
                    scope.launch {
                        try {
                            withContext(Dispatchers.IO) {
                                downloaderLogic(context, downloadUrl)
                            }
                            Log.d("fermux", "Download succeeded for $downloadUrl")
                        } catch (e: Exception) {
                            Log.e("fermux", "Download failed for $downloadUrl", e)
                        }
                    }

                }) {
                Text("Download")
            }
        }
    }
}