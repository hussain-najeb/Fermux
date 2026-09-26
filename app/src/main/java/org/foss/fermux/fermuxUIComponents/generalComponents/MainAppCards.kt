package org.foss.fermux.fermuxUIComponents.generalComponents

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.MainScreens

@Composable
fun MainAppCard(
     modifier: Modifier,
     navController: NavController,
     title: String,
     description: String,
     image: Int,
     route: MainScreens
) {
     Surface( modifier = Modifier
          .fillMaxWidth()
          .height(150.dp),
          color = FermuxColors.fermuxInActiveButton,
          contentColor = FermuxColors.fermuxActiveButton,

     ) {


     }

}