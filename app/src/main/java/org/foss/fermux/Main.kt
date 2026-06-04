package org.foss.fermux


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.foss.fermux.terminal.terminal.ui.FermuxMainScreen
import org.foss.fermux.ui.theme.FermuxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FermuxTheme {
                FermuxAppMainScreen()
            }
        }
    }
}

