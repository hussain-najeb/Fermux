package org.foss.fermux.ytdlp.ui.ytdlpMainScreen


import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun SponsorBlockOptions(onDismissRequest: () -> Unit) {


     BasicAlertDialog(
          onDismissRequest = onDismissRequest,
          modifier = Modifier.wrapContentSize(),
          properties = DialogProperties(
               dismissOnBackPress = true,
               dismissOnClickOutside = true,
               usePlatformDefaultWidth = false
               ),
          ) {
          Surface(
               modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .border(1.dp, FermuxColors.fermuxGenericBorder, RoundedCornerShape(14.dp))
                    .wrapContentHeight(),
               shape = MaterialTheme.shapes.large,
               tonalElevation = 6.dp,
               color = FermuxColors.fermuxComponents
          ) {
               Column(modifier = Modifier.padding(24.dp)) {

                         Text(
                        text =  "Sponsor Categories",
                         fontSize = 18.sp,
                         style = MaterialTheme.typography.headlineMediumEmphasized //TODO. Finish this for the love of god
                    )

               }
          }
     }
}
