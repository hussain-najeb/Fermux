package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import org.foss.fermux.fermuxUIComponents.downloaderComponents.FermuxDownloadDescription
import org.foss.fermux.fermuxUIComponents.downloaderComponents.FermuxFinishedDownloadCard
import org.foss.fermux.fermuxUIComponents.buttons.FermuxCancelButton
import org.foss.fermux.fermuxUIComponents.buttons.FermuxIconButton
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.DownloadMetadata


private enum class ProgressState { InProgress, Done }

@Composable
fun FinishedCard (
     metadata: DownloadMetadata,
     progress: Float? = null,
     onCancel: () -> Unit,
     @SuppressLint("ContextCastToActivity") settingsViewModel: SettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)

) {

     val showYtdlpDetails by settingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()

     val downloadState = progress?.let {
          if (it >= 100f) ProgressState.Done else ProgressState.InProgress
     }

     // TEST LINK. https://www.youtube.com/watch?v=VD6xJq8NguY&pp=0gcJCcQLAYcqIYzv

     Column(
          modifier = Modifier
               .fillMaxWidth()
     ) {
          FermuxFinishedDownloadCard {

               Box(modifier = Modifier.clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp ))) {
                    AsyncImage(
                         model = metadata.thumbnail,
                         contentDescription = null,
                         contentScale = ContentScale.Crop,

                         modifier = Modifier
                              .fillMaxWidth()
                              .aspectRatio(16f / 9f)
                              .background(FermuxColors.fermuxSurface)
                    )
                    progress?.let {
                         when (downloadState) {
                              ProgressState.InProgress -> Column(modifier = Modifier
                                   .align(Alignment.Center)
                                   .background(FermuxColors.fermuxComponents.copy(alpha = 0.48f))
                                   .clip(RoundedCornerShape(10.dp))
                                   .size(50.dp)
                              ) {
                                   CircularWavyProgressIndicator(
                                        progress = { progress / 100f },
                                        color = FermuxColors.fermuxGenericBorder,
                                        trackColor = FermuxColors.fermuxTertiaryBorder,
                                        modifier = Modifier
                                             .padding(8.dp)
                                             .align(Alignment.CenterHorizontally)
                                   )
                              } // TODO. The check mark after the download is finished looks bad, fix it. also add a border to the whole thing!
                              ProgressState.Done -> Column(
                                   modifier = Modifier
                                        .align(Alignment.Center)
                                        .background(FermuxColors.fermuxComponents.copy(alpha = 0.48f))
                              ) {
                                   Icon(
                                        Icons.Default.Check,
                                        contentDescription = "Download Complete",
                                        modifier = Modifier
                                             .padding(8.dp)
                                             .align(Alignment.CenterHorizontally)
                                   )
                              }

                              null -> Unit
                         }
                    }
                    FermuxCancelButton(
                         modifier = Modifier
                              .align(alignment = Alignment.TopStart).padding(10.dp),
                         onClick = { onCancel() }
                    )
                    if (showYtdlpDetails) {
                         FermuxIconButton(
                              modifier = Modifier
                                   .padding(1.dp)
                                   .align(alignment = Alignment.BottomStart),
                              icon = Icons.Default.ExpandMore,
                              contentPadding = PaddingValues(8.dp),
                              onClick = { } // TODO. ADD the dialog in the function here
                         )
                    }
               }
               FermuxDownloadDescription {
                    Column(modifier = Modifier.fillMaxSize().background(FermuxColors.fermuxComponents)) {
                         Text(
                              text = metadata.title,
                              fontFamily = FontFamily.Default,
                              fontSize = 18.sp,
                              fontWeight = FontWeight.W400,
                              color = Color.White,
                              modifier = Modifier
                                   .padding(7.dp)
                         )
                         metadata.uploader?.let {
                              Text(
                                   text = it,
                                   fontFamily = FontFamily.Default,
                                   fontSize = 13.sp,
                                   color = FermuxColors.fermuxTextColorBackground,
                                   modifier = Modifier
                                        .padding(7.dp)
                              )
                         }
                    }
               }

          }
     }
}

@Preview (heightDp = 800, widthDp = 440,showBackground = true, backgroundColor = 0xFF181825  )
@Composable
fun CardDownload() {
     Column(
          modifier = Modifier
               .fillMaxWidth()
     ) {
          FermuxFinishedDownloadCard {

               Box(modifier = Modifier.clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp ))) {
                    AsyncImage(
                         model = "/home/Hussain/Downloads/maxresdefault.jpg",
                         contentDescription = "test image",
                         contentScale = ContentScale.Crop,

                         modifier = Modifier
                              .fillMaxWidth()
                              .aspectRatio(16f / 9f)
                              .background(FermuxColors.fermuxSurface)
                    )

                    FermuxCancelButton(
                         modifier = Modifier
                              .align(alignment = Alignment.TopStart).padding(10.dp),
                         onClick = { }
                    )

               }
               FermuxDownloadDescription {
                    Column(modifier = Modifier.fillMaxWidth().background(FermuxColors.fermuxComponents)) {
                         Text(
                              text = "Dummy Test Title For Video And Audio",
                              fontFamily = FontFamily.Default,
                              fontSize = 18.sp,
                              fontWeight = FontWeight.W400,
                              color = Color.White,
                              modifier = Modifier
                                   .padding(7.dp)
                         )

                         Text(
                              text = "Dummy uploader",
                              fontFamily = FontFamily.Default,
                              fontSize = 13.sp,
                              color = FermuxColors.fermuxTextColorBackground,
                              modifier = Modifier
                                   .padding(7.dp)
                         )
                    }
               }
          }
     }
}


