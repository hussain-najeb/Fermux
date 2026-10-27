package org.foss.fermux.ffmpeg.ui.ffmpegStateCards

import android.content.ClipData
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.foss.fermux.R
import org.foss.fermux.components.buttons.IconTextButton
import org.foss.fermux.components.ffmpegComponents.FFmpegCard
import org.foss.fermux.ui.theme.FermuxColors
import org.foss.fermux.ui.theme.JetbrainsMono

@Composable
fun FFmpegErrorMassage(
    errorMessage: String,
    rawError: String,
    onCancel: () -> Unit
) {

    val clipboard = LocalClipboard.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
    ) {
        FFmpegCard(
            modifier = Modifier
                .height(450.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Error,
                        contentDescription = null,
                        tint = FermuxColors.fermuxLightErrorTextColor,
                        modifier = Modifier
                            .padding(top = 13.dp, start = 6.dp)
                            .size(28.dp)
                    )
                    Text(
                        text = errorMessage,
                        fontSize = 12.sp,
                        fontStyle = FontStyle.Normal,
                        fontFamily = JetbrainsMono,
                        color = FermuxColors.fermuxLightErrorTextColor,
                        modifier = Modifier.padding(top = 22.dp, start = 9.dp)
                    )
                }
                Text(
                    text = rawError,
                    fontSize = 12.sp,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Default,
                    color = FermuxColors.fermuxLightErrorTextColor,
                    modifier = Modifier.padding(top = 20.dp, start = 12.dp)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconTextButton(
                text = "Copy Error",
                image = R.drawable.copy,
                onClick = {
                    scope.launch {
                        val clipData = ClipData.newPlainText("ffmpeg error", rawError)
                        clipboard.setClipEntry(ClipEntry(clipData))
                    }
                }
            )
            IconTextButton(
                text = "Back",
                activeColor = FermuxColors.fermuxFFmpegGreen,
                activeIconColor = FermuxColors.white,
                image = R.drawable.undo,
                onClick = { onCancel() }
            )
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFF15152e)
@Composable
fun test19() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        FFmpegErrorMassage(
            rawError = "ffmpeg version 9.0.2 Copyright (c) 2000-2026 the FFmpeg developers\n" +
                 "  built with Android (13691557, +pgo, +bolt, +lto, +mlgo, based on r522817d) clang version 18.0.4 (https://android.googlesource.com/toolchain/llvm-project d8003a456d14a3deb8054cdaa529ffbf02d9b262)\n" +
                 "  configuration: Android build\n" +
                 "  libavutil      61.  1.102 / 61.  1.102\n" +
                 "  libavcodec     63.  1.102 / 63.  1.102\n" +
                 "  libavformat    63.  1.102 / 63.  1.102\n" +
                 "  libavdevice    63.  1.102 / 63.  1.102\n" +
                 "  libavfilter    12.  1.102 / 12.  1.102\n" +
                 "  libswscale     10.  1.102 / 10.  1.102\n" +
                 "  libswresample   7.  1.102 /  7.  1.102\n" +
                 "Input #0, jpeg_pipe, from '/data/user/0/org.foss.fermux/cache/input_b7a12f5b-7ae3-47d7-8518-e8959420933c.tmp':\n" +
                 "  Duration: N/A, bitrate: N/A\n" +
                 "  Stream #0:0: Video: mjpeg (Baseline), yuvj420p(pc, bt470bg/unknown/unknown), 256x144 [SAR 1:1 DAR 16:9], 25 fps, 25 tbr, 25 tbn\n" +
                 "Output #0, flac, to '/data/user/0/org.foss.fermux/cache/output_b7a12f5b-7ae3-47d7-8518-e8959420933c.flac':\n" +
                 "[out#0/flac @ 0x7c3a2a611b00] Output file does not contain any stream\n" +
                 "Error opening output file /data/user/0/org.foss.fermux/cache/output_b7a12f5b-7ae3-47d7-8518-e8959420933c.flac.\n" +
                 "Error opening output files: Invalid argument\n",
            errorMessage = "SomeError as well",
            onCancel = {},
        )
    }
}