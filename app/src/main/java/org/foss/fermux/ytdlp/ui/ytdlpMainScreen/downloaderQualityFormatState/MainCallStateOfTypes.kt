package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderQualityFormatState


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.fermuxUIComponents.downloaderComponents.DownloaderCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.DownloaderViewModel

@Composable
fun QualitySheet(downloaderViewModel: DownloaderViewModel) {

    Card(
          modifier = Modifier
               .padding(16.dp)
               .wrapContentSize(),
          shape = RoundedCornerShape(8.dp),
          colors = CardDefaults.cardColors(
               containerColor = FermuxColors.fermuxSurface
          ),
          border = BorderStroke(1.dp, FermuxColors.fermuxWhiteColor)
    ) {
            QualityState(downloaderViewModel) 
    }
}