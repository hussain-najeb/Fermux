package org.foss.fermux.ytdlp.ui.ytdlpMainScreen.downloaderStates

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import org.foss.fermux.fermuxUIComponents.downloaderComponents.FermuxDownloadDescription
import org.foss.fermux.fermuxUIComponents.downloaderComponents.FinishedDownloadCard
import org.foss.fermux.fermuxUIComponents.buttons.CancelButton
import org.foss.fermux.fermuxUIComponents.buttons.AppIconButton
import org.foss.fermux.main.Miscellaneous
import org.foss.fermux.settings.logic.SettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.DownloadMetadata


private enum class ProgressState { InProgress, Done }

@Composable
fun FinishedCard (
    metadata: DownloadMetadata,
    progress: Float? = null,
    onCancel: () -> Unit,
    navController: NavController,
    @SuppressLint("ContextCastToActivity") settingsViewModel: SettingsViewModel = viewModel(viewModelStoreOwner = LocalContext.current as ComponentActivity)

) {

     val showYtdlpDetails by settingsViewModel.ytdlpDetails.collectAsStateWithLifecycle()

     val downloadState = progress?.let {
          if (it >= 100f) ProgressState.Done else ProgressState.InProgress
     }

     // TEST LINK. https://www.youtube.com/watch?v=VD6xJq8NguY&pp=0gcJCcQLAYcqIYzv

     Column(
          modifier = Modifier
               .fillMaxWidth()
     ) {
          FinishedDownloadCard {

               Box(modifier = Modifier.clip(RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp ))) {
                    AsyncImage(
                         model = metadata.thumbnail,
                         contentDescription = null,
                         contentScale = ContentScale.Crop,

                         modifier = Modifier
                              .fillMaxWidth()
                              .aspectRatio(16f / 9f)
                              .background(FermuxColors.fermuxSurface)
                    )
                    progress?.let {
                         when (downloadState) {
    ProgressState.InProgress -> Column(
        modifier = Modifier
            .align(Alignment.Center)
            .size(50.dp)
            .background(
                color = FermuxColors.fermuxComponents.copy(alpha = 0.48f),
                shape = RoundedCornerShape(10.dp) 
            )
    ) {
        CircularWavyProgressIndicator(
            progress = { progress / 100f },
            color = FermuxColors.fermuxGenericBorder,
            trackColor = FermuxColors.fermuxTertiaryBorder,
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
     }
     
    ProgressState.Done -> Column(
        modifier = Modifier
            .align(Alignment.Center)
            .background(
                color = FermuxColors.fermuxComponents.copy(alpha = 0.48f),
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Icon(
            Icons.Default.Check,
            contentDescription = "Download Complete",
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
        )
    }

    null -> Unit
}
                    }
                    CancelButton(
                         modifier = Modifier
                              .align(alignment = Alignment.TopStart).padding(10.dp),
                         onClick = { onCancel() }
                    )
                    if (showYtdlpDetails) {
                         AppIconButton(
                              modifier = Modifier
                                   .padding(1.dp)
                                   .align(alignment = Alignment.BottomStart),
                              icon = Icons.Default.ExpandMore,
                              contentPadding = PaddingValues(8.dp),
                              onClick = { navController.navigate(Miscellaneous.DownloaderLogs.route) }
                         )
                    }
                    
               }
               FermuxDownloadDescription {
                    Column(modifier = Modifier.fillMaxSize().background(FermuxColors.fermuxComponents)) {
                         Text(
                              text = metadata.title,
                              fontFamily = FontFamily.Default,
                              fontSize = 18.sp,
                              fontWeight = FontWeight.W400,
                              color = Color.White,
                              modifier = Modifier
                                   .padding(7.dp)
                         )
                         metadata.uploader?.let {
                              Text(
                                   text = it,
                                   fontFamily = FontFamily.Default,
                                   fontSize = 13.sp,
                                   color = FermuxColors.fermuxTextColorBackground,
                                   modifier = Modifier
                                        .padding(7.dp)
                              )
                         }
                    }
               }

          }
     }
}