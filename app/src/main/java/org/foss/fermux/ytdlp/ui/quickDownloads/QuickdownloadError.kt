package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.foss.fermux.components.buttons.LogButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.Miscellaneous

@Composable
fun QuickdownloadError(navController: NavController) {

     Surface(modifier = Modifier
          .padding(8.dp)
          .height(60.dp)
          .fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          color = FermuxColors.fermuxErrorCardColor,
          border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder)
     ) {
          Box(modifier = Modifier
               .padding(10.dp)
               .fillMaxWidth(),
               contentAlignment = Alignment.CenterStart
          ) {
               Text(
                    text = "Press the button to see the logs",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.Start,
                    color = FermuxColors.fermuxWhiteColor,
                    modifier = Modifier.padding(start = 10.dp)
               )
          }
          Box(modifier = Modifier
               .padding(3.dp)
               .fillMaxWidth(),
               contentAlignment = Alignment.CenterEnd
          ) {
               LogButton(
                    onClick = { navController.navigate(Miscellaneous.DownloaderLogs.route) },
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(end = 5.dp)
               )
          }
     }
}


@Preview
@Composable
fun test10() {
     val navController = rememberNavController()
     Column(modifier = Modifier.fillMaxSize().padding(5.dp).background(FermuxColors.fermuxBackground)) {
          QuickdownloadError(
               navController = navController
          )
     }
}