package com.google.mediapipe.examples.llminference.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define a custom color scheme with a #252525 background
private val PureBlackColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    secondary = Color(0xFF03DAC5),
    tertiary = Color(0xFFFFFFFF),
    background = Color(0xFF252525), // Updated to #252525
    surface = Color(0xFF252525) // Updated to #252525
)

@Composable
fun LLMInferenceTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = PureBlackColorScheme
    val view = LocalView.current
    SideEffect {
        val window = (view.context as Activity).window
        window.statusBarColor = colorScheme.surface.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
