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
    val fermuxInActiveButton:      Color    = Color(0xFF303258),
    val fermuxActiveButton:        Color    = Color(0xFFadc6ff),

    // Fermux global components
    val fermuxPrimaryBorder:        Color    = Color(0xFF005DFF),
    val fermuxSecondaryBorder:      Color    = Color(0xFF67ECA2),
    val fermuxGenericBorder:        Color    = Color(0xFF7E7EF6),
    val fermuxTertiaryBorder:       Color    = Color(0xFF3B3B40),
    val fermuxComponents:           Color    = Color(0xFF3C3F68),
    val fermuxBackground:           Color    = Color(0xFF181825),
    val fermuxSurface:              Color    = Color(0xFF1f2034),

    // Fermux icon components
    val fermuxActiveIcon:           Color    = Color(0xFF102f60),
    val fermuxInActiveIcon:         Color    = Color(0xFF727882),

    // Fermux text
    val fermuxTextColorBackground:  Color    = Color(0xFF727882),
    val fermuxInActiveTextColor:    Color    = Color(0xFFadc6ff),
    val fermuxActiveTextColor:      Color    = Color(0xFF102f60),
    val fermuxTextError:            Color    = Color(0xFFea5054),

    )
val FermuxColors = FermuxColor()