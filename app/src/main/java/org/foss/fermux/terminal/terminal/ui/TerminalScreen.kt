package org.foss.fermux.terminal.terminal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import org.foss.fermux.terminal.terminal.logic.TerminalLine
import org.foss.fermux.terminal.terminal.logic.TermuxOutput
import org.foss.fermux.terminal.terminal.logic.myTermuxCommands
import org.foss.fermux.ui.theme.JetbrainsMono
import kotlin.collections.plus

@Composable
fun FermuxTerminalScreen(

) {
    val context = LocalContext.current
    var userCommand by remember { mutableStateOf(TextFieldValue("")) }
    var history by remember { mutableStateOf(listOf<String>()) }
    var commandplace by remember { mutableIntStateOf(-1) }



    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .systemBarsPadding()
            .background(
                Color(0xFF282C34)
            )
    )
    {


        LazyColumn(modifier = Modifier
            .weight(1f),
            reverseLayout = true

            ) {
            items(TermuxOutput.lines.reversed()) { line ->
                when (line) {
                    is TerminalLine.Output -> Text(
                        text = line.outputText,
                        color = Color.White,
                        fontFamily = JetbrainsMono,
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )

                    is TerminalLine.Prompt -> Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = Color(0xFF5669BD))) { append("u0_a319") }
                            withStyle(SpanStyle(color = Color(0xFFABB2BF))) { append("@") }
                            withStyle(SpanStyle(color = Color(0xFF678E55))) { append("fermux") }
                            withStyle(SpanStyle(color = Color(0xFFABB2BF))) { append(":~$ ") }
                            withStyle(SpanStyle(color = Color(0xFF9EA55D))) { append(line.userPrompt) }
                },
                        fontFamily = JetbrainsMono,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 10.dp)

                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(1.dp))



        BasicTextField(
            value = userCommand,
            onValueChange = { userCommand = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Send,
                keyboardType = KeyboardType.Ascii,
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false
            ),
            modifier = Modifier
                    .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 12.dp, vertical = 8.dp),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (userCommand.text.isNotBlank()) {
                        val cancelCommand = userCommand.text.trim()
                        if (cancelCommand == "clear") {
                            TermuxOutput.lines = listOf()
                        } else {
                            TermuxOutput.lines += TerminalLine.Prompt(cancelCommand)
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






        Spacer(modifier = Modifier.height(16.dp))

        // Arrow key toolbar for cursor movement inside the text field.
        // Sits above the keyboard when the text field is focused.
        ArrowKeyMovement(
            userCommand = userCommand,
            onCommandChange = { userCommand = it },
            onHistoryUp = {
                if (history.isNotEmpty()) {
                    commandplace = (commandplace + 1).coerceAtMost(history.size - 1)
                    val command = history[history.size - 1 - commandplace]
                    userCommand = TextFieldValue(command, TextRange(command.length))
                }
            },
            onHistoryDown = {
                commandplace = (commandplace - 1).coerceAtLeast(-1)
                if (commandplace == -1) {
                    userCommand = TextFieldValue("")
                } else {
                    val command = history[history.size - 1 - commandplace]
                    userCommand = TextFieldValue(command, TextRange(command.length))
                }
            }
        )
    }
}




