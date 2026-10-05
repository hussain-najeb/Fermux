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
          enter = expandVertically(
               expandFrom = Alignment.Top,
               animationSpec = tween(150)
          ) + fadeIn(),
          exit = shrinkVertically(
               shrinkTowards = Alignment.Top,
               animationSpec = tween(100)
          ) + fadeOut()
     ) {
          Surface(
               modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
               color = FermuxColors.darkBlue,
               shape = RoundedCornerShape(8.dp),
               border = BorderStroke(1.dp, FermuxColors.mutedBlue)
          ) {
               Row(
                    modifier = Modifier
                         .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
               ) {
                    Box(
                         modifier = Modifier.wrapContentSize(),
                         contentAlignment = Alignment.CenterStart
                    ) {
                         SmallActionButton(
                              modifier = Modifier
                                   .padding(3.dp),
                              onClick = onDelete,
                              image = R.drawable.trash
                         )
                    }
                    Box(
                         modifier = Modifier.wrapContentSize(),
                         contentAlignment = Alignment.Center
                    ) {
                         SmallActionButton(
                              modifier = Modifier
                                   .padding(3.dp),
                              onClick = onMoreInfo,
                              image = R.drawable.more_info
                         )
                    }
                    Box(
                         modifier = Modifier.wrapContentSize(),
                         contentAlignment = Alignment.CenterEnd
                    ) {
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