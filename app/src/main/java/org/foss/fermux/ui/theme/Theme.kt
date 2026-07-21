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
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
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
            modifier = modifier,
            shape = shape,
            border = BorderStroke(1.5.dp, color.fermuxPrimaryBorder),
            color = color.fermuxSurface,
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
    buttonPadding: Dp = 15.dp   ,
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
        targetValue = if (isPressed) 1.11f else 1.0f,
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
        border = BorderStroke(1.5.dp, color.fermuxSecondaryBorder),
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
        targetValue = if (isPressed && clickable != null && aspectRatio == null) 1.07f else 1.0f,
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