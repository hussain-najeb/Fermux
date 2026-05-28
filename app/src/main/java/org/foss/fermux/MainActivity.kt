package org.foss.fermux

import android.annotation.SuppressLint
import android.app.PendingIntent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxTheme
import org.foss.fermux.ui.theme.JetbrainsMono

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            0.21f to Color(0xFF152671),
                            0.4f to Color(0xFF1B2C78),
                            0.6f to Color(0xFF2E3E8C),
                            1.8f to Color(0xFF4353A5),
                            1.0f to Color(0xFF5669BD)
                        )
                    )
                )
        )
        {


                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = userCommand,
                    onValueChange = { userCommand = it },
                    label = { Text("") },
                    modifier = Modifier.
                    fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Send
                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (userCommand.text.isNotBlank()) {
                                myTermuxCommands(context, userCommand.text)
                                userCommand = TextFieldValue("")
                            }

                        }
                    ),
                    textStyle = LocalTextStyle.current.copy(color = Color(0xFFFEFEFE)),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF8F97BC),
                        unfocusedBorderColor = Color(0xFF5669bd), // change this to #5669bd so it looks better
                        focusedTextColor = Color(0xFFFEFEFE),
                        unfocusedTextColor = Color(0xFFFEFEFE)
                    )
                )
                LazyColumn {
                    item {
                        SelectionContainer {


                            Text(
                                text = TermuxOutput.output,
                                fontFamily = JetbrainsMono,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp),
                            )
                        }
                    }
                }
            Spacer(modifier = Modifier.height(16.dp))
            ArrowKeyMovement(
                userCommand = userCommand,
                onCommandChange = { userCommand = it })
            }
        }
    }




@Composable
fun ArrowKeyMovement(
    userCommand: TextFieldValue,
    onCommandChange: (TextFieldValue) -> Unit, )
{
    Row(modifier = Modifier
        ) {
        Button(onClick = {
            val newPos = (userCommand.selection.start -1).coerceAtLeast(0)
            onCommandChange(TextFieldValue(
                text = userCommand.text,
                selection = TextRange(newPos)))

        }) {
            Text("<-")
        }


    }









    }














object TermuxOutput {
    var output by mutableStateOf("")
}


@SuppressLint("SdCardPath")
fun myTermuxCommands(context: Context, command: String) {
    TermuxOutput.output += "$ $command\n"
    Log.d("fermux", "Sending: $command")

    val resultIntent = Intent(context, Receiver::class.java).apply {
        action = "org.foss.fermux.COMMAND_RESULT"
    }
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        System.currentTimeMillis().toInt(),
        resultIntent,
        PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_MUTABLE
    )
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
