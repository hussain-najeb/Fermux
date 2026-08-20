package org.foss.fermux.fermuxUIComponents.downloaderComponents

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.fermuxUIComponents.buttons.LogCopy
import org.foss.fermux.fermuxUIComponents.generalComponents.LargeTopBarScaffold
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel


@Composable
fun DownloaderLogs(
navController: NavHostController
	) {	 
	val downloaderViewModel: DownloaderViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)


	@Suppress("DEPRECATION") val clipboard = LocalClipboardManager.current
	val scrollState = rememberScrollState()

	Column(
		modifier = Modifier
			.fillMaxSize()
			.background(FermuxColors.fermuxBackground)
	) {

		LargeTopBarScaffold(
			title = "Logs",
			onBack = { navController.popBackStack() }
		) { paddingValues ->


			Column(
				modifier = Modifier
					.verticalScroll(scrollState)
					.padding(paddingValues)
					.fillMaxSize()
					.background(FermuxColors.fermuxBackground),
				verticalArrangement = Arrangement.Center
			) {

				Surface(
					modifier = Modifier
					.padding(5.dp),
					color = FermuxColors.fermuxComponents,
					shape = RoundedCornerShape(10.dp),
					border = BorderStroke(1.dp, FermuxColors.fermuxWhiteColor)
				) {
					Text(
						text = downloaderViewModel.downloaderLogs,
						color = FermuxColors.fermuxWhiteColor,
						fontFamily = JetbrainsMono,
						modifier = Modifier.padding(14.dp)
					)
					Box(modifier = Modifier
						.fillMaxSize()
						.padding(5.dp)
						) {
						LogCopy(
							onClick = { clipboard.setText(AnnotatedString(downloaderViewModel.downloaderLogs)) }
							)
					}
				}
			}
		}
	}
}