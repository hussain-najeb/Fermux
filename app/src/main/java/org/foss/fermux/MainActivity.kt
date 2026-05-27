package org.foss.fermux

import android.annotation.SuppressLint
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.foss.fermux.ui.theme.FermuxTheme

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
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var userCommand by remember { mutableStateOf("") }
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = Brush.linearGradient(
                        colorStops = arrayOf(
                            0.21f to Color(0xFF152671), // Your original dark
                            0.4f to Color(0xFF1B2C78),  // Your original mid
                            0.6f to Color(0xFF2E3E8C),  // Subtle Slate Indigo (New)
                            1.8f to Color(0xFF4353A5),  // Subtle Muted Blue (New)
                            1.0f to Color(0xFF5669BD)   // Your original light
                        )
                    )
                )
        )
        {

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = userCommand,
                onValueChange = { userCommand = it },
                label = { Text("|") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (userCommand.isNotBlank()) {
                            myTermuxCommands(context, userCommand)
                        }
                    }
                ),
                textStyle = LocalTextStyle.current.copy(color = Color(0xFFFEFEFE)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF8F97BC),
                    unfocusedBorderColor = Color(0xFF8F97BC),
                    focusedTextColor = Color(0xFFFEFEFE),
                    unfocusedTextColor = Color(0xFFFEFEFE)
                )
                )
            Text(
                text = TermuxOutput.output,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                .padding(10.dp)
                .verticalScroll(state = scrollState),
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}



data class TermuxCommand(

    val label: String,
    val script: String
)
object TermuxOutput {
    var output by mutableStateOf("")
}


@SuppressLint("SdCardPath")
fun myTermuxCommands(context: Context, command: String) {
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
