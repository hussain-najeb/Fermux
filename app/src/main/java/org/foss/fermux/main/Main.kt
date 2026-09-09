package org.foss.fermux.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.video.VideoFrameDecoder
import com.yausername.youtubedl_android.YoutubeDL
import org.foss.fermux.BuildConfig
import org.foss.fermux.ui.theme.FermuxTheme
import org.foss.fermux.utils.FermuxAppMainScreen
import org.foss.fermux.ytdlp.logic.downloader.DebugLog

class MainActivity : ComponentActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          enableEdgeToEdge()

          val sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)
          val debugLoggingEnabled =
               BuildConfig.DEBUG && sharedPreferences.getBoolean("debug_logging", true)
          DebugLog.setEnabled(debugLoggingEnabled)

          YoutubeDL.getInstance().init(this)

          setContent {
               FermuxTheme {
                    setSingletonImageLoaderFactory { context ->
                         ImageLoader.Builder(context)
                              .components {
                                   add(VideoFrameDecoder.Factory())
                              }
                              .build()
                    }
                    FermuxAppMainScreen()
               }
          }
     }
}
