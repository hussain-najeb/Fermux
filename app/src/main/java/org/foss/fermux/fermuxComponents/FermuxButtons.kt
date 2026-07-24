package org.foss.fermux.fermuxComponents

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.foss.fermux.R
import org.foss.fermux.ui.theme.FermuxColor
import org.foss.fermux.ui.theme.FermuxColors



// TODO. Add documentation like the other button to this one and the other as well
@Composable
fun FermuxMainActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    image: Painter? = null,
    iconRotation: Float = 0f,
    componentSize: Dp = 32.dp,
    color: FermuxColor = FermuxColors,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor by animateColorAsState(
        targetValue = if (isPressed) color.fermuxActiveButton else color.fermuxInActiveButton,
        animationSpec = tween(200),
        label = "Fermux Container Color",
    )
    val iconColor by animateColorAsState(
        targetValue = if (isPressed) color.fermuxActiveIcon else color.fermuxInActiveIcon,
        animationSpec = tween(150),
        label = "Fermux Icon Color"
    )
    val buttonAnimation by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1.0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "Fermux Button Animation"
    )
    val iconRotate by animateFloatAsState(
        targetValue = iconRotation,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "Fermux Icon Rotation"
    )

    val iconModifier = Modifier.rotate(iconRotate).size(componentSize)

    FilledTonalButton(
        modifier = modifier.graphicsLayer {
            scaleX = buttonAnimation
            scaleY = buttonAnimation
        },
        enabled = enabled,
        colors = ButtonDefaults.filledTonalButtonColors(
            containerColor = containerColor,
            contentColor = iconColor,
        ),
        interactionSource = interactionSource,
        contentPadding = PaddingValues(10.dp),
        border = BorderStroke(width = 1.dp, color = color.fermuxSecondaryBorder),
        shape = RoundedCornerShape(8.dp),
        onClick = onClick,
    ) {
        when {
            icon != null -> Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = iconModifier
            )
            image != null -> Icon(
                painter = image,
                contentDescription = null,
                tint = iconColor,
                modifier = iconModifier
            )
        }
    }
}


/**
 * A flexible Fermux-styled text button that supports an optional icon and/or text label.
 *
 * Handles all press animation, color transitions, and icon rotation internally —
 * the caller only needs to provide target values, not animation logic.
 *
 * @param modifier Applied to the outer [TextButton]. Use this to control size and positioning.
 * @param text Optional label rendered to the right of the icon. Pass null to hide.
 * @param contentPadding Internal padding between the button edge and its content.
 * @param icon Optional leading icon. Pass null to hide.
 * @param iconRotation Target rotation angle for the icon in degrees. The button animates
 * to this value automatically — useful for expand/collapse chevrons. Defaults to 0f (no rotation).
 * @param buttonRoundness Corner radius of the button shape. Defaults to 16.dp if not provided.
 * @param color Fermux color scheme. Defaults to [FermuxColors].
 * @param enabled Whether the button is interactive. When false, the button dims and
 * blocks clicks — the caller controls when and how long this lasts.
 * @param onClick Called when the button is tapped.
 */

@Composable
fun FermuxTextWithIconButton(
    modifier: Modifier = Modifier,
    text: String? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    icon: ImageVector? = null,
    buttonRoundness: Dp? = null,
    contentDescription: String? = null,
    iconRotation: Float = 0f,
    color: FermuxColor = FermuxColors,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor by animateColorAsState(
        targetValue = when {
            !enabled -> color.fermuxBackground
            isPressed -> color.fermuxActiveButton
            else -> color.fermuxInActiveButton
        },
        animationSpec = tween(200),
        label = "Fermux Button Colors",
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            !enabled -> color.fermuxTextColorBackground
            isPressed -> color.fermuxActiveTextColor
            else -> color.fermuxInActiveTextColor
        },
        animationSpec = tween(200),
        label = "Fermux Text Colors",
    )

    val iconColor by animateColorAsState(
        targetValue = when {
            !enabled -> color.fermuxTextColorBackground
            isPressed -> color.fermuxActiveIcon
            else -> color.fermuxInActiveIcon
        },
        animationSpec = tween(durationMillis = 150),
        label = "Fermux Icon Colors"
    )

    val buttonAnimation by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1.0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "Fermux Button Animation"
    )

    val iconRotate by animateFloatAsState(
        targetValue = iconRotation,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "Fermux Icon Rotation"
    )

    ElevatedButton(
        modifier = modifier.graphicsLayer {
            scaleX = buttonAnimation
            scaleY = buttonAnimation
        },
        contentPadding = contentPadding,
        enabled = enabled,
        shape = RoundedCornerShape(buttonRoundness ?: 16.dp),
        colors = ButtonDefaults.textButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = onClick,
        interactionSource = interactionSource,
        border = BorderStroke(width = 1.dp, color = color.fermuxGenericBorder)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon, contentDescription = contentDescription,
                    tint = iconColor,
                    modifier = Modifier.rotate(iconRotate)
                )
            }
            if (text != null) {
                Text(text, modifier = Modifier.padding(start = if (icon != null) 5.dp else 0.dp))
            }
        }
    }
}




