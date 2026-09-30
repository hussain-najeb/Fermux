package org.foss.fermux.components.generalComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.foss.fermux.components.buttons.UndoButton
import org.foss.fermux.ui.theme.FermuxColors

@Composable
fun AppSnackBar(
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
                    contentColor = FermuxColors.fermuxWhiteColor
               ) {
                    Row(
                         modifier = Modifier.fillMaxWidth(),
                         verticalAlignment = Alignment.CenterVertically
                    ) {
                         Text(
                              text = data.visuals.message,
                              fontSize = 16.sp,
                              fontFamily = FontFamily.Default,
                              modifier = Modifier.weight(1f)
                         )

                         data.visuals.actionLabel?.let {
                              UndoButton(
                                   onClick = data::performAction
                              )
                         }
                    }
               }
          }
     }
}

@Preview(showBackground = true, backgroundColor = 0xFF181825)
@Composable
fun test11() {
     val snackbarHostState = remember { SnackbarHostState() }
     val scope = rememberCoroutineScope()
     var settingsReset by remember { mutableStateOf(false) }

     MediumTopBarScaffold(
          title = "Snackbar Preview",
          onBack = {},
          snackbarHost = { AppSnackBar(snackbarHostState) }
     ) { paddingValues ->
          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .background(FermuxColors.fermuxBackground)
                    .padding(paddingValues),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
          ) {
               Text(
                    text = if (settingsReset) "Settings are at their defaults" else "Settings are customized",
                    color = FermuxColors.fermuxWhiteColor
               )
               Button(
                    enabled = !settingsReset,
                    onClick = {
                         val oldSettingsResetState = settingsReset
                         settingsReset = true

                         scope.launch {
                              val result = snackbarHostState.showSnackbar(
                                   message = "Downloader settings reset",
                                   actionLabel = "Undo",
                                   duration = SnackbarDuration.Long
                              )

                              if (result == SnackbarResult.ActionPerformed) {
                                   settingsReset = oldSettingsResetState
                              }
                         }
                    },
                    modifier = Modifier.padding(top = 16.dp)
               ) {
                    Text("Reset downloader settings")
               }
          }
     }
}
