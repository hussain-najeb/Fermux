package org.foss.fermux.terminal.terminal.logic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class Receiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {

        // Termux wraps all results in a Bundle attached to the intent
        val result = intent.getBundleExtra("result")
        if (result == null) {
            Log.e("fermux", "Termux result intent did not contain a result bundle")
            return
        }

        // stdout — the normal output of the command (what you'd see in a terminal)
        val stdout = result.getString("stdout").orEmpty()

        // stderr — error output, shown when something goes wrong
        val stderr = result.getString("stderr").orEmpty()

        // exitCode — 0 means success, anything else means the command failed
        val exitCode = result.getInt("exitCode")

        // errCode and errorMessage — Termux's own internal error codes,
        // separate from the shell command's exit code
        val errCode = result.getInt("err")
        val errorMessage = result.getString("errmsg").orEmpty()


        val cleaned = stdout
            .replace(Regex("\u001B\\[\\??[\\d;]*[A-Za-z]"), "") // CSI sequences: [2J, [?25h, [32m etc
            .replace(Regex("\u001B[()][AB012]"), "")             // charset sequences
            .replace(Regex("\u001B[^\\[]"), "")                  // other bare ESC sequences
            .replace(Regex("\u001B"), "")                        // any remaining lone ESC

        // Append cleaned output to the shared state object.
        // Because TermuxOutput.output uses mutableStateOf, this
        // automatically triggers a recompose in MainScreen.
        TermuxOutput.lines += TerminalLine.Output(cleaned.trimEnd())

        // Debug logging — visible in Android Studio Logcat, not to the user
        Log.d("fermux", "output: $stdout")
        Log.d("fermux", "error: $stderr")
        Log.d("fermux", "ExitCode: $exitCode")
        Log.d("fermux", "TermuxErrCode: $errCode")
        Log.d("fermux", "TermuxErrorMessage: $errorMessage")
    }
}