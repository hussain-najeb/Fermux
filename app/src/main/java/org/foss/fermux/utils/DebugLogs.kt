package org.foss.fermux.utils

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DebugClass(
     val tag: String,
     val message: String,
     val level: DebugKind,
     val throwable: Throwable? = null,
     val timestamp: Long = System.currentTimeMillis()
)

enum class DebugKind {
     DownloaderDebug,
     DownloaderError,
     FFmpegDebug,
     FFmpegError
}


object DebugLogDownloader {
     private val _enabled = MutableStateFlow(false)
     val enabled = _enabled.asStateFlow()
     private val _downloaderLog = MutableStateFlow<List<DebugClass>>(emptyList())
     val downloaderLog: StateFlow<List<DebugClass>> = _downloaderLog.asStateFlow()

     fun setEnable(value: Boolean) {
          _enabled.value = value
     }

     fun debugDownloader(tag: String, message: String) {
          if (!enabled.value) return

          Log.d(tag, message)

          addDownloaderLogs(
               DebugClass(
                    tag = tag,
                    message = message,
                    level = DebugKind.DownloaderDebug
               )
          )
     }
     fun errorDownloader(
          tag: String,
          message: String,
          throwable: Throwable?
     ) {
          if (!enabled.value) return

          Log.e(tag, message, throwable)

          addDownloaderLogs(
               DebugClass(
                    tag = tag,
                    message = message,
                    level = DebugKind.DownloaderError,
                    throwable = throwable
               )
          )
     }

     private fun addDownloaderLogs(entry: DebugClass) {
          _downloaderLog.update { currentLogs ->
               currentLogs + entry
          }
     }

     val downloaderLogcat = downloaderLog
}

object DebugLogFFmpeg {
     private val _enabled = MutableStateFlow(false)
     val enabled = _enabled.asStateFlow()

     fun setEnable(value: Boolean) {
          DebugLogFFmpeg._enabled.value = value
     }

     private val _ffmpegLog = MutableStateFlow<List<DebugClass>>(emptyList())
     val ffmpegLog: StateFlow<List<DebugClass>> = _ffmpegLog.asStateFlow()

     fun debugFFmpeg(
          tag: String,
          message: String
     ) {
          if (!DebugLogFFmpeg.enabled.value) return

          Log.d(tag, message)

          DebugLogFFmpeg.addFFmpegLog(
               DebugClass(
                    tag = tag,
                    message = message,
                    level = DebugKind.FFmpegDebug
               )
          )
     }

     fun errorFFmpeg(
          tag: String,
          message: String,
          throwable: Throwable?
     ) {
          if (!DebugLogFFmpeg.enabled.value) return

          Log.e(tag, message, throwable)

          DebugLogFFmpeg.addFFmpegLog(
               DebugClass(
                    tag = tag,
                    message = message,
                    level = DebugKind.FFmpegError,
                    throwable = throwable
               )
          )
     }

     private fun addFFmpegLog(entry: DebugClass) {
          _ffmpegLog.update { currentLog ->
               currentLog + entry
          }
     }

     val ffmpegLogcat = ffmpegLog


}