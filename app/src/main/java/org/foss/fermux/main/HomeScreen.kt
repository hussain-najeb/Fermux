package org.foss.fermux.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.foss.fermux.fermuxUIComponents.generalComponents.AppCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.MainScreens

@Composable
fun HomeScreen(navigationController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FermuxColors.fermuxBackground)
            .systemBarsPadding()
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val screens = listOf(
                MainScreens.Terminal,
                MainScreens.Downloader,
                MainScreens.Converter,
                MainScreens.Settings,
            )
            screens.forEach { screen ->
                AppCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.25f)
                        .padding(4.dp),
                    pressable = true,
                    onClick = { navigationController.navigate(screen.route) }
                ) {
                    screen.descriptor?.let {
                        Text(
                            text = it,
                            color = Color.White,
                            fontSize = 22.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                }
            }
        }
    }
}