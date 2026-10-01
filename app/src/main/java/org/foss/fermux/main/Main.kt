package org.foss.fermux.main

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.video.VideoFrameDecoder
import com.yausername.youtubedl_android.YoutubeDL
import org.foss.fermux.utils.FermuxAppMainScreen

class MainActivity : ComponentActivity() {
     @RequiresApi(Build.VERSION_CODES.S)
     override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)

          enableEdgeToEdge()

          YoutubeDL.getInstance().init(this)

          setContent {

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
