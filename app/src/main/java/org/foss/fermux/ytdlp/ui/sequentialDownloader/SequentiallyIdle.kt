package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.components.buttons.FilterButton
import org.foss.fermux.components.buttons.ImageButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SequentialIdle(
    sequentialViewModel: SequentialViewModel
) {
    val context = LocalContext.current

    val submittedUrls = sequentialViewModel.downloadUrl
        .map(String::trim)
        .filter(String::isNotBlank)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(2.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            items(
                count = sequentialViewModel.downloadUrl.size,
                key = { index -> index }

            ) { index ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    val visibilityState = remember {
                        MutableTransitionState(false).apply {
                            targetState = true
                        }
                    }
                    LaunchedEffect(
                        key1 = visibilityState.isIdle,
                        key2 = visibilityState.currentState
                    ) {
                        if (
                            visibilityState.isIdle &&
                            !visibilityState.currentState &&
                            !visibilityState.targetState
                        ) {
                            sequentialViewModel.downloadUrl.removeAt(index)
                        }
                    }
                    androidx.compose.animation.AnimatedVisibility(
                        visibleState = visibilityState,
                        enter = slideInHorizontally(tween(170)) + fadeIn(tween(200)),
                        exit = slideOutHorizontally(tween(170)) + fadeOut(tween(200))
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(5.dp)
                                .fillMaxWidth(),
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
                                )
                            },
                            trailingIcon = {
                                androidx.compose.animation.AnimatedVisibility(
                                    visible = sequentialViewModel.downloadUrl.isNotEmpty(),
                                    enter = slideInVertically(tween(70)) + fadeIn(tween(100)),
                                    exit = slideOutVertically(tween(70)) + fadeOut(tween(100))
                                ) {
                                    CancelButton(
                                        modifier = Modifier.padding(5.dp),
                                        transparency = true,
                                        onClick = {
                                            visibilityState.targetState = false
                                        }
                                    )
                                }
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
                            keyboardOptions = KeyboardOptions(
                                imeAction = ImeAction.Send,
                                capitalization = KeyboardCapitalization.None,
                                autoCorrectEnabled = false
                            ),
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomStart
            ) {
                FilterButton(
                    modifier = Modifier
                        .padding(5.dp),
                    image = R.drawable.settings_2,
                    onClick = sequentialViewModel::showBottomSheet
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                ImageButton(
                    modifier = Modifier.padding(5.dp),
                    image = R.drawable.add,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder),
                    onClick = { sequentialViewModel.downloadUrl.add("") }
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.BottomEnd
            ) {
                FilterButton(
                    modifier = Modifier
                        .padding(5.dp),
                    image = R.drawable.download_side_bar,
                    enabled = true,
                    onClick = {
                        sequentialViewModel.startSequentiallyDownloading(context, submittedUrls)
                    },
                )
            }
        }
    }
}
