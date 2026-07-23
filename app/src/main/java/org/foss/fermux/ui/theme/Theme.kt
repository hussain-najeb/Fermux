package org.foss.fermux.ui.theme

import android.os.Build
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun FermuxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        motionScheme = MotionScheme.expressive(),
        content = content
    )
}









@Composable
fun FermuxCard(
    // Core
    modifier: Modifier = Modifier,
    clickable: (() -> Unit)? = null,
    color: FermuxColor = FermuxColors,

    // Card shape & size
    cardShape: Shape = RoundedCornerShape(4.dp),
    cardPadding: Dp,
    cardSize: Dp? = null,
    isExpanded: Boolean = false,
    aspectRatio: Float? = null,

    content: @Composable ColumnScope.() -> Unit
) {

    val cardExpansion by animateDpAsState(
        targetValue = if (isExpanded) 80.dp else 74.dp,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "card get bigger"
    )

// Card color
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val cardColors by animateColorAsState(
        if (isPressed && clickable != null) color.fermuxComponents else color.fermuxSurface,
        animationSpec = tween(durationMillis = 150),
        label = "card get color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed && clickable != null && aspectRatio == null) 0.95f else 1.0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "card get bigger when press"
    )


    var cardModifier = modifier
        .padding(cardPadding)
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        cardModifier = if (aspectRatio != null) {
            cardModifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        } else {
            cardModifier
                .heightIn(cardSize ?: 80.dp)
                .widthIn(min = cardExpansion)
        }



    Card(
        onClick = { clickable?.invoke() },
        shape = cardShape,
        modifier = cardModifier,
        colors = CardDefaults.cardColors(
            containerColor = cardColors
        ),
        border = BorderStroke(1.5.dp, color.fermuxPrimaryBorder),
        interactionSource = interactionSource,
    ) {
        content()
    }
}





