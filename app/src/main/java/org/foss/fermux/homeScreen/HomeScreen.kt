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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.foss.fermux.Screen


@OptIn(ExperimentalGridApi::class)
@Composable
fun HomeScreen(navigationController: NavHostController) {

    // TODO. Have the GRID composable here to revamp this shitty page.

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
        contentAlignment = Alignment.Center
    ) {
        Grid(
            config = {
                repeat(2) { column(1.fr) }
                repeat(2) { row(1.fr) }
                rowGap(10.dp)
                columnGap(20.dp)
            }
        ) {
            val cardModifier = Modifier.fillMaxSize()
            val boxModifier = Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFebedef))



            ElevatedCard(modifier = cardModifier) { Box(modifier = boxModifier.clickable { navigationController.navigate(Screen.Terminal) })
            {
                Text("Terminal", modifier = Modifier
                    .padding(start = 12.dp)
                    .padding(bottom = 70.dp, top = 25.dp)
                    )
                }
            }

            ElevatedCard(modifier = cardModifier) { Box(modifier = boxModifier)  }






        }
    }
}