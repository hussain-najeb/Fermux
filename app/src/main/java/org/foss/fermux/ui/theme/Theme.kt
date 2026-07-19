package org.foss.fermux.ui.theme

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
fun FermuxSurface(
    expanded: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(4.dp),
    color: FermuxColor = FermuxColor(),
    padding: PaddingValues = PaddingValues(0.dp),

    content: @Composable ColumnScope.() -> Unit
) {

    AnimatedVisibility(
        visible = expanded,
        enter = expandVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeIn(),
        exit = shrinkVertically(MaterialTheme.motionScheme.fastSpatialSpec()) + fadeOut()
    ) {
        Surface(
            shape = shape,
            border = BorderStroke(1.5.dp, color.fermuxBorder),
            color = color.fermuxSurface
        ) {
           Column(
             modifier = Modifier.padding(padding)
           ) {
                content()
            }
        }
    }
}


@Composable
fun FermuxButton(
    // Core
    modifier: Modifier = Modifier,
    clickable: () -> Unit,
    color: FermuxColor = FermuxColors,

    // Button shape & size
    buttonSize: Dp = 74.dp,
    buttonShape: Dp = 16.dp,
    buttonPadding: Dp = 10.dp,
    contentPadding: PaddingValues = PaddingValues(horizontal = 22.dp, vertical = 12.dp),

    // Icon
    icon: ImageVector? = null,
    iconSize: Dp = 30.dp,
    iconModifier: Modifier = Modifier,

    // Image
    image: Painter? = null,
    imageSize: Dp = 40.dp,
    imageModifier: Modifier = Modifier,

    // Text
    text: String? = null,
    contentDescription: String? = null,

    // State
    isExpanded: Boolean = false,
    rotation: Float? = null,
)

{

    // For icon rotation
    val rotate by animateFloatAsState(
        if (isExpanded) (rotation ?: 0f) else 0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "icon get rotate"
    )

    // // Expands button width when isExpanded is true, separate from press scaling
    val buttonExpansion by animateDpAsState(
        targetValue = if (isExpanded) 80.dp else 74.dp,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "button get bigger"
    )

    // Button color
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonColors by animateColorAsState(
        if (isPressed) color.buttonPrimaryActive else color.buttonPrimaryInActive,
        animationSpec = tween(durationMillis = 150),
        label = "button get color"
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.12f else 1.0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "button get bigger when press"
    )

    FilledTonalButton(
        modifier = modifier
            .padding(buttonPadding)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .heightIn(buttonSize)
            .widthIn(min = buttonExpansion),

        contentPadding = contentPadding,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColors
        ),
        border = BorderStroke(1.5.dp, color.fermuxBorder),
        shape = RoundedCornerShape(buttonShape),

        onClick =  clickable

    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    tint = if (isPressed) color.activeIcon else color.inActiveIcon,
                    contentDescription = contentDescription,
                    modifier = iconModifier
                        .size(iconSize)
                        .rotate(rotate)
                )
            }

            if (image != null) {
                Icon(
                    painter = image,
                    tint = if (isPressed) color.activeIcon else color.inActiveIcon,
                    contentDescription = contentDescription,
                    modifier = imageModifier
                        .size(imageSize)
                        .rotate(rotate)
                )
            }

            if (text != null) {
                Text(
                    text = text,
                    fontFamily = FontFamily.Default,
                    fontStyle = FontStyle.Normal,
                    fontWeight = FontWeight.Normal,
                    fontSize = 16.sp,
                    color = if (isPressed) color.fermuxTextColorActive else color.fermuxTextColorInActive,
                    modifier = Modifier.padding(start = if (icon != null || image != null) 4.dp else 0.dp)
                )
            }
        }
    }
}