package org.foss.fermux.ytdlp.ui.historyPage


import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.foss.fermux.storage.JSONHistoryCards
import org.foss.fermux.ui.theme.FermuxButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.FermuxSurface
import org.foss.fermux.ytdlp.ui.ytdlpMainScreen.videoTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryCards(entry: JSONHistoryCards) {

        var expanded by remember { mutableStateOf(false) }

        Column(verticalArrangement = Arrangement.Center) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxSize()
                    .border(1.5.dp, FermuxColors.fermuxPrimaryBorder, RoundedCornerShape(8.dp))
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

                        FermuxButton(
                            isExpanded = expanded,
                            rotation = 180f,
                            buttonSize = 20.dp,
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                            icon = Icons.Default.ExpandMore,
                            text = if (expanded) "Hide details" else "Show details",
                            clickable = {expanded = !expanded},
                        )
                    }
                }

                FermuxSurface(
                    expanded = expanded,
                    padding = PaddingValues(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row {
                        Text(text = "Title:", fontFamily = FontFamily.Default, fontSize = 19.sp, color = Color(0xFF48AF79), modifier = Modifier.padding(3.dp))
                        Text(text = entry.title, fontFamily = FontFamily.Default, fontSize = 16.sp, modifier = Modifier.padding(3.dp))
                    }
                    Row {
                        Text(text = "Duration:", fontFamily = FontFamily.Default, fontSize = 19.sp, color = Color(0xFF546CE8), modifier = Modifier.padding(3.dp))
                        Text(text = videoTime(entry.videoDuration.toInt()), fontFamily = FontFamily.Default, fontSize = 16.sp, color = Color.White, modifier = Modifier.padding(3.dp))
                    }
                    val formattedDate = remember(entry.downloadTime) {
                        SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(entry.downloadTime))
                    }
                    Row {
                        Text(text = "Date:", fontFamily = FontFamily.Default, fontSize = 19.sp, color = Color(0xFFC96726), modifier = Modifier.padding(3.dp))
                        Text(text = formattedDate, fontFamily = FontFamily.Default, fontSize = 16.sp, color = Color.White, modifier = Modifier.padding(3.dp))
                    }
                    entry.uploader?.let {
                        Row {
                            Text(text = "Uploader:", fontFamily = FontFamily.Default, fontSize = 19.sp, color = Color(0xFFF34545), modifier = Modifier.padding(3.dp))
                            Text(text = it, fontFamily = FontFamily.Default, fontSize = 16.sp, color = Color.White, modifier = Modifier.padding(3.dp))
                        }
                    }
                }
            }
        }
    }