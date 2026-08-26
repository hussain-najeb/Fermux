package org.foss.fermux.fermuxUIComponents.downloaderComponents

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SponsorBlockChoices(
	expanded: Boolean,
    downloaderSettingsViewModel: DownloaderSettingsViewModel
	) {

	val sponsorBlock by downloaderSettingsViewModel.sponsorBlockCategories.collectAsStateWithLifecycle()

	val flags = listOf(
          "sponsor" to "Skipping Sponsor",
          "selfpromo" to "Self Promotion",
          "intro" to "Intro",
          "outro" to "Outro",
          "preview" to "Preview/Recap"
        )

	AnimatedVisibility(
          visible = expanded,
          enter = slideInVertically(animationSpec = tween(200)) + fadeIn(initialAlpha = 0.2f),
          exit = slideOutVertically(animationSpec = tween(250)) + fadeOut(targetAlpha = 0.1f)
     ) {
          Surface(
               modifier = Modifier
               .wrapContentSize()
               .padding(start = 8.dp, end = 8.dp),
               color = FermuxColors.fermuxComponents,
               shape = RoundedCornerShape(8.dp),
               border = BorderStroke(1.dp, color = FermuxColors.fermuxHelperBorder)
          ) {

          	FlowRow(modifier = Modifier
          		.fillMaxWidth()
          		.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
          		) {
          		flags.forEach { (flag, labeledFlag) -> 
          			val pickedFlags = flag in sponsorBlock
          			FilterChip(
                        selected = pickedFlags,
                        onClick = {
                        	val updatedFlags = if (pickedFlags) sponsorBlock - flag
                        	else sponsorBlock + flag
                        	downloaderSettingsViewModel.setSponsorBlockCategories(updatedFlags)
                        },
                        label = { Text(labeledFlag) },
    					colors = FilterChipDefaults.filterChipColors(
					        containerColor = FermuxColors.inActiveContainer,
					        labelColor = FermuxColors.inActiveContent,
					        iconColor = FermuxColors.inActiveContent,
					        selectedContainerColor = FermuxColors.activeContainer,
					        selectedLabelColor = FermuxColors.activeContent,
					        selectedLeadingIconColor = FermuxColors.activeContent
					    ),
					    border = FilterChipDefaults.filterChipBorder(
					        enabled = true,
					        selected = pickedFlags,
					        borderColor = FermuxColors.fermuxGenericBorder,
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
	}
}