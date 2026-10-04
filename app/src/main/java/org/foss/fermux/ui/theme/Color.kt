package org.foss.fermux.ui.theme

import androidx.compose.ui.graphics.Color

data class FermuxColor( // TODO. edit the bad naming on these

     // Fermux button colors
     val fermuxInActiveButton: Color = Color(0xFF303258),
     val fermuxActiveButton: Color = Color(0xFFadc6ff),
     val fermuxInActiveBackButton: Color = Color(0xFF353749),
     val fermuxRedDeleteColorInActive: Color = Color(0xFFF8504E),
     val fermuxRedDeleteColorActive: Color = Color(0xFFf06866),

     // Fermux Segmented buttons
     val inActiveContainer: Color = Color(0xFF30325d),

     // Fermux Borders
     val fermuxPrimaryBorder: Color = Color(0xFF005DFF),
     val fermuxSecondaryBorder: Color = Color(0xFF67ECA2),
     val fermuxGenericBorder: Color = Color(0xFF7979FC),
     val fermuxTertiaryBorder: Color = Color(0xFF3B3B40),
     val fermuxHelperBorder: Color = Color(0xFF6B6B9E),

     // Fermux global components
     val fermuxSurface: Color = Color(0xFF1f2034),
     val fermuxErrorCardColor: Color = Color(0xFF8c1d18),
     val inActiveTextField: Color = Color(0xFF474968),
     val mainCardPrimary: Color = Color(0xFF282c34),
     val mainCardSecondary: Color = Color(0xFF202329),

     // Gray variations
     val gray: Color = Color(0xFF3d444f),
     val warmGray: Color = Color(0xFF3c4257),
     val transparent: Color = Color.Transparent,

     // Fermux FFmpeg crad colors
     val fermuxFFmpegGreen: Color = Color(0xFF388e3c),

     // Purple variations
     val warmPurple: Color = Color(0xFF3C3F68),
     val darkPurple: Color = Color(0xFF22243E),

     // Blue variations
     val mutedDarkBlue: Color = Color(0xFF19212c),
     val skyBlue: Color = Color(0xFFb9c2ff),
     val warmBlue: Color = Color(0xFF8494f8),
     val darkBlue: Color = Color(0xFF293444),
     val deepBlue: Color = Color(0xFF3148d6),
     val mutedBlue: Color = Color(0xFF2D2F49),
     val downriver: Color = Color(0xFF102f60),
     val background: Color = Color(0xFF15152e),

     // Switch colors
     val fermuxThumbOn: Color = Color(0xFF40407F),
     val fermuxTrackOn: Color = Color(0xFF2e2edc),

     // Slider color
     val activeSliderColor: Color = Color(0xFF4D7DE5),
     val inActiveSliderColor: Color = Color(0xFFB5C1E8),

     // Text
     val white: Color = Color.White,
     val offWhiteTextColor: Color = Color(0xFFC2C6C6),
     val fermuxLightErrorTextColor: Color = Color(0xFFf2b8b5),
     val fermuxTextError: Color = Color(0xFFea5054),
)

val FermuxColors = FermuxColor()
// TODO. Spilt each color to its own class, so its better organized

