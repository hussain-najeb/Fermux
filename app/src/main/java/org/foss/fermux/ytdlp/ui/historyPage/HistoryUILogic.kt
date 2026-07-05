package org.foss.fermux.ytdlp.ui.historyPage

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.videoTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryCards(entry: JSONHistoryCards) {

        val cardBorder = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(1.5.dp, Color(0xFF17DB6F), RoundedCornerShape(8.dp))
            .border(1.5.dp, Color(0xFF20B161), RoundedCornerShape(8.dp))
            .border(0.8.dp, Color(0xFF20bf6b), RoundedCornerShape(8.dp))

        var expanded by remember { mutableStateOf(false) }
        val rotation by animateFloatAsState(if (expanded) 180f else 0f)

        Column(verticalArrangement = Arrangement.Center) {
            ElevatedCard(
                modifier = cardBorder
                    .fillMaxSize()
            ) {
                Box {
                    AsyncImage(
                        model = entry.thumbnail,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(8.dp))
                    )


                    androidx.compose.animation.AnimatedVisibility(
                    visible = true,
                    enter = slideInHorizontally(
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    ),
                    exit = slideOutHorizontally(
                        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
                    ),
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
                {
                    FilledTonalButton(
                        onClick = { expanded = !expanded },
                        contentPadding = PaddingValues(8.dp),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.5.dp, Color(0xFF20bf6b)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1f2034),
                            contentColor = Color.White
                        ),
                        modifier = Modifier.padding(4.dp)
                    )
                    {
                        Icon(
                            Icons.Default.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.rotate(rotation)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        Text(if (expanded) "Hide details" else "Show details")
                    }
                }
                }

                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeIn(),
                    exit = shrinkVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeOut()
                )
                {
                    Surface(
                        modifier = Modifier
                            .background(Color(0xFF1f2034))
                            .padding(5.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF1f2034))

                        ) {
                            Row {
                                Text(
                                    text = " Title:",
                                    fontSize = 19.sp,
                                    fontFamily = FontFamily.Default,
                                    color = Color(0xFF48AF79),
                                    modifier = Modifier.padding(3.dp)
                                )
                                Text(
                                    text = entry.title,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Default,
                                    modifier = Modifier.padding(3.dp)

                                )
                            }
                            Row {
                                Text(
                                    text = "Duration:",
                                    fontSize = 19.sp,
                                    fontFamily = FontFamily.Default,
                                    color = Color(0xFF546CE8),
                                    modifier = Modifier.padding(3.dp),
                                )
                                Text(
                                    text = videoTime(entry.videoDuration.toInt()),

                                    fontFamily = FontFamily.Default,
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(3.dp)
                                )
                            }

                            val formattedDate = remember(entry.downloadTime) {
                                SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(entry.downloadTime))
                            }
                            Row {
                                Text(
                                    text = "Date:",
                                    fontSize = 19.sp,
                                    fontFamily = FontFamily.Default,
                                    color = Color(0xFFC96726),
                                    modifier = Modifier.padding(3.dp)
                                )
                                Text(
                                    text = formattedDate,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Default,
                                    color = Color.White,
                                    modifier = Modifier.padding(3.dp)
                                )
                            }
                            Row {
                                entry.uploader?.let {
                                    Text(
                                        text = "Uploader:",
                                        fontSize = 19.sp,
                                        fontFamily = FontFamily.Default,
                                        color = Color(0xFFF34545),
                                        modifier = Modifier.padding(3.dp)
                                    )
                                    Text(
                                        text = it,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Default,
                                        color = Color.White,
                                        modifier = Modifier.padding(3.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
