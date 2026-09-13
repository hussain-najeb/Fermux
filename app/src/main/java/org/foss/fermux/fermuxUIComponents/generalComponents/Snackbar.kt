package org.foss.fermux.fermuxUIComponents.generalComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun FermuxSnackBar(
     hostState: SnackbarHostState
) {
     SnackbarHost(hostState = hostState) { data ->
          val dismissBehavior = rememberSwipeToDismissBoxState(
               positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
          )

          SwipeToDismissBox(
               state = dismissBehavior,
               backgroundContent = {},
               onDismiss = {
                    data.dismiss()
               }
          ) {
               Snackbar(
                    modifier = Modifier
                         .padding(12.dp)
                         .border(1.dp, FermuxColors.fermuxGenericBorder, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    containerColor = FermuxColors.something3,
                    contentColor = FermuxColors.fermuxWhiteColor,
               ) {
                    Text(
                         text = data.visuals.message,
                         fontSize = 14.sp,
                         fontFamily = FontFamily.Default
                    )

               }
          }
     }
}