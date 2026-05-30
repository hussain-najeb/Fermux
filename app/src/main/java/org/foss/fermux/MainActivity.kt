package org.foss.fermux

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.imePadding
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.foss.fermux.ui.theme.FermuxTheme
import org.foss.fermux.ui.theme.JetbrainsMono

/**
 * Entry point of the app.
 * Sets up edge-to-edge display and launches MainScreen.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FermuxTheme {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
        val context = LocalContext.current
        var userCommand by remember { mutableStateOf(TextFieldValue("")) }


        Column(
            Modifier
                .fillMaxSize()
                .imePadding()
                .systemBarsPadding()
                .background( Color(0xFF282C34)
                )
        )
                {
            var history by remember { mutableStateOf(listOf<String>()) }
            var commandplace by remember { mutableStateOf(-1) }
            Spacer(modifier = Modifier.height(10.dp))



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


                    LazyColumn(modifier = Modifier.weight(1f)){
                        item {
                            SelectionContainer {
                                Text(
                                    text = TermuxOutput.output,
                                    fontFamily = JetbrainsMono,
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(10.dp),
                                )
                            }
                        }
                    }

            Spacer(modifier = Modifier.height(16.dp))

            // Arrow key toolbar for cursor movement inside the text field.
            // Sits above the keyboard when the text field is focused.
            ArrowKeyMovement(
                userCommand = userCommand,
                onCommandChange = { userCommand = it }
            )
        }
    }

    /**
     * A row of buttons for moving the cursor inside the text field.
     *
     * Does not send commands to Termux — only updates cursor position
     * inside [userCommand] via [onCommandChange].
     *
     * @param userCommand Current text field value including cursor position.
     * @param onCommandChange Callback to update userCommand in MainScreen.
     */
    @Composable
    fun ArrowKeyMovement(
        userCommand: TextFieldValue,
        onCommandChange: (TextFieldValue) -> Unit,
    )

    {
        Row(modifier = Modifier) {

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

    /**
     * App-wide shared state for terminal output.
     *
     * Using mutableStateOf means any composable reading [output]
     * will automatically recompose when it changes.
     * This is updated by [Receiver] when Termux returns command results,
     * and by [myTermuxCommands] when a command is sent (to show the prompt line).
     */
    object TermuxOutput {
        var output by mutableStateOf("")
    }

    /**
     * Sends a shell command to Termux via the RUN_COMMAND Intent API.
     *
     * Flow:
     * 1. Immediately appends "$ command" to output so the user sees what was sent.
     * 2. Creates a PendingIntent pointing to [Receiver] as the return address.
     * 3. Builds an Intent addressed to Termux's RunCommandService.
     * 4. Sends the Intent via startService.
     * 5. When Termux finishes, it broadcasts the result back to [Receiver].
     *
     * Note: BACKGROUND = true means Termux runs silently without opening its UI.
     * Interactive commands (nano, vim, htop) will not work correctly this way —
     * they require a real PTY session. Use neofetch --off or fastfetch --logo none
     * instead of their default modes to avoid ANSI rendering issues.
     *
     * @param context Android context required to start services and create intents.
     * @param command The shell command string to run in bash (e.g. "ls", "pwd").
     */
    @SuppressLint("SdCardPath")
    fun myTermuxCommands(context: Context, command: String) {

        // Show the command immediately in the output before Termux responds

        Log.d("fermux", "Sending: $command")

        // Return address — Termux will broadcast results back to Receiver
        val resultIntent = Intent(context, Receiver::class.java).apply {
            action = "org.foss.fermux.COMMAND_RESULT"
        }

        // Wrap return address as a PendingIntent so Termux can deliver it later.
        // System.currentTimeMillis() as request code ensures each command gets
        // a unique envelope — prevents Android from reusing stale intents.
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            System.currentTimeMillis().toInt(),
            resultIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
        )

        // The message to Termux
        val intent = Intent().apply {
            setClassName("com.termux", "com.termux.app.RunCommandService")
            action = "com.termux.RUN_COMMAND"
            putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash")
            putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf("-c", command))
            putExtra("com.termux.RUN_COMMAND_WORKDIR", "/data/data/com.termux/files/home")
            putExtra("com.termux.RUN_COMMAND_BACKGROUND", true)
            putExtra("com.termux.RUN_COMMAND_SESSION_ACTION", "0")
            putExtra("com.termux.RUN_COMMAND_PENDING_INTENT", pendingIntent)
        }

        try {
            context.startService(intent)
        } catch (e: Exception) {
            Log.e("fermux", "Error starting Termux RunCommandService", e)
        }
    }
