package org.foss.fermux.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)
val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

data class FermuxColor( // TODO. edit the bad naming on these

     // Fermux button colors
     val fermuxInActiveButton: Color = Color(0xFF303258),
     val fermuxActiveButton: Color = Color(0xFFadc6ff),
     val fermuxInActiveBackButton: Color = Color(0xFF353749),
     val fermuxRedDeleteColorInActive: Color = Color(0xFFF8504E),
     val fermuxRedDeleteColorActive: Color = Color(0xFFf06866),

     // Fermux Segmented buttons
     val activeContainer: Color = Color(0xFF9baede),
     val activeContent: Color = Color(0xFF102f60),
     val inActiveContainer: Color = Color(0xFF30325d),
     val inActiveContent: Color = Color.White,

     // Fermux Borders
     val fermuxPrimaryBorder: Color = Color(0xFF005DFF),
     val fermuxSecondaryBorder: Color = Color(0xFF67ECA2),
     val fermuxGenericBorder: Color = Color(0xFF7979FC),
     val fermuxTertiaryBorder: Color = Color(0xFF3B3B40),
     val fermuxHelperBorder: Color = Color(0xFF6B6B9E),

     // Fermux global components
     val fermuxComponents: Color = Color(0xFF3C3F68),
     val fermuxSaturatedComponents: Color = Color(0xFF22243E),
     val something3: Color = Color(0xFF2D2F49),
     val fermuxBackground: Color = Color(0xFF181825),
     val fermuxSurface: Color = Color(0xFF1f2034),
     val fermuxErrorCardColor: Color = Color(0xFF8c1d18),
     val inActiveTextField: Color = Color(0xFF474968),
     val mainCardPrimary: Color = Color(0xFF282c34),
     val mainCardSecondary: Color = Color(0xFF202329),
     val deepBlue: Color = Color(0xFF3148d6),

     val skyBright: Color = Color(0xFFb9c2ff),
     val skyBrightDark: Color = Color(0xFF8494f8),
     val something: Color = Color(0xFF3c4257),
     val deepDarkBlue: Color = Color(0xFF19212c),
     val darkBlue: Color = Color(0xFF293444),
     val gray: Color = Color(0xFF3d444f),

     // Fermux FFmpeg crad colors
     val fermuxFFmpegGreen: Color = Color(0xFF388e3c),


     // Fermux switch Colors
     val fermuxThumbOn: Color = Color(0xFF40407F),
     val fermuxThumbOff: Color = Color(0xFF848489),
     val fermuxTrackOn: Color = Color(0xFFB9B9FA),
     val fermuxTrackOff: Color = Color(0xFF393636),

     // Fermux icon components
     val fermuxActiveIcon: Color = Color(0xFF102f60),
     val fermuxInActiveIcon: Color = Color(0xFF9FAAB6),

     // Fermux slider
     val activeSliderColor: Color = Color(0xFF4D7DE5),
     val inActiveSliderColor: Color = Color(0xFFB5C1E8),

     // Fermux text
     val fermuxWhiteColor: Color = Color.White,
     val fermuxTextColorBackground: Color = Color(0xFF727882),
     val fermuxOffWhiteTextColor: Color = Color(0xFFC2C6C6),
     val fermuxLightErrorTextColor: Color = Color(0xFFf2b8b5),
     val fermuxInActiveTextColor: Color = Color(0xFFadc6ff),
     val fermuxActiveTextColor: Color = Color(0xFF102f60),
     val fermuxTextError: Color = Color(0xFFea5054),
     val fermuxBackgroundTextColor: Color = Color(0xFF45455A)
)

val FermuxColors = FermuxColor()


