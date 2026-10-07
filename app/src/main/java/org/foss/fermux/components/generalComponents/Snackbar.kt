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
import org.foss.fermux.R
import org.foss.fermux.components.buttons.SmallActionButton
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
                         .border(1.dp, FermuxColors.fermuxHelperBorder, RoundedCornerShape(8.dp)),
                    shape = RoundedCornerShape(8.dp),
                    containerColor = FermuxColors.mutedBlue,
                    contentColor = FermuxColors.white
               ) {
                    Row(
                         modifier = Modifier.fillMaxWidth(),
                         verticalAlignment = Alignment.CenterVertically,
                         horizontalArrangement = Arrangement.Center
                    ) {
                         Box(
                              modifier = Modifier.weight(1f),
                              contentAlignment = Alignment.Center
                         ) {
                              Text(
                                   text = data.visuals.message,
                                   fontSize = 16.sp,
                                   fontFamily = FontFamily.Default,
                              )
                         }
                         Box(
                              modifier = Modifier,
                              contentAlignment = Alignment.CenterEnd
                         ) {
                              data.visuals.actionLabel?.let {
                                   SmallActionButton(
                                        onClick = data::performAction,
                                        image = R.drawable.undo
                                   )
                              }
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
                    .background(FermuxColors.background)
                    .padding(paddingValues),
               verticalArrangement = Arrangement.Center,
               horizontalAlignment = Alignment.CenterHorizontally
          ) {
               Button(
                    enabled = !settingsReset,
                    onClick = {
                         val oldSettingsResetState = settingsReset
                         settingsReset = true

                         scope.launch {
                              val result = snackbarHostState.showSnackbar(
                                   message = " settings reset",
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
