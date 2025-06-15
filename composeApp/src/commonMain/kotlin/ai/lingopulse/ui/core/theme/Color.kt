package ai.lingopulse.ui.core.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

// Newspaper-style color scheme
val PrimaryColor = Color.Black
val AccentColor = Color(0xFF757575) // Medium grey

// Background Colors
val LightBackgroundColor = Color(0xFFFAFAFA)
val DarkBackgroundColor =  Color(0xFF202020)

// Toolbar Colors
val LightToolbarColor = Color(0xFFFAFAFA)
val DarkToolbarColor = Color(0xFF202020)

// Text Colors (newspaper style)
val DarkTextPrimaryColor = Color.White
val LightTextPrimaryColor = Color.Black

val DarkTextSecondaryColor = Color(0xFFB0B0B0) // Light grey for dark mode
val LightTextSecondaryColor = Color(0xFF505050) // Dark grey for light mode

val DarkPlaceholderTextColor = Color(0xFF808080) // Medium grey for dark mode
val LightPlaceholderTextColor = Color(0xFF909090) // Lighter grey for light mode

val ShimmerEffectColor = Color(0x4DBFC1C3) // Very light grey

val TextPrimaryColor: Color
    @Composable get() = if (isSystemInDarkTheme()) DarkTextPrimaryColor else LightTextPrimaryColor

val TextSecondaryColor: Color
    @Composable get() = if (isSystemInDarkTheme()) DarkTextSecondaryColor else LightTextSecondaryColor

val PlaceholderTextColor: Color
    @Composable get() = if (isSystemInDarkTheme()) DarkPlaceholderTextColor else LightPlaceholderTextColor

val BackgroundColor: Color
    @Composable get() = if (isSystemInDarkTheme()) DarkBackgroundColor else LightBackgroundColor

val ToolbarColor: Color
    @Composable get() = if (isSystemInDarkTheme()) DarkToolbarColor else LightToolbarColor

// Bubble Colors (newspaper style)
val UserBubbleBrush = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF2C2C2C), // Dark grey
        Color(0xFF1A1A1A)  // Darker grey
    )
)

val RainbowColor = arrayOf(
    0.0f to Color(0xFF000000), // Black
    0.1f to Color(0xFF1A1A1A), // Very dark grey
    0.2f to Color(0xFF333333), // Dark grey
    0.3f to Color(0xFF4D4D4D), // Medium dark grey
    0.4f to Color(0xFF666666), // Medium grey
    0.5f to Color(0xFF808080), // Grey
    0.6f to Color(0xFF999999), // Light grey
    0.7f to Color(0xFFB3B3B3), // Lighter grey
    0.8f to Color(0xFFCCCCCC), // Very light grey
    0.9f to Color(0xFFE6E6E6), // Almost white
    1.0f to Color(0xFFFFFFFF)  // White
)

val AiBubbleBrush: Brush
    @Composable get() = if (isSystemInDarkTheme()) {
        Brush.verticalGradient(
            colors = listOf(
                Color.Black, // White
                Color.Black  // White
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color.White, // White
                Color.White  // White
            )
        )
    }

val SystemBubbleBrush: Brush
    @Composable get() = if (isSystemInDarkTheme()) {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFF1A1A1A), // Very dark grey
                Color(0xFF0D0D0D)  // Almost black
            )
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                Color(0xFFE8E8E8), // Light grey
                Color(0xFFDDDDDD)  // Slightly darker light grey
            )
        )
    }
