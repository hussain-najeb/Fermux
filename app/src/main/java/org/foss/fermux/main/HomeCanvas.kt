package org.foss.fermux.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import org.foss.fermux.ui.theme.FermuxColors


private data class CanvasInfo(
    val color: Color,
    val transitions: Pair<Dp, Dp>
)

private val circles = listOf(
    CanvasInfo(FermuxColors.darkPurple, 330.dp to 370.dp),
    CanvasInfo(FermuxColors.warmPurple, 250.dp to 290.dp),
    CanvasInfo(FermuxColors.warmBlue, 170.dp to 210.dp),
    CanvasInfo(FermuxColors.skyBlue, 95.dp to 135.dp),
    CanvasInfo(FermuxColors.skyBlue, 35.dp to 65.dp)
)

@Composable
fun HomeCanv(
    ffColor: Boolean? = null,
    ytColor: Boolean? = null
    ) {
    val anim = rememberInfiniteTransition()

    val sizing = anim.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 10_000
                0f at 0
                0f at 3_000 using FastOutSlowInEasing
                1f at 5_000
                1f at 8_000 using FastOutSlowInEasing
                0f at 10_000
            }
        )
    )

    val colors = circles.mapIndexed { index, ring ->
        animateColorAsState(
            targetValue = when {
                index != circles.lastIndex -> ring.color
                ffColor == true -> FermuxColors.fermuxFFmpegGreen
                ytColor == true -> FermuxColors.fermuxErrorCardColor
                else -> ring.color
            },
            animationSpec = tween(500)
        )
    }

    Canvas(
        Modifier.fillMaxSize()
    ) {
        val radius = 20.dp.toPx()
        val bottomLeft = Offset(radius, size.height - radius)

        circles.forEachIndexed { index, ring ->
            val (normal, grown) = ring.transitions
            drawCircle(
                color = colors[index].value,
                radius = lerp(normal.toPx(), grown.toPx(), sizing.value),
                center = bottomLeft
            )

        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF3d444f, showSystemUi = true)
@Composable
fun test24() {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        HomeCanv(ffColor = true)
    }
}
