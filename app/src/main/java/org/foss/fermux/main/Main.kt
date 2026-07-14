package org.foss.fermux.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.yausername.ffmpeg.FFmpeg.getInstance
import com.yausername.youtubedl_android.YoutubeDL
import org.foss.fermux.ui.theme.FermuxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getInstance().init(this)
        YoutubeDL.getInstance().init(this)


        setContent {
            FermuxTheme {

                FermuxAppMainScreen()
            }
        }
    }
}