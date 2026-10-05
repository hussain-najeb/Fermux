package org.foss.fermux.ytdlp.ui.history


import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.components.buttons.SmallActionButton
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun HistoryActions(
     expanded: Boolean,
     onDelete: () -> Unit,
     onMoreInfo: () -> Unit,
     onCopyUrl: () -> Unit
) {
     AnimatedVisibility(
          visible = expanded,
          enter = expandHorizontally(animationSpec = tween(250)) + fadeIn(),
          exit = shrinkHorizontally(animationSpec = tween(200)) + fadeOut()
     ) {
          Surface(
               modifier = Modifier
                    .padding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)
                    .fillMaxWidth(),
               color = FermuxColors.darkBlue,
               shape = RoundedCornerShape(8.dp),
               border = BorderStroke(1.dp, FermuxColors.mutedBlue)
          ) {
               Row(
                    modifier = Modifier
                         .fillMaxWidth()
                         .padding(4.dp)
                    ,
                    horizontalArrangement = Arrangement.Center
               ) {
                    Box(contentAlignment = Alignment.CenterStart) {
                         SmallActionButton(
                              modifier = Modifier
                                   .padding(3.dp),
                              onClick = onDelete,
                              image = R.drawable.trash
                         )
                    }
                    Box(contentAlignment = Alignment.Center) {
                         SmallActionButton(
                              modifier = Modifier
                                   .padding(3.dp),
                              onClick = onMoreInfo,
                              image = R.drawable.more_info
                         )
                    }
                    Box(contentAlignment = Alignment.CenterEnd) {
                         SmallActionButton(
                              modifier = Modifier
                                   .padding(3.dp),
                              onClick = onCopyUrl,
                              image = R.drawable.copy
                         )
                    }
               }
          }
     }
}