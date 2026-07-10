package org.foss.fermux.ffmpeg.ui


import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel


@Composable
fun ConverterScreen(@SuppressLint("ContextCastToActivity") viewModel: FFmpegViewModel = viewModel(viewModelStoreOwner =
    LocalContext.current as ComponentActivity), navigationController: NavController) {

    val context = LocalContext.current

    Column(modifier = Modifier
        .background(Color(0xFF181825))
        .fillMaxSize()) {

        FFmepgState(viewModel.state, viewModel.FFmpegLogs, navigationController = navigationController)




    }
}