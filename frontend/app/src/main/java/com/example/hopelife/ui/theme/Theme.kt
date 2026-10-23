
package com.example.hopelife.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = DeepLilac,
    onPrimary = Color.White,
    primaryContainer = LightLilac,
    onPrimaryContainer = DeepLilac,

    secondary = DeepPink,
    onSecondary = Color.White,
    secondaryContainer = LightPink,
    onSecondaryContainer = Color(0xFF641B43),

    tertiary = Lilac,
    onTertiary = Color.White,

    background = HopeBackground,
    onBackground = HopeText,

    surface = HopeSurface,
    onSurface = HopeText,

    surfaceVariant = PaleLilac,
    onSurfaceVariant = HopeTextSecondary,

    outline = HopeOutline,
    error = ErrorRed,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkLilac,
    onPrimary = Color(0xFF32145F),
    primaryContainer = DeepLilac,
    onPrimaryContainer = Color.White,

    secondary = DarkPink,
    onSecondary = Color(0xFF641B43),
    secondaryContainer = Color(0xFF79345A),
    onSecondaryContainer = Color(0xFFFFD8E9),

    tertiary = DarkLilac,
    onTertiary = Color(0xFF32145F),

    background = DarkBackground,
    onBackground = DarkText,

    surface = DarkSurface,
    onSurface = DarkText,

    surfaceVariant = Color(0xFF352B42),
    onSurfaceVariant = Color(0xFFD0C2DD),

    outline = Color(0xFF9587A7),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

@Composable
fun HopeLifeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
