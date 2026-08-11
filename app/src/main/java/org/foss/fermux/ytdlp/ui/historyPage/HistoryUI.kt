package org.foss.fermux.ytdlp.ui.historyPage


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.fermuxUIComponents.generalComponents.AppCard
import org.foss.fermux.fermuxUIComponents.buttons.AppIconButton
import org.foss.fermux.fermuxUIComponents.generalComponents.AppSurface
import org.foss.fermux.fermuxUIComponents.buttons.TextWithIconButton
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.videoTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryCards(entry: JSONHistoryCards) {

    var expanded by remember { mutableStateOf(false) }
    @Suppress("DEPRECATION") val clipboard = LocalClipboardManager.current

        Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            AppCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
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
                            animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                        ),
                        exit = slideOutHorizontally(
                            animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
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

                AppSurface(
                    expanded = expanded,
                    padding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row {
                        Text(text = "Title: ${entry.title}",
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
                        Text(text = "Duration: ${videoTime(entry.videoDuration.toInt())}",
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
                        SimpleDateFormat("yyyy-MM-dd HH:mm",
                            Locale.getDefault()).format(Date(entry.downloadTime))
                    }

                    Row {
                        Text(text = "Date: $formattedDate",
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
                            Text(text = "Uploader: ${entry.uploader}",
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
        }
    }
