package org.foss.fermux.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.foss.fermux.R

// Set of Material typography styles to start with

val JetbrainsMono = FontFamily(
     Font(
          resId = R.font.jetbrainsmono_regular,
          weight = FontWeight.Normal
     ),
     Font(
          resId = R.font.jetbrainsmono_bold,
          weight = FontWeight.Bold
     )
)

val GoogleSans = FontFamily(
     Font(
          resId = R.font.google_sans_regular,
          weight = FontWeight.Normal
     ),
     Font(
          resId = R.font.google_sans_semibold,
          weight = FontWeight.SemiBold
     ),
     Font(
          resId = R.font.google_sans_17pt_italic,
          weight = FontWeight.Normal,
          style = FontStyle.Italic
     ),
     Font(
          resId = R.font.google_sans_17pt_medium_italic,
          weight = FontWeight.Medium,
          style = FontStyle.Italic
     )
)

val Typography = Typography(
     bodyLarge = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal,
          fontSize = 16.sp,
          lineHeight = 24.sp,
          letterSpacing = 0.5.sp
     ),
     bodyMedium = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),
     bodySmall = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),

     titleLarge = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),
     titleMedium = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),
     titleSmall = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),

     headlineLarge = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),
     headlineMedium = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),
     headlineSmall = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),

     displayLarge = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),
     displayMedium = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),
     displaySmall = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),

     labelLarge = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),
     labelMedium = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     ),
     labelSmall = TextStyle(
          fontFamily = GoogleSans,
          fontWeight = FontWeight.Normal
     )
)

















