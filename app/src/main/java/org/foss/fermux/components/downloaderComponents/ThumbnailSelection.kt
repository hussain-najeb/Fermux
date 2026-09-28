package org.foss.fermux.components.downloaderComponents

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.downloader.ThumbnailFormat

@Composable
fun ThumbnailSelector(
     expanded: Boolean,
) {

     val downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel()

     val thumbnailOptions = listOf(
          ThumbnailFormat.Off to "off",
          ThumbnailFormat.Jpeg to "jpeg",
          ThumbnailFormat.Png to "png",
          ThumbnailFormat.WebP to "webp",
     )


     val thumbnailFormats by downloaderSettingsViewModel.thumbnailFormat.collectAsStateWithLifecycle()
     val thumbnail by downloaderSettingsViewModel.embedThumbnail.collectAsStateWithLifecycle()

     AnimatedVisibility(
          visible = expanded,
          enter = expandVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeIn(initialAlpha = 0.2f),
          exit = shrinkVertically(animationSpec = MaterialTheme.motionScheme.fastEffectsSpec()) + fadeOut(targetAlpha = 0.1f)
     ) {
          Surface(
               modifier = Modifier
                    .wrapContentSize()
                    .padding(start = 8.dp, end = 8.dp),
               color = FermuxColors.fermuxComponents,
               shape = RoundedCornerShape(8.dp),
               border = BorderStroke(1.dp, color = FermuxColors.fermuxHelperBorder)
          ) {
               SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                         .padding(7.dp)
                         .fillMaxWidth()
               ) {
                    thumbnailOptions.forEachIndexed { index, (format, label) ->
                         SegmentedButton(
                              selected = thumbnailFormats == format,
                              onClick = { downloaderSettingsViewModel.setThumbnailFormat(format) },
                              shape = SegmentedButtonDefaults.itemShape(
                                   index = index,
                                   count = thumbnailOptions.size,
                                   baseShape = RoundedCornerShape(8.dp)
                              ),
                              enabled = thumbnail,
                              colors = SegmentedButtonDefaults.colors(
                                   activeContainerColor = FermuxColors.activeContainer,
                                   activeContentColor = FermuxColors.activeContent,
                                   inactiveContainerColor = FermuxColors.inActiveContainer,
                                   inactiveContentColor = FermuxColors.inActiveContent,
                                   activeBorderColor = FermuxColors.fermuxSecondaryBorder,
                                   inactiveBorderColor = FermuxColors.fermuxGenericBorder,
                                   disabledActiveContainerColor =
                                        FermuxColors.activeContainer.copy(alpha = 0.4f),
                                   disabledActiveContentColor =
                                        FermuxColors.activeContent.copy(alpha = 0.4f),
                                   disabledActiveBorderColor =
                                        FermuxColors.fermuxSecondaryBorder.copy(alpha = 0.4f),
                                   disabledInactiveContainerColor =
                                        FermuxColors.inActiveContainer.copy(alpha = 0.4f),
                                   disabledInactiveContentColor =
                                        FermuxColors.inActiveContent.copy(alpha = 0.4f),
                                   disabledInactiveBorderColor =
                                        FermuxColors.fermuxGenericBorder.copy(alpha = 0.4f)
                              )
                         ) {
                              Text(label)
                         }
                    }
               }
          }
     }
}