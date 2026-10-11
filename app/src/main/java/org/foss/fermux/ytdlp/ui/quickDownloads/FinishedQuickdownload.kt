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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.ui.theme.FermuxColors


@Composable
fun FinalQuickDownload(
     onCancel: () -> Unit
) {

     Surface(
          modifier = Modifier
               .padding(8.dp)
               .height(60.dp)
               .fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          color = FermuxColors.inActiveContainer,
          border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder)
     ) {
          Box(
               modifier = Modifier
                    .padding(6.dp),
               contentAlignment = Alignment.CenterStart
          ) {
               Text(
                    text = "Download Completed",
                    fontSize = 18.sp,
                    fontStyle = FontStyle.Normal,
                    fontFamily = FontFamily.Default,
                    textAlign = TextAlign.Start,
                    color = FermuxColors.skyBlue,

                    )
          }

          Row(
               verticalAlignment = Alignment.CenterVertically,
               modifier = Modifier.fillMaxWidth()
          ) {
               Box(
                    modifier = Modifier
                         .fillMaxWidth()
                         .padding(top = 5.dp, bottom = 5.dp, end = 8.dp),
                    contentAlignment = Alignment.CenterEnd
               ) {
                    CancelButton(
                         shape = RoundedCornerShape(8.dp),
                         onClick = { onCancel() },
                    )
               }
          }
     }
}

@Preview
@Composable
fun test14() {
     FinalQuickDownload(
          onCancel = {}
     )
}