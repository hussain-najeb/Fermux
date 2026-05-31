package org.foss.fermux.Terminal

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

@Composable
fun ArrowKeyMovement(
    userCommand: TextFieldValue,
    onCommandChange: (TextFieldValue) -> Unit,
)

{
    Row(modifier = Modifier) {
        var history by remember { mutableStateOf(listOf<String>()) }
        var commandplace by remember { mutableStateOf(-1) }

        // Calculate new positions before the buttons so both can read them.
        // coerceAtLeast(0) prevents going below position 0 (start of text).
        // coerceAtMost(length) prevents going past the end of the text.
        val leftArrow = (userCommand.selection.start - 1).coerceAtLeast(0)
        val rightArrow = (userCommand.selection.start + 1).coerceAtMost(userCommand.text.length)

        // Move cursor one position to the left
        Button(onClick = {
            onCommandChange(
                TextFieldValue(
                    text = userCommand.text,
                    selection = TextRange(leftArrow)
                )
            )
        }) {
            Text("<-")
        }

        // Move cursor one position to the right
        Button(onClick = {
            onCommandChange(
                TextFieldValue(
                    text = userCommand.text,
                    selection = TextRange(rightArrow)
                )
            )
        }) {
            Text("->")
        }
    }
}