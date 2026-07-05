package org.foss.fermux.terminal.main.ui

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


        // Calculate new positions before the buttons so both can read them.
        // coerceAtLeast(0) prevents going below position 0 (start of text).
        // coerceAtMost(length) prevents going past the end of the text.
        val leftArrow = (userCommand.selection.start - 1).coerceAtLeast(0)
        val rightArrow = (userCommand.selection.start + 1).coerceAtMost(userCommand.text.length)

        // Move cursor one position to the left
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


