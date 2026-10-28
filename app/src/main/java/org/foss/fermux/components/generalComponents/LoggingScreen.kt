package org.foss.fermux.components.generalComponents

import android.content.ClipData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.foss.fermux.components.buttons.ErrorCopyButton
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono


@Composable
fun LoggingScreen(
    loggingTitle: String,
    logs: String,
    navController: NavController
) {

    val pageScroll = rememberScrollState()
    val logsScroll = rememberScrollState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val clipboard = LocalClipboard.current

    MediumTopBarScaffold(
        title = loggingTitle,
        snackbarHost = { AppSnackBar(snackbarHostState) },
        onBack = { navController.popBackStack() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(pageScroll)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                color = FermuxColors.warmPurple,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, FermuxColors.white)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = logs,
                        color = FermuxColors.white,
                        fontFamily = JetbrainsMono,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(logsScroll)
                            .padding(6.dp)
                    )
                }
            }
            ErrorCopyButton(
                modifier = Modifier
                    .padding(7.dp)
                    .align(Alignment.Start)
                    .size(50.dp),
                onClick = {
                    scope.launch {
                        val clipData = ClipData.newPlainText("logs", logs)
                        clipboard.setClipEntry(ClipEntry(clipData))
                        snackbarHostState.showSnackbar(
                            message = "Copied logs",
                            duration = SnackbarDuration.Short
                        )
                    }
                }
            )
        }
    }
}
