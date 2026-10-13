package org.foss.fermux.ffmpeg.ui

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun ConverterScreen(
     navController: NavController,
     ffmpegViewModel: FFmpegViewModel = viewModel(viewModelStoreOwner = LocalActivity.current as ComponentActivity)
) {

     LargeTopBarScaffold(
          title = "Converter",
          onBack = { navController.popBackStack() }
     ) { paddingValues ->
          Column(
               modifier = Modifier
                    .background(FermuxColors.background)
                    .fillMaxSize()
                    .padding(paddingValues)
          ) {
               FFmepgState(
                    ffmpegViewModel.state,
                    navController = navController,
                    ffmpegViewModel = ffmpegViewModel
               )
          }
     }
}
