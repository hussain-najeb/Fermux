package org.foss.fermux.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

data class FermuxColor(

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

    // Fermux FFmpeg card colors
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


@Preview(showBackground = true, backgroundColor = 0xFF3d444f)
@Composable
fun test25() {
    val colorList = listOf(
        Color(0xFF19212c),
        Color(0xFFb9c2ff),
        Color(0xFF8494f8),
        Color(0xFF293444),
        Color(0xFF3148d6),
        Color(0xFF2D2F49),
        Color(0xFF102f60),
        Color(0xFF15152e)
    )
    Column(
        modifier = Modifier
    ) {
        colorList.forEach { color ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {

                Text(
                    color = color,
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam vel neque iaculis, molestie elit at, laoreet tortor.",
                    modifier = Modifier.fillMaxWidth().size(50.dp),
                    overflow = TextOverflow.Visible
                )
            }
        }
    }
}