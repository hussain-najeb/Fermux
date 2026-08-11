package org.foss.fermux.settings.logic

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class SettingListInfo(
     val title: String,
     val description: String,
     val icon: ImageVector? = null,
     val image: Painter? = null,
     val route: String? = null,
     val content: @Composable (() -> Unit)? = null
)