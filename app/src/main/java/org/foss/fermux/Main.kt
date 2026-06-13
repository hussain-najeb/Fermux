package org.foss.fermux


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yausername.youtubedl_android.YoutubeDL
import android.util.Log
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.foss.fermux.ui.theme.FermuxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        YoutubeDL.getInstance().init(this)
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                YoutubeDL.getInstance().updateYoutubeDL(this@MainActivity, YoutubeDL.UpdateChannel.STABLE)
                Log.d("fermux", "yt-dlp updated successfully")
            } catch (e: Exception) {
                Log.e("fermux", "yt-dlp update failed", e)
            }
        }

        setContent {
            FermuxTheme {
                FermuxAppMainScreen()
            }
        }
    }
}