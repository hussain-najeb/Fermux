package org.foss.fermux.ffmpeg.ui.formatSheet


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.logic.MediaKind
import org.foss.fermux.ui.theme.FermuxCard
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun Screens(sheet: MediaKind, navHostController: NavHostController, viewModel: FFmpegViewModel) {
    val context = LocalContext.current

    LaunchedEffect(viewModel.inputUri) {
        viewModel.updateInputKind(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FermuxColors.fermuxBackground)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FFmpegTargetFormat.entries
                .filter { viewModel.isSheetFormat(it, sheet) }
                .forEach { format ->
                    FermuxCard(
                        cardPadding = 5.dp,
                        clickable = {
                            viewModel.selectedFormat = format
                            viewModel.inputUri?.let { uri ->
                                viewModel.startingConversion(context, uri, format)
                            }
                            navHostController.popBackStack()
                        }
                    ) {
                        Text(text = format.descriptor)
                    }
                }
        }
    }
}