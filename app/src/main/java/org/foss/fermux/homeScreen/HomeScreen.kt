package org.foss.fermux.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.foss.fermux.Screen


@OptIn(ExperimentalGridApi::class)
@Composable
fun HomeScreen(navigationController: NavHostController) {



    val cardModifier = Modifier.fillMaxSize()
    val boxModifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp))



    // TODO. Have the GRID composable here to revamp this shitty page.

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(13.dp),

        contentAlignment = Alignment.Center

    ) {
        Grid(
            config = {
                repeat(2) { column(1.fr) }
                row(1.5.fr)
                row(0.5.fr)
                row(1.5.fr)

                rowGap(15.dp)
                columnGap(15.dp)
            }
        ) {

            ElevatedCard(modifier = cardModifier.gridItem(columnSpan = 2)) { Box(modifier = boxModifier
                .clickable { navigationController.navigate(Screen.Terminal.route) })
            {
                Text("Terminal", modifier = Modifier
                    .padding(start = 12.dp, bottom = 70.dp, top = 25.dp),
                    fontStyle  = FontStyle.Italic,
                    fontFamily = FontFamily.Default,
                    fontSize = 20.sp,
                )
                }
            }

            ElevatedCard(modifier = cardModifier.gridItem(columnSpan = 1)) { Box(modifier = boxModifier
                .clickable { navigationController.navigate(Screen.Downloader.route) })

            {
                Text("Download", modifier = Modifier
                    .padding(start = 12.dp, bottom = 70.dp, top = 25.dp),
                    fontStyle  = FontStyle.Italic,
                    fontFamily = FontFamily.Default,
                    fontSize = 20.sp
                    )
                }
            }

            ElevatedCard(modifier = cardModifier.gridItem(columnSpan = 1)) { Box(modifier = boxModifier
                    .clickable { navigationController.navigate(Screen.Converter.route)} )
                {
                    Text("Converter", modifier = Modifier
                    .padding(start = 12.dp, bottom = 70.dp, top = 25.dp),
                        fontStyle  = FontStyle.Italic,
                        fontFamily = FontFamily.Default,
                        fontSize = 20.sp
        )
                }
            }
            ElevatedCard(modifier = cardModifier.gridItem(columnSpan = 2)) { Box(modifier = boxModifier
                .clickable { navigationController.navigate(Screen.Settings.route) })
            {
                    Text("Settings", modifier = Modifier
                    .padding(start = 12.dp, bottom = 70.dp, top = 25.dp),
                    fontStyle  = FontStyle.Italic,
                    fontFamily = FontFamily.Default,
                    fontSize = 20.sp
                    )
                 }
            }
        }
    }
}