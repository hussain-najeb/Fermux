package org.foss.fermux.settings.logic

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

data class SettingListInfo(
     val title: String,
     val description: String,
     val icon: ImageVector? = null,
     val image: Int? = null,
     val route: String? = null,
     val onClick: (() -> Unit)? = null,
     val content: @Composable (() -> Unit)? = null,
     val trailingContent: @Composable (() -> Unit)? = null
)