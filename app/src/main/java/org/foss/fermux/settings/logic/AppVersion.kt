package org.foss.fermux.settings.logic

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log

fun Context.getAppVersionName(): String {
     return try {
          packageManager.getPackageInfo(packageName, 0).versionName ?: "Unknown"
     } catch (e: PackageManager.NameNotFoundException) {
          Log.e("fermux", "App version failure", e)
          "Unknown"
     }
}