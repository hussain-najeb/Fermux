package org.foss.fermux.fermuxUIComponents.generalComponents

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
                         .padding(bottom = 15.dp)
                         .width(250.dp)
                         .border(1.dp, FermuxColors.fermuxGenericBorder, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    containerColor = FermuxColors.something3,
                    contentColor = FermuxColors.fermuxWhiteColor,)
                    {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                         Text(
                              text = data.visuals.message,
                              fontSize = 16.sp,
                              fontFamily = FontFamily.Default
                         )
                    }
               }
          }
     }
}