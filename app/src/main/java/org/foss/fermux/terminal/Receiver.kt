package org.foss.fermux.terminal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Catches the result broadcast from Termux after a command finishes running.
 *
 * When [myTermuxCommands] sends a command to Termux, it attaches a PendingIntent
 * as a return address. When Termux finishes, it broadcasts the result back to
 * this receiver. Android calls [onReceive] automatically when that broadcast arrives.
 *
 * Registered in AndroidManifest.xml with action "org.foss.fermux.COMMAND_RESULT".
 * Must have android:exported="true" so Termux (an external app) can reach it.
 */
class Receiver : BroadcastReceiver() {

    /**
     * Called automatically by Android when Termux broadcasts command results.
     *
     * Extracts stdout, stderr, and exit codes from the result bundle,
     * strips ANSI escape codes from stdout, then appends the clean output
     * to [TermuxOutput.output] which triggers a UI recompose in MainScreen.
     *
     * @param context Android context provided by the system.
     * @param intent The broadcast intent containing the result bundle from Termux.
     */
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

        // Strip ANSI escape codes from stdout before displaying.
        // Terminal programs embed color/cursor codes like 32m and ?25h
        // that look like garbage in a plain text view.
        // This regex matches and removes all of them.
        val cleaned = stdout
            .replace(Regex("\u001B\\[\\??[\\d;]*[A-Za-z]"), "") // CSI sequences: [2J, [?25h, [32m etc
            .replace(Regex("\u001B[()][AB012]"), "")             // charset sequences
            .replace(Regex("\u001B[^\\[]"), "")                  // other bare ESC sequences
            .replace(Regex("\u001B"), "")                        // any remaining lone ESC

        // Append cleaned output to the shared state object.
        // Because TermuxOutput.output uses mutableStateOf, this
        // automatically triggers a recompose in MainScreen.
        TermuxOutput.lines += TerminalLine.Output(cleaned)

        // Debug logging — visible in Android Studio Logcat, not to the user
        Log.d("fermux", "output: $stdout")
        Log.d("fermux", "error: $stderr")
        Log.d("fermux", "ExitCode: $exitCode")
        Log.d("fermux", "TermuxErrCode: $errCode")
        Log.d("fermux", "TermuxErrorMessage: $errorMessage")
    }
}