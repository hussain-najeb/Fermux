package org.foss.fermux.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController


@OptIn(ExperimentalGridApi::class)
@Composable
fun HomeScreen(navigationController: NavHostController) {

    // TODO. make this livelier with color.

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF181825))
            .padding(13.dp),

        contentAlignment = Alignment.Center

    ) {
        Grid(
            config = {
                repeat(2) { column(1.fr) }
                row(1.5.fr)
                row(0.5.fr)
                row(1.5.fr)

                rowGap(12.dp)
                columnGap(12.dp)
            }
        )

        {

            GridCards(
                2,
                Screen.Terminal,
                "Terminal",
                FontStyle.Italic,
                FontFamily.Default,
                FontWeight.Bold,
                20.sp,
                Color.White,
                navigationController
            )

            GridCards(
                1,
                Screen.Downloader,
                "Downloader",
                FontStyle.Italic,
                FontFamily.Default,
                FontWeight.Bold,
                20.sp,
                Color.White,
                navigationController
            )

            GridCards(
                1,
                Screen.Converter,
                "Converter",
                FontStyle.Italic,
                FontFamily.Default,
                FontWeight.Bold,
                20.sp,
                Color.White,
                navigationController
            )

            GridCards(
                2,
                Screen.Settings,
                "Settings",
                FontStyle.Italic,
                FontFamily.Default,
                FontWeight.Bold,
                20.sp,
                Color.White,
                navigationController,
            )
        }
    }
}


    @OptIn(ExperimentalGridApi::class)
    @Composable
    fun GridScope.GridCards(
        columnWidth: Int,
        screen: Screen,
        title: String,
        style: FontStyle,
        family: FontFamily,
        fontWeight: FontWeight,
        fontSize: TextUnit,
        color: Color,
        navigationController: NavHostController,
        onClick: (() -> Unit)? = null

        ) {
        val cardModifier = Modifier
            .fillMaxSize()
            .border(1.5.dp, Color(0xFF17DB6F), RoundedCornerShape(8.dp))
            .border(1.5.dp, Color(0xFF20B161), RoundedCornerShape(8.dp))
            .border(0.8.dp, Color(0xFF20bf6b), RoundedCornerShape(8.dp))

        val boxModifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF1f2034))

        ElevatedCard(modifier = cardModifier.gridItem(columnSpan = columnWidth)) {
            Box(
                modifier = boxModifier
                    .clickable {
                        if (onClick != null) {
                            onClick()
                        } else { navigationController.navigate(screen.route)}
                    }
                )
            {
                Text(
                    title, modifier = Modifier
                        .padding(start = 12.dp, bottom = 70.dp, top = 25.dp),
                    fontStyle = style,
                    fontFamily = family,
                    fontWeight = fontWeight,
                    color = color,
                    fontSize = fontSize
                )
            }
        }
    }