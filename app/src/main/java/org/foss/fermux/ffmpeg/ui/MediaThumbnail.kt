package org.foss.fermux.ffmpeg.ui

import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.video.videoFrameMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MediaThumbnailImage(
    uri: Uri?,
    contentScale: ContentScale,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var embeddedThumbnail by remember(uri) { mutableStateOf<ByteArray?>(null) }
    var finishedCheck by remember(uri) { mutableStateOf(false) }

    LaunchedEffect(uri) {
        finishedCheck = false
        embeddedThumbnail = null

        if (uri != null) {
            embeddedThumbnail = withContext(Dispatchers.IO) {
                val retriever = MediaMetadataRetriever()
                try {
                    when {
                        uri.scheme == "content" -> retriever.setDataSource(context, uri)
                        uri.scheme == "file"    -> uri.path?.let { retriever.setDataSource(it) }
                        else                    -> retriever.setDataSource(uri.toString())
                    }
                    retriever.embeddedPicture
                } catch (_: Exception) {
                    null
                } finally {
                    try { retriever.release() } catch (e: Exception) {
                        Log.e("coil error ffmpeg", "error with loading the thumbnail to ffmpeg from coil", e)
                    }
                }
            }
        }
        finishedCheck = true
    }

    val request = remember(uri, finishedCheck, embeddedThumbnail) {
        ImageRequest.Builder(context)
            .data(
                if (finishedCheck && embeddedThumbnail != null) {
                    embeddedThumbnail
                } else {
                    uri
                }
            )
            .apply {
                if (finishedCheck && embeddedThumbnail == null) {
                    videoFrameMillis(5000)
                }
            }
            .build()
    }

    AsyncImage(
        model = request,
        contentDescription = null,
        contentScale = contentScale,
        modifier = modifier
    )
}