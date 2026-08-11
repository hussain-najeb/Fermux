package org.foss.fermux.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.foss.fermux.settings.logic.getAppVersionName

@Composable
fun AboutPage() {
     val context = LocalContext.current
     val versionName = remember { context.getAppVersionName() }




}