package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono

@SuppressLint("ContextCastToActivity")
@Composable
fun FFmpegLogs(
navController: NavHostController
	) {
	val ffmpegViewModel: FFmpegViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)

	val scrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(FermuxColors.fermuxBackground)
	) {

		LargeTopBarScaffold(
			title = "Logs",
			onBack = { navController.popBackStack() }
		) {


			Column(
				modifier = Modifier
					.verticalScroll(scrollState)
					.padding(6.dp)
					.fillMaxSize()
					.background(FermuxColors.fermuxBackground),
				verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
			) {

				Surface(
					modifier = Modifier
					.padding(top = 180.dp, start = 5.dp, end = 5.dp),
					color = FermuxColors.fermuxComponents,
					shape = RoundedCornerShape(10.dp),
					border = BorderStroke(1.dp, FermuxColors.fermuxWhiteColor)
				) {
					Text(
						text = ffmpegViewModel.FFmpegLogs,
						color = FermuxColors.fermuxWhiteColor,
						fontFamily = JetbrainsMono,
						modifier = Modifier.padding(14.dp)
					)
				}
			}
		}
	}
}