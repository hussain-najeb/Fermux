package org.foss.fermux.ytdlp.ui.history

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil3.compose.AsyncImage
import org.foss.fermux.R
import org.foss.fermux.components.buttons.FilterButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.openMedia
import org.foss.fermux.utils.openUrl
import org.foss.fermux.ytdlp.logic.downloader.resolutionFormatting
import org.foss.fermux.ytdlp.logic.downloader.sizeFormatting
import org.foss.fermux.ytdlp.logic.downloader.videoTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoModalSheet(
     onDismissRequest: () -> Unit,
     thumbnail: String? = null,
     imageDescription: String,
     title: String,
     uploader: String,
     duration: Int,
     format: String,
     size: Long,
     resolution: String,
     extractor: String,
     url: String,
     uri: String
) {

     val context = LocalContext.current

     val sheetState = rememberBottomSheetState(
          initialValue = SheetValue.Hidden,
          enabledValues = setOf(
               SheetValue.Hidden,
               SheetValue.Expanded
          )
     )

     ModalBottomSheet(
          modifier = Modifier
               .fillMaxWidth(),
          onDismissRequest = onDismissRequest,
          dragHandle = {
               BottomSheetDefaults.DragHandle(
                    color = FermuxColors.white
               )
          },
          sheetState = sheetState,
          containerColor = FermuxColors.darkPurple,
     ) {
          Box(
               modifier = Modifier
                    .fillMaxWidth(),
               contentAlignment = Alignment.TopCenter
          ) {
               Text(
                    text = "More Info?",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.W500,
                    color = FermuxColors.white,
                    modifier = Modifier.padding(bottom = 25.dp)
               )
          }
          Row {
               Box(
                    modifier = Modifier
                         .weight(1f)
                         .padding(start = 10.dp)
                         .size(140.dp),
                    contentAlignment = Alignment.CenterStart
               ) {
                    AsyncImage(
                         model = thumbnail,
                         contentDescription = imageDescription,
                         contentScale = ContentScale.Crop,
                         modifier = Modifier
                              .clip(RoundedCornerShape(12.dp))
                              .border(1.dp, FermuxColors.white, RoundedCornerShape(12.dp)),
                    )
               }
               Box(
                    modifier = Modifier
                         .wrapContentSize()
                         .padding(14.dp),
                    contentAlignment = Alignment.CenterEnd
               ) {
                    FilterButton(
                         modifier = Modifier
                              .size(80.dp)
                              .padding(6.dp),
                         image = R.drawable.vlc,
                         iconModifier = Modifier.size(50.dp),
                         onClick = { context.openMedia(uri.toUri()) }
                    )
               }
          }
          Box(modifier = Modifier
               .padding(start = 10.dp, top = 30.dp)
               .fillMaxWidth(),
               contentAlignment = Alignment.BottomStart
          ) {
               Column {

                    Text(
                         text = "Title: $title",
                         fontSize = 20.sp,
                         color = FermuxColors.white,
                         overflow = TextOverflow.Ellipsis,
                         modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp)
                    )

                    Text(text = "Uploader: $uploader",
                         fontSize = 20.sp,
                         color = FermuxColors.white,
                         overflow = TextOverflow.Ellipsis,
                         modifier = Modifier.padding(start = 4.dp, top = 8.dp, bottom = 8.dp)
                    )

                    Text(
                         text = "Format: $format",
                         fontSize = 20.sp,
                         color = FermuxColors.white,
                         overflow = TextOverflow.Ellipsis,
                         modifier = Modifier.padding(start = 4.dp, top = 6.dp, bottom = 6.dp)

                    )

                    Text(text = "Duration: ${videoTime(duration)}",
                         fontSize = 20.sp,
                         color = FermuxColors.white,
                         overflow = TextOverflow.Ellipsis,
                         modifier = Modifier.padding(start = 4.dp, top = 6.dp, bottom = 6.dp)
                    )

                    Text(text = "Size: ${sizeFormatting(size)}",
                         fontSize = 20.sp,
                         color = FermuxColors.white,
                         overflow = TextOverflow.Ellipsis,
                         modifier = Modifier.padding(start = 4.dp, top = 6.dp, bottom = 6.dp)
                    )

                    Text(
                         text = if (resolution.equals("audio only", ignoreCase = true))
                              resolution else "Resolution: ${resolutionFormatting(resolution)}p",
                         fontSize = 20.sp,
                         color = FermuxColors.white,
                         overflow = TextOverflow.Ellipsis,
                         modifier = Modifier.padding(start = 4.dp, top = 6.dp, bottom = 6.dp)
                    )

                    Text(
                         text = "Extractor: $extractor",
                         fontSize = 20.sp,
                         color = FermuxColors.white,
                         overflow = TextOverflow.Ellipsis,
                         modifier = Modifier.padding(bottom = 7.dp, start = 6.dp, top = 6.dp)
                    )
               }
               Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp),
                    contentAlignment = Alignment.BottomEnd
               ) {
                    FilterButton(
                         modifier = Modifier,
                         image = R.drawable.open_link,
                         onClick = {
                              context.openUrl(url)
                         }
                    )
               }
          }
     }
}



@Preview
@Composable
fun test15() {
     Column(Modifier.fillMaxSize()) {
          InfoModalSheet(
               onDismissRequest = {},
               thumbnail = "/home/Hussain/Downloads/galagcy.webp",
               imageDescription = "",
               title = "Something situation crazy",
               uploader = "PenguinZ0",
               duration = 234564,
               format = "Webm",
               size = 352534,
               resolution = "1920x1080",
               extractor = "Youtube",
               url = "https://www.youtube.com/watch?v=fCK8-2pdtFU&t=5041s",
               uri = "EGKWSNOPRSERFNKOP[eSNJOFGPnjoprSG"
          )
     }
}