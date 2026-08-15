package org.foss.fermux.ffmpeg.ui


import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun ConverterScreen(@SuppressLint("ContextCastToActivity") viewModel: FFmpegViewModel = viewModel(viewModelStoreOwner =
    LocalContext.current as ComponentActivity), navigationController: NavController) {

    LargeTopBarScaffold(
        title = "Converter",
        onBack = { navigationController.popBackStack() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .background(FermuxColors.fermuxBackground)
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FFmepgState(
                viewModel.state,
                viewModel.FFmpegLogs,
                navigationController = navigationController,
                ffmpegViewModel = viewModel
            )
        }
    }
}
