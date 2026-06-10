package org.foss.fermux


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import com.yausername.youtubedl_android.YoutubeDL
import org.foss.fermux.ui.theme.FermuxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            YoutubeDL.getInstance().init(this)
        setContent {
            FermuxTheme {
                FermuxAppMainScreen()
            }
        }
    }
}

