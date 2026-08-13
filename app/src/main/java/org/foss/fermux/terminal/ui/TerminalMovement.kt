package org.foss.fermux.terminal.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun ArrowKeyMovement(
    userCommand: TextFieldValue,
    onCommandChange: (TextFieldValue) -> Unit,
    onHistoryUp: () -> Unit,
    onHistoryDown: () -> Unit
) {

    Row(modifier = Modifier) {
        val leftArrow = (userCommand.selection.start - 1).coerceAtLeast(0)
        val rightArrow = (userCommand.selection.start + 1).coerceAtMost(userCommand.text.length)

        IconButton(onClick = {
            onCommandChange(
                TextFieldValue(
                    text = userCommand.text,
                    selection = TextRange(leftArrow)
                )
            )
        }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "left-arrow")
        }

        // Move cursor one position to the right
        IconButton(onClick = {
            onCommandChange(
                TextFieldValue(
                    text = userCommand.text,
                    selection = TextRange(rightArrow)
                )
            )
        }) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "right-arrow")

        }
        IconButton(onClick = { onHistoryUp() }) {
            Icon(Icons.Filled.KeyboardArrowUp, "up-arrow")
        }

        IconButton(onClick = { onHistoryDown() }) {
            Icon(Icons.Filled.KeyboardArrowDown, "down-arrow")
        }
    }}


