package org.foss.fermux.settings.logic

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class TilePosition {
    TOP,
    MIDDLE,
    BOTTOM,
    SOLO;

    fun toShape(
        outerRadius: Dp = 12.dp,
        innerRadius: Dp = 1.dp
    ): Shape = when (this) {
        TOP -> RoundedCornerShape(
            topStart = outerRadius,
            topEnd = outerRadius,
            bottomStart = 1.dp,
            bottomEnd = 1.dp
        )
        MIDDLE -> RoundedCornerShape(innerRadius)
        BOTTOM -> RoundedCornerShape(
            topStart = 1.dp,
            topEnd = 1.dp,
            bottomStart = outerRadius,
            bottomEnd = outerRadius
        )
        SOLO -> RoundedCornerShape(outerRadius)
    }
}

data class SettingListInfo(
    val title: String,
    val description: String,
    val icon: ImageVector? = null,
    val image: Int? = null,
    val position: TilePosition = TilePosition.MIDDLE,
    val route: String? = null,
    val onClick: (() -> Unit)? = null,
    val content: @Composable (() -> Unit)? = null,
    val trailingContent: @Composable (() -> Unit)? = null
)