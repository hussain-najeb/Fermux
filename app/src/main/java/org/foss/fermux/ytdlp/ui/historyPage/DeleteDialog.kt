package org.foss.fermux.ytdlp.ui.historyPage


import android.view.WindowManager
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowProvider
import org.foss.fermux.components.buttons.AppTextButton
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun DeleteDialog(
     deletedItem: () -> Unit,
     onDismissRequest: () -> Unit
) {
     Dialog(
          onDismissRequest = onDismissRequest,
     ) {
          val view = LocalView.current
          SideEffect {
               val window = (view.parent as DialogWindowProvider).window
               window.setDimAmount(0.5f)
               window.addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND
               )
          }
          Surface(
               modifier = Modifier
                    .height(130.dp)
                    .fillMaxWidth()
                    .padding(8.dp),
               shape = RoundedCornerShape(12.dp),
               color = FermuxColors.warmPurple,
               border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder),
          ) {
               Column(
                    modifier = Modifier
                         .padding(2.dp)

               ) {
                    Box(
                         modifier = Modifier
                              .fillMaxWidth()
                              .padding(start = 2.dp, top = 8.dp),
                         contentAlignment = Alignment.Center
                    ) {
                         Text(
                              text = "Confirm Deletion",
                              fontSize = 22.sp,
                              fontStyle = FontStyle.Normal,
                              fontWeight = FontWeight.SemiBold,
                              color = FermuxColors.white
                         )
                    }
                    Row(
                         modifier = Modifier
                         .padding(5.dp),
                         verticalAlignment = Alignment.Bottom,
                         horizontalArrangement = Arrangement.End
                    ) {
                         AppTextButton(
                              modifier = Modifier.padding(5.dp),
                              text = "Delete",
                              onClick = deletedItem,
                         )
                    }
               }
          }
     }
}


@Preview (backgroundColor = 0xFF15152e, showBackground = true)
@Composable
fun test12 () {

     Column(
          modifier = Modifier.fillMaxSize()
     ) {
          DeleteDialog(
               deletedItem = {},
               onDismissRequest = {}
          )
     }
}