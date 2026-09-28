package org.foss.fermux.ytdlp.ui.historyPage


import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.components.buttons.AppIconButton
import org.foss.fermux.components.buttons.TextWithIconButton
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.videoTime
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SuspiciousIndentation")
@Composable
fun HistoryCards(entry: JSONHistoryCards) {

     var expanded by remember { mutableStateOf(false) }
     val spatialSpec = MaterialTheme.motionScheme
     @Suppress("DEPRECATION") val clipboard = LocalClipboardManager.current

     Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {


               Box(contentAlignment = Alignment.TopStart) {
                    AsyncImage(
                         model = entry.thumbnail,
                         contentDescription = null,
                         contentScale = ContentScale.Crop,
                         modifier = Modifier
                              .aspectRatio(16f / 9f)
                              .clip(RoundedCornerShape(8.dp))
                    )


                    this@Column.AnimatedVisibility(
                         visible = true,
                         enter = slideInHorizontally(
                              animationSpec = spatialSpec.fastSpatialSpec()
                         ),
                         exit = slideOutHorizontally(
                              animationSpec = spatialSpec.fastSpatialSpec()
                         ),
                         modifier = Modifier.align(Alignment.BottomStart)
                    ) {
                         TextWithIconButton(
                              modifier = Modifier
                                   .defaultMinSize(minWidth = 70.dp)
                                   // Note: inside the AnimatedVisibility scope, alignment is handled by outer Box/Column layout
                                   .padding(6.dp),
                              contentPadding = PaddingValues(8.dp),
                              iconRotation = if (expanded) 180f else 0f,
                              icon = Icons.Default.ExpandMore,
                              text = if (expanded) "Hide details" else "Show details",
                              onClick = { expanded = !expanded },
                         )
                    }

                    AppIconButton(
                         icon = Icons.Default.ContentCopy,
                         modifier = Modifier.size(60.dp).padding(6.dp).align(Alignment.BottomEnd),
                         onClick = { clipboard.setText(AnnotatedString(entry.url)) }
                    )

               }


                    Row {
                         Text(
                              text = "Title: ${entry.title}",
                              fontFamily = FontFamily.Default,
                              fontSize = 17.sp,
                              color = Color(0xFF48AF79),
                              modifier = Modifier
                                   .padding(3.dp)
                         )
                    }

                    Spacer(modifier = Modifier.padding(4.dp))
                    HorizontalDivider(
                         thickness = 1.0.dp,
                         color = FermuxColors.fermuxComponents,
                         modifier = Modifier.padding(2.dp)
                    )

                    Row {
                         Text(
                              text = "Duration: ${videoTime(entry.videoDuration.toInt())}",
                              fontFamily = FontFamily.Default,
                              fontSize = 17.sp,
                              color = Color(0xFF546CE8),
                              modifier = Modifier
                                   .padding(3.dp)
                         )
                    }

                    Spacer(modifier = Modifier.padding(4.dp))
                    HorizontalDivider(
                         thickness = 1.0.dp,
                         color = FermuxColors.fermuxComponents,
                         modifier = Modifier.padding(2.dp)
                    )

                    val formattedDate = remember(entry.downloadTime) {
                         SimpleDateFormat(
                              "yyyy-MM-dd HH:mm",
                              Locale.getDefault()
                         ).format(Date(entry.downloadTime))
                    }

                    Row {
                         Text(
                              text = "Date: $formattedDate",
                              fontFamily = FontFamily.Default,
                              fontSize = 17.sp,
                              color = Color(0xFFC96726),
                              modifier = Modifier
                                   .padding(3.dp)
                         )
                    }

                    Spacer(modifier = Modifier.padding(4.dp))
                    HorizontalDivider(
                         thickness = 1.0.dp,
                         color = FermuxColors.fermuxComponents,
                         modifier = Modifier.padding(2.dp)
                    )

                    entry.uploader?.let {
                         Row {
                              Text(
                                   text = "Uploader: ${entry.uploader}",
                                   fontFamily = FontFamily.Default,
                                   fontSize = 17.sp,
                                   color = Color(0xFFF34545),
                                   modifier = Modifier
                                        .padding(3.dp)
                              )
                         }
                    }
               }
          }
