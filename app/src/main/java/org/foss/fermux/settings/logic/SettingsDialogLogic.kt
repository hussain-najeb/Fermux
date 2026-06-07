package org.foss.fermux.settings.logic

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogLogic(
    title: String,
    text: String,
    onDismiss: () -> Unit,
) {
// for future Dialog windows use BasicAlertDialog()

}