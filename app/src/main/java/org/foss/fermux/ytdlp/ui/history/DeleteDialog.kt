package org.foss.fermux.ytdlp.ui.history


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
          onDismissRequest = onDismissRequest
     ) {
          val dialogWindow = (LocalView.current.parent as? DialogWindowProvider)?.window
          SideEffect {
               dialogWindow?.apply {
                    addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
                    setDimAmount(0.6f)
               }
          }

          Surface(
               modifier = Modifier
                    .height(150.dp)
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
                              .fillMaxSize()
                              .padding(5.dp),
                         verticalAlignment = Alignment.Bottom,
                         horizontalArrangement = Arrangement.End
                    ) {
                         AppTextButton(
                              modifier = Modifier
                                   .padding(start = 5.dp, bottom = 5.dp, top = 5.dp, end = 95.dp)                                   .align(Alignment.Bottom),
                              text = "Delete",
                              isError = true,
                              onClick = deletedItem,
                         )
                         AppTextButton(
                              modifier = Modifier
                                   .padding(5.dp)
                                   .align(Alignment.Bottom),
                              text = "Cancel",
                              onClick = onDismissRequest
                         )
                    }
               }
          }
     }
}