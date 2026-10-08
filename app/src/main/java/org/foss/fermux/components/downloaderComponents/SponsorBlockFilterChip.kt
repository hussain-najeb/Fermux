package org.foss.fermux.components.downloaderComponents

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SponsorBlockChoices(
     downloaderSettingsViewModel: DownloaderSettingsViewModel
) {
     val sponsorBlockCategories by downloaderSettingsViewModel.sponsorBlockCategories.collectAsStateWithLifecycle()
     val sponsorBlock by downloaderSettingsViewModel.sponsorBlock.collectAsStateWithLifecycle()

     val flags = listOf(
          "sponsor" to "Skip Sponsor",
          "selfpromo" to "Self Promo",
          "intro" to "Intro",
          "outro" to "Outro",
          "preview" to "Preview/Recap"
     )

          FlowRow(
               modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
               horizontalArrangement = Arrangement.spacedBy(6.dp),
               verticalArrangement = Arrangement.spacedBy(2.dp)
          ) {
               flags.forEach { (flag, labeledFlag) ->
                    val pickedFlags = flag in sponsorBlockCategories
                    FilterChip(
                         selected = pickedFlags,
                         enabled = sponsorBlock,
                         onClick = {
                              val updatedFlags = if (pickedFlags) sponsorBlockCategories - flag
                              else sponsorBlockCategories + flag
                              downloaderSettingsViewModel.setSponsorBlockCategories(updatedFlags)
                         },
                         label = { Text(labeledFlag) },
                         colors = FilterChipDefaults.filterChipColors(
                              containerColor = FermuxColors.darkPurple,
                              labelColor = FermuxColors.white,
                              iconColor = FermuxColors.white,
                              selectedContainerColor = FermuxColors.skyBlue,
                              selectedLabelColor = FermuxColors.downriver,
                              selectedLeadingIconColor = FermuxColors.downriver
                         ),
                         border = FilterChipDefaults.filterChipBorder(
                              enabled = true,
                              selected = pickedFlags,
                              borderColor = FermuxColors.fermuxHelperBorder,
                              selectedBorderColor = FermuxColors.fermuxSecondaryBorder
                         ),
                         leadingIcon = if (pickedFlags) {
                              {
                                   Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "Selected",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                   )
                              }
                         } else {
                              null
                         }
                    )
               }
          }
}