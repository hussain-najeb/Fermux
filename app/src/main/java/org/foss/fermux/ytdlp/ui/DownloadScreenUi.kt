package org.foss.fermux.ytdlp.ui
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.ytdlp.logic.DownloadStatus
import org.foss.fermux.ytdlp.logic.downloaderLogic
import org.foss.fermux.ytdlp.logic.fetchingTheMetadata


//Also — imePadding() and verticalScroll
// aren't on this Column yet,
// but you don't need them until the TextField actually opens a
// keyboard and pushes content.
// Tomorrow's problem.


@SuppressLint("SuspiciousIndentation")
@Composable
fun DownloadContent() {

    var state by remember { mutableStateOf<DownloadStatus>(DownloadStatus.Idle) }

    val context = LocalContext.current

    val scope = rememberCoroutineScope()

    var downloadUrl by remember { mutableStateOf("") }

    val clipboard = LocalClipboardManager.current



    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()

    )

    {

        WhenCards(state)

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(9.dp),
            value = downloadUrl,
            onValueChange = { txt -> downloadUrl = txt },
            label = { Text("Type URL here") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false
            )

        )

        // https://www.youtube.com/watch?v=ZFSN40r--zk&list=PLpR23XfB8-1UM2BbVMGeLMAQs3oiO5MRt&index=6 <-- testing

    }

Box(contentAlignment = Alignment.BottomEnd,
    modifier = Modifier
        .fillMaxSize()
        .padding(3.dp)
    ) {

    Column (verticalArrangement = Arrangement.spacedBy(7.dp)) {

        Button(
            modifier = Modifier
                .padding(10.dp)
                .size(74.dp),

            contentPadding = PaddingValues(0.dp),

            shape = RoundedCornerShape(16.dp),

            onClick = { clipboard.getText()?.text?.let { downloadUrl = it } }

        ) {
            Icon(
                imageVector = Icons.Default.ContentPaste,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }


            Button(
                modifier = Modifier
                    .padding(10.dp)
                    .size(74.dp),
                contentPadding = PaddingValues(0.dp),
                shape = RoundedCornerShape(16.dp),
                onClick = {
                    scope.launch {

                        state = DownloadStatus.Loading

                        try {
                            val metadata = fetchingTheMetadata(downloadUrl)
                            state = DownloadStatus.Loaded(metadata)
                            downloaderLogic(context, downloadUrl) { progress ->
                                state = DownloadStatus.Downloading(progress, metadata)
                            }
                            state = DownloadStatus.Loaded(metadata)
                            Log.d("fermux", "state is set to loaded")
                            Log.d("fermux", "Download succeeded for $downloadUrl")
                        } catch (e: Exception) {
                            Log.e("fermux", "Download failed for $downloadUrl", e)
                            Log.d("fermux", "state failed to get stop the loading bar")
                        }

                    }

                }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_file_download_42),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        }

    }
}