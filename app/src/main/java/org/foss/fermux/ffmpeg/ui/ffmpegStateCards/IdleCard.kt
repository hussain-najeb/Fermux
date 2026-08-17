
package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.R
import coil3.compose.AsyncImage
import org.foss.fermux.fermuxUIComponents.buttons.ImageButton
import org.foss.fermux.fermuxUIComponents.generalComponents.AppCard
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.ffmpeg.ui.FormatList
import org.foss.fermux.ui.theme.FermuxColors






@Composable
fun IdleCard(
     @SuppressLint("ContextCastToActivity") ffmpegViewModel: FFmpegViewModel = viewModel(
          viewModelStoreOwner = LocalContext.current as ComponentActivity
     ),
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
               }
          }
     }

     Column(modifier = Modifier.fillMaxSize()) {
          AppCard(
               shape = RoundedCornerShape(8.dp),
               modifier = Modifier.padding(8.dp)
          ) {
               Box(modifier = Modifier.aspectRatio(16f/9f)) {

                    if (ffmpegViewModel.inputUri == null) {
                         Column(
                              modifier = Modifier
                                   .fillMaxSize()
                                   .background(FermuxColors.fermuxSurface),
                              verticalArrangement = Arrangement.Center,
                              horizontalAlignment = Alignment.CenterHorizontally
                         ) {
                              Text(
                                   text = "Upload a file to convert",
                                   fontSize = 19.sp,
                                   fontFamily = FontFamily.Default,
                                   fontStyle = FontStyle.Normal,
                                   color = FermuxColors.fermuxInActiveTextColor,
                              )

                              Spacer(modifier = Modifier.height(8.dp))

                              ImageButton(
                                   image = painterResource(R.drawable.upload),
                                   modifier = Modifier
                                        .size(80.dp)
                                        .padding(8.dp),
                                   onClick = { fileLauncher.launch("*/*") },
                                   contentPadding = PaddingValues(9.dp)
                              )
                         }
                    }

                    if (ffmpegViewModel.inputUri != null) {

                         AsyncImage(
                              model = ffmpegViewModel.inputUri,
                              contentDescription = null,
                              contentScale = ContentScale.Crop,
                              modifier = Modifier
                                   .fillMaxSize()
                                   .background(FermuxColors.fermuxSurface)
                         )
                    }
               }
               Column(modifier = Modifier.weight(1f)) {
               FormatList(ffmpegViewModel)
               }
          }
     }
}