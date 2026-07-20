package org.foss.fermux.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

data class FermuxColor(

    // Fermux button colors
    val buttonPrimaryInActive:      Color    = Color(0xFF303258),
    val buttonPrimaryActive:        Color    = Color(0xFFadc6ff),

    // Fermux global components
    val fermuxSecondaryBorder:      Color    = Color(0xFF20bf6b),
    val fermuxPrimaryBorder:        Color    = Color(0xFF005DFF),
    val fermuxComponents:           Color    = Color(0xFF3C3F68),
    val fermuxBackground:           Color    = Color(0xFF181825),
    val fermuxSurface:              Color    = Color(0xFF1f2034),

    // Fermux icon components
    val activeIcon:                 Color    = Color(0xFF102f60),
    val inActiveIcon:               Color    = Color(0xFF727882),

    // Fermux
    val fermuxTextColorBackground:  Color    = Color(0xFF727882),
    val fermuxTextColorInActive:    Color    = Color(0xFFadc6ff),
    val fermuxTextColorActive:      Color    = Color(0xFF102f60),
    val fermuxTextError:            Color    = Color(0xFFea5054),

    )
val FermuxColors = FermuxColor()