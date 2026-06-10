package org.foss.fermux.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navigationController: NavHostController) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {

        // Terminal — big tile
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFebedef))
                .clickable { navigationController.navigate("terminal") }
        ) {
            Text("terminal", modifier = Modifier
                .padding(start = 12.dp)
                .padding(bottom = 70.dp, top = 25.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Downloader + Settings side by side
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(180.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFebedef))
                    .clickable { navigationController.navigate("downloader") }
            ) {
                Text("Downloader", modifier = Modifier
                    .padding(start = 12.dp)
                    .padding(bottom = 70.dp, top = 19.dp)

                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Box(
                modifier = Modifier
                    .width(170.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFebedef))
                    .clickable { navigationController.navigate("converter") }
            ) {
                Text("converter", modifier = Modifier
                    .padding(start = 12.dp)
                    .padding(bottom = 70.dp, top = 19.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFebedef))
                .clickable { navigationController.navigate("settings") }
        ) {
            Text("settings", modifier = Modifier
                .padding(start = 12.dp)
                .padding(bottom = 70.dp, top = 13.dp)
            )
        }

        }
    }
