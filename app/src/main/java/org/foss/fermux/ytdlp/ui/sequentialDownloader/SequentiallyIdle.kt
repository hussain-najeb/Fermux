package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.foss.fermux.R
import org.foss.fermux.components.buttons.GlobalCancelButton
import org.foss.fermux.components.buttons.ImageButton
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.components.generalComponents.SettingAlertDialog
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SequentialIdle(
     navController: NavController,
) {

     val sequentialViewModel: SequentialViewModel = viewModel()


     var dialogDismiss by remember { mutableStateOf(false) }

     val submittedUrls = sequentialViewModel.downloadUrl
          .map(String::trim)
          .filter(String::isNotBlank)

     LargeTopBarScaffold(
          title = "Sequential Downloads",
          onBack = navController::popBackStack,
          firstButton = { dialogDismiss = !dialogDismiss },
          firstImage = R.drawable.info
     ) { innerPadding ->
          Column(
               modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
          ) {
               LazyColumn(
                    modifier = Modifier
                         .padding(8.dp)
                         .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
               ) {
                    items(
                         count = sequentialViewModel.downloadUrl.size,
                         key = { index -> index }

                    ) { index ->
                         OutlinedTextField(
                              modifier = Modifier
                                   .fillMaxWidth()
                                   .padding(start = 15.dp, end = 15.dp),
                              value = sequentialViewModel.downloadUrl[index],
                              onValueChange = { value ->
                                   sequentialViewModel.downloadUrl[index] = value
                              },
                              placeholder = {
                                   Text(
                                        text = "Type URL Here",
                                        fontFamily = FontFamily.Default,
                                        textAlign = TextAlign.Start,
                                        color = FermuxColors.offWhiteTextColor,
                                        modifier = Modifier.padding(start = 9.dp, bottom = 5.dp)
                                   )
                              },
                              shape = RoundedCornerShape(8.dp),
                              minLines = 1,
                              maxLines = 7,
                              colors = OutlinedTextFieldDefaults.colors(
                                   focusedBorderColor = FermuxColors.fermuxSecondaryBorder,
                                   unfocusedBorderColor = FermuxColors.fermuxGenericBorder,
                                   focusedLabelColor = FermuxColors.fermuxPrimaryBorder,
                                   unfocusedLabelColor = FermuxColors.offWhiteTextColor,
                                   cursorColor = FermuxColors.fermuxGenericBorder,
                                   focusedTextColor = Color.White,
                                   unfocusedTextColor = Color.White,
                                   errorTextColor = FermuxColors.fermuxLightErrorTextColor,
                                   errorBorderColor = FermuxColors.fermuxLightErrorTextColor,
                                   errorLabelColor = FermuxColors.fermuxLightErrorTextColor,
                                   errorCursorColor = FermuxColors.fermuxLightErrorTextColor,
                                   errorContainerColor = FermuxColors.fermuxErrorCardColor,
                                   unfocusedContainerColor = FermuxColors.warmPurple,
                                   focusedContainerColor = FermuxColors.inActiveTextField
                              ),
                              trailingIcon = {
                                   androidx.compose.animation.AnimatedVisibility(
                                        visible = sequentialViewModel.downloadUrl.isNotEmpty(),
                                        enter = slideInVertically(tween(70)) + fadeIn(tween(100)),
                                        exit = slideOutVertically(tween(70)) + fadeOut(tween(100))
                                   ) {
                                        GlobalCancelButton(
                                             modifier = Modifier.size(40.dp).padding(end = 3.dp), onClick = {
                                                  sequentialViewModel.downloadUrl.remove("")
                                             }
                                        )
                                   }
                              },
                              keyboardOptions = KeyboardOptions(
                                   imeAction = ImeAction.Send,
                                   capitalization = KeyboardCapitalization.None,
                                   autoCorrectEnabled = false
                              ),
                              )
                    }
                    item {
                         ImageButton(
                              modifier = Modifier,
                              image = R.drawable.add,
                              onClick = { sequentialViewModel.downloadUrl.add("") }
                         )
                    }
               }
          }
     }

     if (dialogDismiss) SettingAlertDialog(
          onDismissRequest = { dialogDismiss = false },
          title = "Sequential Download Info",
          settingImage = R.drawable.sequence,
          description = "Add a url for each text field and press the download button to download each one at a time, you don't have to fill all the text fields"
     )
}


