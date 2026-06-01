package org.foss.fermux.terminal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.sp
import org.foss.fermux.ui.theme.JetbrainsMono

@Composable
fun FermuxMainScreen(

) {
    val context = LocalContext.current
    var userCommand by remember { mutableStateOf(TextFieldValue("")) }
    var history by remember { mutableStateOf(listOf<String>()) }
    var commandplace by remember { mutableStateOf(-1) }





    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .systemBarsPadding()
            .background( Color(0xFF282C34)
            )
    )
    {

        LazyColumn(modifier = Modifier
            .weight(1f))

        {
            item {
                SelectionContainer {
                    Text(
                        text = TermuxOutput.output,
                        fontFamily = JetbrainsMono,
                        color = Color.White,
                        modifier = Modifier
                            .padding(10.dp),
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(1.dp))



        Column {
            BasicTextField(
                value = userCommand,
                onValueChange = { userCommand = it },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (userCommand.text.isNotBlank()) {
                            val cancelCommand = userCommand.text.trim()
                            if (cancelCommand == "clear") {
                                TermuxOutput.output = ""
                            } else {
                                myTermuxCommands(context, cancelCommand)
                            }
                            history = history + cancelCommand
                            commandplace = -1
                            userCommand = TextFieldValue("")
                        }
                    }
                ),
                textStyle = TextStyle(
                    fontFamily = JetbrainsMono,
                    color = Color(0xFF9EA55D),
                    fontSize = 14.sp
                ),
                cursorBrush = SolidColor(Color(0xFF678E55)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "u0_a319",
                            color = Color(0xFF5669BD),
                            fontFamily = JetbrainsMono,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "@",
                            color = Color(0xFFABB2BF),
                            fontFamily = JetbrainsMono,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "fermux",
                            color = Color(0xFF678E55),
                            fontFamily = JetbrainsMono,
                            fontSize = 14.sp
                        )
                        Text(
                            text = ":",
                            color = Color(0xFFABB2BF),
                            fontFamily = JetbrainsMono,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "~",
                            color = Color(0xFF678E55),
                            fontFamily = JetbrainsMono,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "$ ",
                            color = Color(0xFFABB2BF),
                            fontFamily = JetbrainsMono,
                            fontSize = 14.sp
                        )

                        innerTextField()
                    }

                })

        }




        Spacer(modifier = Modifier.height(16.dp))

        // Arrow key toolbar for cursor movement inside the text field.
        // Sits above the keyboard when the text field is focused.
        ArrowKeyMovement(
            userCommand = userCommand,
            onCommandChange = { userCommand = it },
            onHistoryUp = {
                if (history.isNotEmpty()) {
                    commandplace = (commandplace + 1).coerceAtLeast(history.size - 1)
                    val command = history[history.size - 1 - commandplace]
                    userCommand = TextFieldValue(command, TextRange(command.length))
                }
            },
            onHistoryDown = {
                if (history.isNotEmpty()) {
                    commandplace = (commandplace - 0 ).coerceAtMost(history.size - 1)
                    val command = history[history.size - 1 - commandplace]
                    userCommand = TextFieldValue(command, TextRange(command.length))
                }
            }
        )
    }
}


