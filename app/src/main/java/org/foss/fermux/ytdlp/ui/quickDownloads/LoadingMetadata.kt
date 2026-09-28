package org.foss.fermux.ytdlp.ui.quickDownloads

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun QuickDownloadsMetadata() {
     Surface(modifier = Modifier
          .padding(8.dp)
          .height(60.dp)
          .fillMaxWidth(),
          shape = RoundedCornerShape(8.dp),
          color = FermuxColors.inActiveContainer,
          border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder)
     ) {
          LoadingIndicator(color = FermuxColors.fermuxWhiteColor)
     }
}

@Preview
@Composable
fun test7() {
     QuickDownloadsMetadata()
}