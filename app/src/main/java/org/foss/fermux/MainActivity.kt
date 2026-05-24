package org.foss.fermux

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
    val context = LocalContext.current
    val commands = listOf("ls", "neofetch; exec bash", "fastfetch", "touch /data/data/com.termux/files/home/fermux_touch_test")
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.DarkGray)
        ) {
            LazyColumn {
                items(commands) { command ->
                    Button(onClick = { myTermuxCommands(context, command) }) {
                        Text(text = command)
                    }
                }
            }
        }
    }
}

fun myTermuxCommands(context: Context, command: String) {
    Log.d("fermux", "Sending: $command")
    val intent = Intent().apply {
        setClassName("com.termux", "com.termux.app.RunCommandService")
        action = "com.termux.RUN_COMMAND"
        putExtra("com.termux.RUN_COMMAND_PATH", "/data/data/com.termux/files/usr/bin/bash")
        putExtra("com.termux.RUN_COMMAND_ARGUMENTS", arrayOf("-c", command))
        putExtra("com.termux.RUN_COMMAND_BACKGROUND", false)
    }
    val result = context.startService(intent)
    Log.d("fermux", "startService returned: $result")
}