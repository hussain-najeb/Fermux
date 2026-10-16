package org.foss.fermux.ytdlp.ui.sequentialDownloader

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.foss.fermux.R
import org.foss.fermux.components.buttons.CancelButton
import org.foss.fermux.components.buttons.IconTextButton
import org.foss.fermux.components.buttons.ImageButton
import org.foss.fermux.components.downloaderComponents.SequentialModalSheet
import org.foss.fermux.components.generalComponents.AppSnackBar
import org.foss.fermux.components.generalComponents.LargeTopBarScaffold
import org.foss.fermux.components.generalComponents.SettingAlertDialog
import org.foss.fermux.settings.logic.DownloaderSettingsViewModel
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ytdlp.logic.sequentialDownloader.SequentialViewModel

@Composable
fun SequentialIdle(
    navController: NavController,
    sequentialViewModel: SequentialViewModel
) {

    val downloaderSettingsViewModel: DownloaderSettingsViewModel = viewModel()
    val context = LocalContext.current

    var dialogDismiss by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()


    val submittedUrls = sequentialViewModel.downloadUrl
        .map(String::trim)
        .filter(String::isNotBlank)

    LargeTopBarScaffold(
        title = "Sequential Downloads",
        onBack = navController::popBackStack,
        firstButton = { dialogDismiss = !dialogDismiss },
        firstImage = R.drawable.info,
        snackbarHost = { AppSnackBar(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
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
                            enter = slideInVertically(tween(170)) + fadeIn(tween(200)),
                            exit = slideOutVertically(tween(170)) + fadeOut(tween(200))
                        ) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
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
                                        modifier = Modifier.padding(6.dp)
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
                item {
                    Row(
                        modifier = Modifier.animateItem(
                            placementSpec = MaterialTheme.motionScheme.fastEffectsSpec()
                        ),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        ImageButton(
                            modifier = Modifier.padding(5.dp),
                            image = R.drawable.add,
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, FermuxColors.fermuxHelperBorder),
                            onClick = { sequentialViewModel.downloadUrl.add("") }
                        )
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
                    IconTextButton(
                        modifier = Modifier
                            .padding(12.dp),
                        image = R.drawable.settings_2,
                        text = "Configure",
                        onClick = sequentialViewModel::showBottomSheet
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    IconTextButton(
                        modifier = Modifier
                            .padding(12.dp),
                        image = R.drawable.download_side_bar,
                        text = "Download All",
                        enabled = true,
                        onClick = {
                            sequentialViewModel.startSequentiallyDownloading(context, submittedUrls)
                        },
                    )
                }
            }
        }
    }

    if (dialogDismiss) SettingAlertDialog(
        onDismissRequest = { dialogDismiss = false },
        title = "Sequentially?",
        settingImage = R.drawable.sequence,
        specialDescription = buildAnnotatedString {
            withStyle(SpanStyle(FermuxColors.white)) { append("Add a url for each text field you want, each entry will be downloaded in the chosen sequence one at a time. Consider the arguments tab, yes, these also apply, and the general downloader settings as well.  The settings are") }
            withStyle(SpanStyle(FermuxColors.warmBlue)) { append(" Atomic.") }
        }
    )

    if (sequentialViewModel.showModalSheet)
        SequentialModalSheet(
            onDismissRequest = sequentialViewModel::hideDeleteDialog,
            downloaderSettingsViewModel = downloaderSettingsViewModel
        )

}
