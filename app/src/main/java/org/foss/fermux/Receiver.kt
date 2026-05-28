package org.foss.fermux

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class Receiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val result = intent.getBundleExtra("result")
        if (result == null) {
            Log.e("fermux", "Termux result intent did not contain a result bundle")
            return
        }

        val stdout = result.getString("stdout").orEmpty()
        val stderr = result.getString("stderr").orEmpty()
        val exitCode = result.getInt("exitCode")
        val errCode = result.getInt("err")
        val errorMessage = result.getString("errmsg").orEmpty()


        val cleaned = stdout.replace(Regex("\u001B\\[[^m]*m"), "")
        TermuxOutput.output += cleaned + "\n"


        Log.d("fermux", "output: $stdout")
        Log.d("fermux", "error: $stderr")
        Log.d("fermux", "ExitCode: $exitCode")
        Log.d("fermux", "TermuxErrCode: $errCode")
        Log.d("fermux", "TermuxErrorMessage: $errorMessage")
    }
}
