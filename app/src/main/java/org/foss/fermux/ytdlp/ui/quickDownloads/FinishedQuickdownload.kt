package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.R
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun FinalQuickDownload() {

     Surface(modifier = Modifier
          .padding(8.dp)
          .height(60.dp)
          .fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          color = FermuxColors.inActiveContainer,
          border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder)
     ) {
          Box(modifier = Modifier
               .padding(10.dp)
               .fillMaxWidth(),
               contentAlignment = Alignment.CenterStart
          ) {
               Text(
                    text = "Download Complete!",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.Start,
                    color = FermuxColors.skyBright,
                    modifier = Modifier.padding(start = 10.dp)
               )
          }
          Box(modifier = Modifier
               .padding(10.dp)
               .fillMaxWidth()
          ) {
               Icon(painter = painterResource(id = R.drawable.cancel_buttons), contentDescription = null)
          }
     }
}