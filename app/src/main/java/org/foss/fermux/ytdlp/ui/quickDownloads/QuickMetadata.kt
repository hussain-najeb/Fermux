package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LoadingIndicator
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
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun QuickDownloadsMetadata(
     progress: Float? = null
) {
     Surface(modifier = Modifier
          .padding(8.dp)
          .height(60.dp)
          .fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          color = FermuxColors.inActiveContainer,
          border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder)
     ) {

          Text(
               text = "Fetching video info...",
               fontSize = 18.sp,
               fontStyle = FontStyle.Normal,
               fontFamily = FontFamily.Default,
               textAlign = TextAlign.Start,
               color = FermuxColors.something2,
               modifier = Modifier.padding(top = 12.dp, start = 10.dp)
          )

          Box(
               modifier = Modifier
                    .padding(end = 15.dp)
                    .fillMaxSize(),
               contentAlignment = Alignment.CenterEnd
          ) {
               if (progress != null) {
                    if (progress > -1f)
                         LoadingIndicator(
                              modifier = Modifier.size(45.dp),
                              color = FermuxColors.something2
                         )
               }
          }
     }
}

@Preview
@Composable
fun test7() {
     QuickDownloadsMetadata()
}