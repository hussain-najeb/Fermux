package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.R
import org.foss.fermux.components.buttons.StandardButton
import org.foss.fermux.components.ffmpegComponents.FFmpegCard
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun IdleCard(
    ffmpegViewModel: FFmpegViewModel = viewModel(
        viewModelStoreOwner = LocalActivity.current as ComponentActivity
    )
) {
    val context = LocalContext.current

    val fileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            ffmpegViewModel.loadInputUri(context, uri)
        } else {
            ffmpegViewModel.inputUri = null
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FFmpegCard(
            modifier = Modifier.padding(15.dp)
        ) {
            if (ffmpegViewModel.inputUri == null) {
                Column(
                    modifier = Modifier
                        .aspectRatio(16f / 9f),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Upload A File",
                        fontSize = 20.sp,
                        fontWeight = W500,
                        color = FermuxColors.white,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    StandardButton(
                        image = R.drawable.upload,
                        activeColor = FermuxColors.ffCoreGreen,
                        activeIconColor = FermuxColors.white,
                        onClick = {
                            fileLauncher.launch(arrayOf("*/*"))
                        }
                    )
                }
            }
        }
    }
}