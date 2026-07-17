package org.foss.fermux.ui.theme

import android.graphics.Color
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
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
    modifier: Modifier? = null,
    border: androidx.compose.ui.graphics.Color = FermuxColors.fermuxBorder,
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
fun FermuxButtons(
    buttonSize: Dp? = null,
    buttonShape: Dp? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 22.dp, vertical = 12.dp),
    icon: ImageVector? = null,
    iconSize: Dp? = null,
    text: String? = null,
    color: FermuxColor = FermuxColor(),
    clickable: (() -> Unit)? = null,
    iconModifier: Modifier? = null,
    modifier: Modifier? = null,
    )

{

// Button color
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val buttonColors by animateColorAsState(
        if (isPressed) color.buttonPrimaryActive else color.buttonPrimaryInActive,
        animationSpec = tween(durationMillis = 150)
    )

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.15f else 1.0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec()
    )

    FilledTonalButton(
        modifier = ( modifier ?: Modifier)
            .padding(10.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .heightIn(buttonSize ?: 74.dp)
            .wrapContentWidth(),

        contentPadding = contentPadding,
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonColors
        ),
        border = BorderStroke(1.5.dp, color.fermuxBorder),
        shape = RoundedCornerShape(buttonShape ?: 16.dp),

        onClick = { clickable?.invoke() }

    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    tint = if (isPressed) color.activeIcon else color.inActiveIcon,
                    contentDescription = null,
                    modifier = (iconModifier ?: Modifier)
                        .size(iconSize ?: 30.dp)
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
                    modifier = Modifier.padding(start = if (icon != null) 4.dp else 0.dp)
                )
            }
        }
    }
}