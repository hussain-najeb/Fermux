package org.foss.fermux.utils

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