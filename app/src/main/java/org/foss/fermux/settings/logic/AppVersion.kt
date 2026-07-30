package org.foss.fermux.settings.logic

import android.content.Context
import android.content.pm.PackageManager

fun Context.getAppVersionName(): String {
     return try {
          packageManager.getPackageInfo(packageName, 0).versionName ?: "Unknown"
     } catch (e: PackageManager.NameNotFoundException) {
          "Unknown"
     }
}