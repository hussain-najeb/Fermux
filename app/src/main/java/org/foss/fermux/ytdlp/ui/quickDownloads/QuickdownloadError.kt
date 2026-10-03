package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.components.buttons.LogButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.utils.Miscellaneous

@Composable
fun QuickdownloadError(
     onCancel: () -> Unit,
     navController: NavController
) {

     Surface(modifier = Modifier
          .padding(8.dp)
          .height(60.dp)
          .fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          color = FermuxColors.fermuxErrorCardColor,
          border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder)
     ) {
          Box(modifier = Modifier
               .padding(6.dp)
               .fillMaxWidth(),
               contentAlignment = Alignment.CenterStart
          ) {
               Text(
                    text = "Error while downloading",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.Start,
                    color = FermuxColors.white,
                    modifier = Modifier.padding(start = 2.dp)
               )
          }
          Box(modifier = Modifier
               .padding(3.dp)
               .fillMaxWidth(),
               contentAlignment = Alignment.CenterEnd
          ) {
               Row(modifier = Modifier.padding(end = 5.dp)) {
                    LogButton(
                         onClick = { navController.navigate(Miscellaneous.DownloaderLogs.route) },
                         shape = RoundedCornerShape(8.dp),
                         modifier = Modifier.padding(end = 4.dp)
                    )
                    CancelButton(
                         modifier = Modifier.padding(),
                         shape = RoundedCornerShape(8.dp),
                         onClick = { onCancel() }
                    )
               }
          }
     }
}