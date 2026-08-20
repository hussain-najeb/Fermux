package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import org.foss.fermux.fermuxUIComponents.buttons.LogCopy
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
    @Suppress("DEPRECATION") val clipboard = LocalClipboardManager.current
    val logScrollState = rememberScrollState()

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
                    .padding(paddingValues)
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    color = FermuxColors.fermuxComponents,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, FermuxColors.fermuxWhiteColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 10.dp, vertical = 12.dp)
                    ) {
                        Text(
                        text = ffmpegViewModel.FFmpegLogs,
                            color = FermuxColors.fermuxWhiteColor,
                            fontFamily = JetbrainsMono,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(logScrollState)
                                .padding(bottom = 40.dp) 
                        )
                        LogCopy(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                                .size(50.dp),
                            onClick = { clipboard.setText(AnnotatedString(ffmpegViewModel.FFmpegLogs)) }
                        )
                    }
                }
                Text(
                	text = "Note*: This is the log page for the ffmpeg output during conversion, it doesnt display errors",
                	color = FermuxColors.fermuxBackgroundTextColor,
                	fontSize = 16.sp,
                	fontStyle = FontStyle.Normal,
                	fontFamily = FontFamily.Default,
                	modifier = Modifier.padding(7.dp)
            	)
            }
        }
    }
}