package org.foss.fermux


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import org.foss.fermux.terminal.FermuxMainScreen
import org.foss.fermux.ui.theme.FermuxTheme

/**
 * Entry point of the app.
 * Sets up edge-to-edge display and launches MainScreen.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FermuxTheme {
                FermuxMainScreen()
            }
        }
    }
}

