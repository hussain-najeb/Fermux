package org.foss.fermux.fermuxUIComponents.generalComponents


import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SectionDivider(
     modifier: Modifier = Modifier,
) {
     HorizontalDivider(
          modifier = modifier.padding(horizontal = 10.dp),
          thickness = 1.dp,
          color = FermuxColors.fermuxComponents
     )
}
