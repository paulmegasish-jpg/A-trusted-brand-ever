package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = RosePrimaryDark,
    onPrimary = Color(0xFF5E002B),
    primaryContainer = Color(0xFF830C3F),
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = RoseSecondaryDark,
    onSecondary = Color(0xFF431B31),
    secondaryContainer = Color(0xFF5B3047),
    onSecondaryContainer = Color(0xFFFFD7E9),
    tertiary = ChampagneGoldDark,
    onTertiary = Color(0xFF432C00),
    tertiaryContainer = Color(0xFF604100),
    onTertiaryContainer = Color(0xFFFFDE9F),
    background = DarkBackground,
    onBackground = TextPrimaryDark,
    surface = DarkSurface,
    onSurface = TextPrimaryDark,
    surfaceVariant = DarkCard,
    onSurfaceVariant = TextSecondaryDark,
    outline = Color(0xFF755160)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = RosePrimary,
    onPrimary = Color.White,
    primaryContainer = SoftBlushCard,
    onPrimaryContainer = Color(0xFF5E002B),
    secondary = RoseSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFD7E9),
    onSecondaryContainer = Color(0xFF3B1529),
    tertiary = ChampagneGold,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFDE9F),
    onTertiaryContainer = Color(0xFF2B1B00),
    background = SoftBlushBg,
    onBackground = TextPrimaryLight,
    surface = SoftBlushSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = SoftBlushCard,
    onSurfaceVariant = TextSecondaryLight,
    outline = Color(0xFFE5C5D2)
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Pinned to branded palette for consistent boutique identity
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
