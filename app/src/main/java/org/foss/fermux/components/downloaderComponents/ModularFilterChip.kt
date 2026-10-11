package org.foss.fermux.components.downloaderComponents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun <T> ModularFilterChip(
     enabled: Boolean = true,
     optionList: List<Pair<T, String>>,
     selectedOptions: Set<T>,
     onSelected: (Set<T>) -> Unit
) {

     FlowRow(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp),
          verticalArrangement = Arrangement.spacedBy(2.dp)
     ) {
          optionList.forEach { (value, name) ->
               val selected = value in selectedOptions
               FilterChip(
                    selected = selected,
                    enabled = enabled,
                    onClick = {
                         onSelected(
                              if (selected) selectedOptions - value
                              else selectedOptions + value
                         )
                    },
                    label = { Text(name) },
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
                         selected = selected,
                         borderColor = FermuxColors.fermuxHelperBorder,
                         selectedBorderColor = FermuxColors.fermuxSecondaryBorder
                    ),
                    leadingIcon = if (selected) {
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