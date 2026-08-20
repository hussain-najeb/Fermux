
package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight.Companion.W500
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.fermuxUIComponents.buttons.ImageButton
import org.foss.fermux.fermuxUIComponents.ffmpegComponents.FFmpegCard
import org.foss.fermux.ffmpeg.logic.FFmpegStatus
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.R


@Composable
fun IdleCard(
     @SuppressLint("ContextCastToActivity") ffmpegViewModel: FFmpegViewModel = viewModel(
          viewModelStoreOwner = LocalContext.current as ComponentActivity
     )
) {
     val context = LocalContext.current

     val fileLauncher = rememberLauncherForActivityResult(
          contract = ActivityResultContracts.GetContent()
     ) { uri ->
          ffmpegViewModel.inputUri = uri
          if (uri != null) {
               ffmpegViewModel.updateInputKind(context)
               if (ffmpegViewModel.inputKind == null) {
                    ffmpegViewModel.typeErrorClarification(context)
               } else {
                    ffmpegViewModel.state = FFmpegStatus.MidConversion(inputUri = uri )
               }
          }
     }

     Column(
          modifier = Modifier
               .fillMaxSize()
     ) {
          FFmpegCard(
               modifier = Modifier.padding(10.dp)
               ) {
                    if (ffmpegViewModel.inputUri == null) {
                         Column(
                              modifier = Modifier
                                   .aspectRatio(16f/9f),
                              verticalArrangement = Arrangement.Center,
                              horizontalAlignment = Alignment.CenterHorizontally
                         ) {
                              Text(
                                   text = "Upload A File",
                                   fontSize = 23.sp,
                                   fontFamily = FontFamily.Default,
                                   fontStyle = FontStyle.Normal,
                                   fontWeight = W500 ,
                                   color = FermuxColors.fermuxWhiteColor,
                              )

                              Spacer(modifier = Modifier.height(15.dp))

                              ImageButton(
                                   image = R.drawable.upload,
                                   modifier = Modifier
                                        .size(100.dp)
                                        .padding(8.dp),
                                   onClick = { fileLauncher.launch("*/*") },
                                   contentPadding = PaddingValues(10.dp)
                              )
                         }
               }
          }
     }
}