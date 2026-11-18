package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.components.buttons.FilterButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.oneTimeDownloader.SequentialItem
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SequentialIdle(
    sequentialViewModel: SequentialViewModel
) {
    val context = LocalContext.current

    val submittedUrls = sequentialViewModel.downloadUrl
        .map { it.url.trim() }
        .filter(String::isNotBlank)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(start = 2.dp, end = 2.dp, bottom = 80.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            items(
                items = sequentialViewModel.downloadUrl,
                key = { item -> item.id }
            ) { item ->
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
                            sequentialViewModel.downloadUrl.removeAll { it.id == item.id }
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
                            value = item.url,
                            onValueChange = { value ->
                                val index = sequentialViewModel.downloadUrl.indexOfFirst { it.id == item.id }
                                if (index != -1) {
                                    sequentialViewModel.downloadUrl[index] = item.copy(url = value)
                                }
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
                                focusedBorderColor = FermuxColors.fermuxGenericBorder,
                                unfocusedBorderColor = FermuxColors.fermuxHelperBorder,
                                focusedLabelColor = FermuxColors.gray,
                                cursorColor = FermuxColors.white,
                                focusedTextColor = FermuxColors.white,
                                unfocusedTextColor = FermuxColors.white,
                                errorTextColor = FermuxColors.fermuxLightErrorTextColor,
                                errorBorderColor = FermuxColors.fermuxHelperBorder,
                                errorLabelColor = FermuxColors.fermuxLightErrorTextColor,
                                errorCursorColor = FermuxColors.fermuxLightErrorTextColor,
                                errorContainerColor = FermuxColors.darkPurple,
                                unfocusedContainerColor = FermuxColors.warmPurple,
                                focusedContainerColor = FermuxColors.darkPurple
                            ),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrectEnabled = false
                            ),
                        )
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape = RoundedCornerShape(topEnd = 8.dp, topStart = 8.dp),
            color = FermuxColors.darkPurple,
            modifier = Modifier.height(88.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(9.dp)
                    .fillMaxSize(),
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
                    FilterButton(
                        modifier = Modifier.padding(5.dp),
                        image = R.drawable.add,
                        onClick = {
                            sequentialViewModel.downloadUrl.add(SequentialItem())
                        }
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
}
