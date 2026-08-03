package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderCards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.fermuxUIComponents.buttons.FermuxCancelButton
import org.foss.fermux.fermuxUIComponents.buttons.FermuxIconButton
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxCard
import org.foss.fermux.fermuxUIComponents.generalComponents.FermuxSurface
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.DownloadMetadata
import org.foss.fermux.ytdlp.logic.downloader.videoTime

@Composable
fun FinishedCard (
     metadata: DownloadMetadata,
     progress: Float? = null,
     onCancel: () -> Unit
) {
     Column(
          modifier = Modifier
               .fillMaxWidth()
     ) {
          FermuxCard(
               shape = RoundedCornerShape(12.dp),
               modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

               )
          {
               Box {
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
                         CircularWavyProgressIndicator(
                              progress = { progress / 100f },
                              color = FermuxColors.fermuxGenericBorder,
                              trackColor = FermuxColors.fermuxTertiaryBorder,
                              modifier = Modifier
                                   .padding(8.dp)
                                   .align(Alignment.BottomEnd)
                         )
                         if (progress == 100f) {
                              Icon(
                                   Icons.Default.Check,
                                   contentDescription = "Download Complete",
                                   modifier = Modifier
                                        .padding(8.dp)
                                        .align(Alignment.BottomCenter)
                              )
                         }
                    }

                    FermuxCancelButton(
                         modifier = Modifier
                              .align(alignment = Alignment.TopStart).padding(10.dp),
                         onClick = { onCancel() }
                    )
//                        if (showYtdlpDetails) {
//                            FermuxTextWithIconButton(
//                                modifier = Modifier.padding(1.dp),
//                                icon = Icons.Default.ExpandMore,
//                                contentPadding = PaddingValues(8.dp),
//                                iconRotation = if (errorLogs) 180f else 0f,
//                                text = if (errorLogs) "Hide Logs" else "Show Logs",
//                                onClick = { errorLogs = !errorLogs }
//
//                            )
                    FermuxIconButton(
                         modifier = Modifier.padding(1.dp),
                         icon = Icons.Default.ExpandMore,
                         contentPadding = PaddingValues(8.dp),
                         onClick = { } // TODO. ADD the dialog in the function here
                    )
               }
          }
     }

     FermuxSurface(expanded = true) {
          Column(modifier = Modifier.fillMaxSize()) {
               Text(
                    text = metadata.title,
                    fontFamily = FontFamily.Default,
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier
                         .padding(7.dp)
               )
               HorizontalDivider(
                    thickness = 1.0.dp,
                    color = FermuxColors.fermuxComponents
               )
               metadata.uploader?.let {
                    Text(
                         text = it,
                         fontFamily = FontFamily.Default,
                         fontSize = 12.sp,
                         color = FermuxColors.fermuxTextColorBackground,
                         modifier = Modifier
                              .padding(7.dp)
                    )
                    HorizontalDivider(
                         thickness = 1.0.dp,
                         color = FermuxColors.fermuxComponents
                    )
                    Text(
                         videoTime(metadata.duration),
                         fontFamily = FontFamily.Default,
                         color = FermuxColors.fermuxTextColorBackground,
                         modifier = Modifier
                              .padding(8.dp),
                    )
               }
          }
     }
}