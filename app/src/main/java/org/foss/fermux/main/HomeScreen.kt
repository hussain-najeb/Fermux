package org.foss.fermux.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            val screens = listOf(
                Screen.Home,
                Screen.Settings,
                Screen.Downloader,
                Screen.Converter,
                Screen.Terminal,
                Screen.AudioFormatSheet,
                Screen.VideoFormatSheet,
                Screen.ImageFormatSheet
            )

            screens.dropLast(3).forEach { screen ->
                FermuxCard(
                    cardPadding = 5.dp,
                    clickable = { navigationController.navigate(screen.route) }
                ) {
                    screen.descriptor?.let { Text(text = it) }
                }
            }


        }
    }
}