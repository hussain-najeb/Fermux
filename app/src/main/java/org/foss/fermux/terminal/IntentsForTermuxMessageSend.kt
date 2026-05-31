package org.foss.fermux.terminal

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object TermuxOutput {
    var output by mutableStateOf("")
}



@SuppressLint("SdCardPath")
fun myTermuxCommands(context: Context, command: String) {

    // Show the command immediately in the output before Termux responds

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
