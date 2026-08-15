
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import org.foss.fermux.fermuxUIComponents.buttons.MainActionButton
import org.foss.fermux.fermuxUIComponents.buttons.TextWithIconButton
import org.foss.fermux.fermuxUIComponents.generalComponents.AppCard
import org.foss.fermux.fermuxUIComponents.generalComponents.AppSurface
import org.foss.fermux.ffmpeg.logic.FFmpegViewModel
import org.foss.fermux.main.MainScreens
import org.foss.fermux.ui.theme.FermuxColors


private data class FormatButtonItem(
     val format: MainScreens,
     val text: String
)

@Composable
fun IdleCard(
     @SuppressLint("ContextCastToActivity") ffmpegViewModel: FFmpegViewModel = viewModel(
          viewModelStoreOwner = LocalContext.current as ComponentActivity
     ),
     navigationController: NavController,
) {
     val context = LocalContext.current
     var expanded by remember { mutableStateOf(false) }

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
               Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {

                    if (ffmpegViewModel.inputUri == null) {
                         Column(
                              modifier = Modifier
                                   .fillMaxSize()
                                   .background(FermuxColors.fermuxSurface),
                              verticalArrangement = Arrangement.Center,
                              horizontalAlignment = Alignment.CenterHorizontally
                         ) {
                              Text(text = "Upload a file to convert",
                                   fontSize = 14.sp,
                                   fontFamily = FontFamily.Monospace,
                                   fontStyle = FontStyle.Normal,
                                   color = FermuxColors.fermuxInActiveTextColor,
                                   modifier = Modifier.padding(8.dp)
                              )

                              Spacer(modifier = Modifier.height(8.dp))

                              MainActionButton(
                                   icon = Icons.Default.Upload,
                                   modifier = Modifier
                                        .defaultMinSize(minWidth = 70.dp)
                                        .padding(8.dp),
                                   onClick = {fileLauncher.launch("*/*")}
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

                         TextWithIconButton(
                              modifier = Modifier
                                   .align(Alignment.BottomStart)
                                   .padding(10.dp),
                              icon = Icons.Default.ExpandMore,
                              contentPadding = PaddingValues(8.dp),
                              iconRotation = if (expanded) 180f else 0f,
                              text = if (expanded) "Hide Format" else "Show Format",
                              onClick = { expanded = !expanded }
                         )
                    }
               }

               AppSurface(expanded = expanded) {

                    val formatButtons = listOf(
                         FormatButtonItem(
                              format = MainScreens.AudioFormatSheet,
                              text = "Press To Convert To Audio"
                         ),
                         FormatButtonItem(
                              format = MainScreens.VideoFormatSheet,
                              text = "Press To Convert To Video"
                         ),
                         FormatButtonItem(
                              format = MainScreens.ImageFormatSheet,
                              text = "Press To Convert To Image"
                         )
                    )

                    Column(
                         modifier = Modifier
                              .fillMaxWidth()
                              .padding(10.dp),
                    ) {
                         formatButtons.forEach { item ->
                              Row(
                                   verticalAlignment = Alignment.CenterVertically
                              ) {
                                   TextWithIconButton(
                                        text = item.text,
                                        contentPadding = PaddingValues(8.dp),
                                        modifier = Modifier
                                             .fillMaxWidth()
                                             .height(60.dp)
                                             .padding(5.dp),
                                        onClick = { navigationController.navigate(item.format.route) }
                                   )
                              }
                         }
                    }
               }
          }
     }
}