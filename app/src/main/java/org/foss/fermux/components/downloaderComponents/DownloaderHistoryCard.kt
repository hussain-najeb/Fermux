package org.foss.fermux.components.downloaderComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import org.foss.fermux.R
import org.foss.fermux.components.buttons.SmallActionButton
import org.foss.fermux.database.DownloadsDatabaseViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.resolutionFormatting
import org.foss.fermux.ytdlp.logic.downloader.videoTime

@Composable
fun HistoryCard(
     thumbnail: String?,
     imageDescription: String?,
     title: String?,
     uploader: String?,
     format: String?,
     resolution: String?,
     duration: Int?,
     onMenuClick: () -> Unit
) {

     val context = LocalContext.current
     val viewModel: DownloadsDatabaseViewModel = viewModel(factory = DownloadsDatabaseViewModel.factory(context))


     val interactionSource = remember { MutableInteractionSource() }
     val isPressed by interactionSource.collectIsPressedAsState()

     var expanded by remember { mutableStateOf(false) }

     val titleColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxOffWhiteTextColor else FermuxColors.white,
          label = "color of main title of main page"
     )

     val descriptionBackground by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.fermuxSurface else FermuxColors.fermuxSaturatedComponents,
          label = "color of main background of the description surface"
     )

     val size by animateFloatAsState(
          targetValue = if (isPressed) 0.98f else 1f,
          animationSpec = MaterialTheme.motionScheme.fastEffectsSpec(),
          label = ""
     )

     val borderColor by animateColorAsState(
          targetValue = if (isPressed) FermuxColors.skyBrightDark else FermuxColors.skyBright,
     )


     Surface(
          modifier = Modifier
          .fillMaxWidth()
               .graphicsLayer {
                    scaleY = size
                    scaleX = size
               }
               .wrapContentSize()
          .clickable(
               interactionSource = interactionSource,
               indication = null
          ) {
               // TODO. Add the dialog here
          }
          .height(100.dp),
          color = descriptionBackground,
          shape = RoundedCornerShape(8.dp),
          border = BorderStroke(1.dp, borderColor)
     ) {
          Row(
               modifier = Modifier.fillMaxWidth()
          ) {
               Surface(
                    modifier = Modifier
                         .wrapContentSize()
                         .align(Alignment.CenterVertically),
                    shape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
               ) {
                    Box(modifier = Modifier
                         .wrapContentSize()
                    ) {
                         AsyncImage(
                              model = thumbnail,
                              contentDescription = imageDescription,
                              contentScale = ContentScale.Crop,
                              fallback = painterResource(id = R.drawable.file_unknown),
                              error = painterResource(id = R.drawable.file_error),
                              modifier = Modifier
                                   .aspectRatio(16f / 9f)
                                   .clip(shape = RoundedCornerShape(8.dp))
                         )
                         Box(modifier = Modifier
                              .padding(5.dp)
                              .wrapContentSize()
                              .background(
                                   color = FermuxColors.fermuxComponents.copy(alpha = 0.75f),
                                   shape = RoundedCornerShape(5.dp)
                              )
                              .wrapContentSize()
                              .align(Alignment.BottomEnd)
                         ) {
                              if (resolution != null) {
                                   Text(
                                        text = resolutionFormatting(resolution),
                                        color = FermuxColors.white,
                                        fontStyle = FontStyle.Normal,
                                        fontFamily = FontFamily.Default,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(3.dp)
                                   )
                              }
                         }
                         Box(modifier = Modifier
                              .padding(5.dp)
                              .wrapContentSize()
                              .background(
                                   color = FermuxColors.fermuxComponents.copy(alpha = 0.75f),
                                   shape = RoundedCornerShape(5.dp)
                              )
                              .wrapContentSize()
                              .align(Alignment.BottomStart)
                         ) {
                              if (duration != null) {
                                   Text(
                                        text = videoTime(duration),
                                        color = FermuxColors.white,
                                        fontStyle = FontStyle.Normal,
                                        fontFamily = FontFamily.Default,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(3.dp)
                                   )
                              }
                         }
                    }
               }
               Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = descriptionBackground,
               ) {
                    Row(modifier = Modifier.padding(2.dp).fillMaxSize()) {
                         Column(
                              modifier = Modifier
                                   .weight(1f)
                                   .padding(5.dp)
                         ) {
                              if (title != null) {
                                   Text(
                                        text = title,
                                        fontSize = 14.sp,
                                        fontStyle = FontStyle.Normal,
                                        fontFamily = FontFamily.Default,
                                        fontWeight = FontWeight.SemiBold,
                                        minLines = 1,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        color = titleColor,
                                        modifier = Modifier.padding(2.dp)
                                   )
                              }
                              Row(modifier = Modifier.padding(5.dp)) {
                                   if (uploader != null) {
                                        Text(
                                             text = uploader,
                                             fontSize = 13.sp,
                                             fontStyle = FontStyle.Normal,
                                             fontFamily = FontFamily.Default,
                                             fontWeight = FontWeight.Normal,
                                             minLines = 1,
                                             maxLines = 1,
                                             overflow = TextOverflow.Ellipsis,
                                             color = titleColor,
                                             modifier = Modifier.padding(2.dp)
                                        )
                                   }
                                   if (format != null) {
                                        Text(
                                             text = "| $format",
                                             fontSize = 13.sp,
                                             fontStyle = FontStyle.Normal,
                                             fontFamily = FontFamily.Default,
                                             fontWeight = FontWeight.Normal,
                                             minLines = 1,
                                             maxLines = 1,
                                             overflow = TextOverflow.Ellipsis,
                                             color = titleColor,
                                             modifier = Modifier.padding(2.dp)
                                        )
                                   }
                              }
                         }
                         SmallActionButton(
                              modifier = Modifier
                                   .align(Alignment.Bottom)
                                   .padding(5.dp),
                              onClick = onMenuClick,
                              image = R.drawable.info_circle
                         )
                    }
               }
          }
     }
}

@Preview(backgroundColor = 181825, showBackground = true)
@Composable
private fun test11() {

     Column(modifier = Modifier
          .fillMaxSize()
          .background(FermuxColors.fermuxBackground)
          .padding(10.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
     ) {

     Spacer(modifier = Modifier.height(20.dp))

          HistoryCard(
               thumbnail = "/home/Hussain/Downloads/thumbnail.webp",
               imageDescription = "Video thumbnail",
               title = "Never Gonna Give You Up - Rick Astley",
               uploader = "Rick Astley",
               format = "MP4",
               resolution = resolutionFormatting("1028px720p"),
               duration = 666,
               onMenuClick = {}
          )
          HistoryCard(
               thumbnail = "/home/Hussain/Downloads/images.webp",
               imageDescription = "Video thumbnail",
               title = "Never Gonna Give You Up - Rick Astley",
               uploader = "Rick Astley",
               format = "MP4",
               resolution = resolutionFormatting("1028px720p"),
               duration = 404,
               onMenuClick = {}
          )
          HistoryCard(
               thumbnail = "/home/Hussain/Downloads/galagcy.webp",
               imageDescription = "Video thumbnail",
               title = "Never Gonna Give You Up - Rick Astley",
               uploader = "Rick Astley",
               format = "MP4",
               resolution = resolutionFormatting("1028px720p"),
               duration = 808,
               onMenuClick = {}
          )
     }
}