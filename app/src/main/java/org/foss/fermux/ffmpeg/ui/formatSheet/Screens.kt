package org.foss.fermux.ffmpeg.ui.formatSheet


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.foss.fermux.ffmpeg.logic.FFmpegTargetFormat
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.logic.MediaKind
import org.foss.fermux.main.GridCards
import org.foss.fermux.main.Screen

@OptIn(ExperimentalGridApi::class)
@Composable
fun Screens(sheet: MediaKind, navHostController: NavHostController, viewModel: FFmpegViewModel) {

    val context = LocalContext.current

    // Shared: detect input type once for all format sheets (bug 3)
    LaunchedEffect(viewModel.inputUri) {
        viewModel.updateInputKind(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF181825))
            .padding(13.dp),

        contentAlignment = Alignment.Center

    ) {
        Grid(
            config = {
                repeat(2) { column(1.fr) }
                row(1.5.fr)
                row(0.5.fr)
                row(1.5.fr)

                rowGap(12.dp)
                columnGap(12.dp)
            }
        )
        {
            FFmpegTargetFormat.entries
                // Shared: only show formats allowed for this sheet + input file
                .filter { viewModel.isSheetFormat(it, sheet) }
                .forEach { format ->
                    GridCards(
                        1,
                        Screen.VideoFormatSheet,
                        format.descriptor,
                        FontStyle.Italic,
                        FontFamily.Default,
                        FontWeight.W300,
                        20.sp,
                        Color.White,
                        navHostController,
                        onClick = {
                            viewModel.selectedFormat = format

                            viewModel.inputUri?.let { uri ->
                                viewModel.startingConversion(context, uri, format)
                            }

                            navHostController.popBackStack()
                        }
                    )
                }
        }
    }
}
