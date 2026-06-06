package org.foss.fermux.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color


@Composable
fun HomeScreen(navigationController: NavHostController) {


    Column {

    Box(modifier = Modifier
        .size(100.dp)
        .clip(RoundedCornerShape(10.dp))
        .background
            (Color(0xFFebedef))
        .clickable { navigationController.navigate("terminal") }

    ) {
        Text("terminal")
    }

    Row {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFebedef))
                .clickable { navigationController.navigate("downloader") }
        ) {
            Text("Downloader")
        }

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFebedef))
                .clickable { navigationController.navigate("settings") }

        ) {
            Text("Settings")
        }
    }
        Box(modifier = Modifier
            .size(100.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFebedef))
            .clickable { navigationController.navigate("converter") }
        ) {
            Text("converter")
        }
}
}