package com.google.mediapipe.examples.llminference.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.google.mediapipe.examples.llminference.R

val PressStart = FontFamily(
    Font(R.font.press_start_regular),  // Regular weight
    Font(R.font.press_start_regular) // Bold weight
)
val koho = FontFamily(
    Font(R.font.koho_regular)  // Regular weight // Bold weight
)
// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = PressStart, // Major Mono Display font family
        fontWeight = FontWeight.Bold,   // Bold weight for the title
        fontSize = 24.sp,              // Adjust size as needed
        lineHeight = 40.sp,
        letterSpacing = 1.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = koho,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
)
