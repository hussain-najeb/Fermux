package org.foss.fermux.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.foss.fermux.ui.theme.FermuxCard
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun HomeScreen(navigationController: NavHostController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FermuxColors.fermuxBackground)
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            val screens = listOf(
                Screen.Terminal,
                Screen.Downloader,
                Screen.Converter,
                Screen.Settings,
            )

            screens.forEach { screen ->
                FermuxCard(
                    cardPadding = 5.dp,
                    modifier = Modifier.fillMaxWidth().weight(0.25f),
                    clickable = { navigationController.navigate(screen.route) }
                ) {
                    screen.descriptor?.let { Text(text = it, color =
                        Color.White ,
                        fontSize = 20.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
        }
    }
}