@Composable
fun FermuxImageButton(
    modifier: Modifier = Modifier,
    image: Painter,
    contentDescription: String? = null,
    iconRotation: Float = 0f,
    color: FermuxColor = FermuxColors,
    contentPadding: PaddingValues = PaddingValues(4.dp),
    onClick: () -> Unit
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val containerColor by animateColorAsState(
        targetValue = when {
            isPressed -> color.fermuxActiveButton
            else -> color.fermuxInActiveButton
        },
        animationSpec = tween(200),
        label = "Fermux Button Colors",
    )

    val contentColor by animateColorAsState(
        targetValue = when {
            isPressed -> color.fermuxActiveTextColor
            else -> color.fermuxInActiveTextColor
        },
        animationSpec = tween(200),
        label = "Fermux Text Colors",
    )

    val iconColor by animateColorAsState(
        targetValue = when {
            isPressed -> color.fermuxActiveIcon
            else -> color.fermuxInActiveIcon
        },
        animationSpec = tween(durationMillis = 150),
        label = "Fermux Icon Colors"
    )

    val buttonAnimation by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1.0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "Fermux Button Animation"
    )

    val iconRotate by animateFloatAsState(
        targetValue = iconRotation,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "Fermux Icon Rotation"
    )

    ElevatedButton(
        modifier = modifier.graphicsLayer {
            scaleX = buttonAnimation
            scaleY = buttonAnimation
        }
            .padding(5.dp),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color = color.fermuxSecondaryBorder),
        colors = ButtonDefaults.textButtonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = contentPadding,
        interactionSource = interactionSource,
         onClick = onClick
    ) {
        Icon(
            painter = image,
            tint = iconColor,
            contentDescription = contentDescription,
            modifier = modifier.rotate(iconRotate)
        )
    }
}





@Composable
fun FermuxCancelButton(
    modifier: Modifier = Modifier,
    iconRotation: Float = 0f,
    componentSize: Dp = 32.dp,
    color: FermuxColor = FermuxColors,
    onClick: () -> Unit,
) {

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val buttonAnimation by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1.0f,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "Fermux Button Animation"
    )

    val iconColor by animateColorAsState(
        targetValue = if (isPressed) color.fermuxTextError else color.fermuxInActiveIcon,
        animationSpec = tween(150),
        label = "Fermux Icon Color"
    )

    val iconRotate by animateFloatAsState(
        targetValue = iconRotation,
        animationSpec = MaterialTheme.motionScheme.fastSpatialSpec(),
        label = "Fermux Icon Rotation"
    )

    val iconModifier = Modifier.rotate(iconRotate).size(componentSize)


    OutlinedButton(
        modifier = modifier
            .graphicsLayer {
                scaleX = buttonAnimation
                scaleY = buttonAnimation
            },

        interactionSource = interactionSource,
        contentPadding = PaddingValues(10.dp),
        border = BorderStroke(width = 1.dp, color = color.fermuxTertiaryBorder),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Default.Cancel,
            contentDescription = null,
            tint = iconColor,
            modifier = iconModifier
        )
    }
}




// Example for how the buttons will look like
@Preview
@Composable
fun Why() {
    var pressed by remember { mutableStateOf(false) } // To give the button an On/Off state.
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FermuxTextWithIconButton(
            modifier = Modifier.defaultMinSize(minWidth = 70.dp), // To decide how big the button is.
            contentPadding = PaddingValues(9.dp), // To give text and icon a room in the button.
            icon = Icons.Default.ExpandMore, // Optional icon.
            iconRotation = if (pressed) 180f else 0f, // Optional icon animation.
            text = if (pressed) "show" else "hide", // Optional text given by caller for the button.
            onClick = { pressed = !pressed } // onClick for executing the desired effect.
        )

        Spacer(Modifier.height(20.dp))

        FermuxMainActionButton(
            modifier = Modifier.size(70.dp),
            image = painterResource(R.drawable.ic_launcher_background),
            componentSize = 50.dp, // The biggest the image can get.
            iconRotation = if (pressed) 180f else 0f,
            onClick = {pressed = !pressed },
        )

        Spacer(Modifier.height(20.dp))

        FermuxCancelButton(
            modifier = Modifier.size(40.dp),
            iconRotation = if (pressed) 360f else 0f,
            onClick = {pressed = !pressed },
        )

    }
}