package org.foss.fermux.utils

import android.content.Context

fun Context.clearCache() {
     cacheDir.listFiles()?.forEach { it.deleteRecursively() }
     externalCacheDir?.listFiles()?.forEach { it.deleteRecursively() }
}