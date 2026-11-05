package com.example.vbtfatiguemonitor.ui.theme

import android.app.Activity
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

// Esquema para el Modo Oscuro
private val DarkColorScheme = darkColorScheme(
    primary = AccentLime,
    onPrimary = Color.Black,
    secondary = AccentOrange,
    tertiary = AccentCoral,
    background = AppBackgroundDark,
    surface = DarkSurface,
    onBackground = TextPrimaryDark,
    onSurface = TextPrimaryDark
)

// Esquema para el Modo Claro
private val LightColorScheme = lightColorScheme(
    primary = AccentLime,          // El verde lima se mantiene como acento principal
    onPrimary = Color.Black,       // Texto negro sobre el verde lima para contraste
    secondary = AccentOrange,
    tertiary = AccentCoral,
    background = LightBackground,  // Fondo claro
    surface = LightSurface,        // Tarjetas blancas
    onBackground = TextPrimaryLight,
    onSurface = TextPrimaryLight
)

@Composable
fun VBTFatigueMonitorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Se desactiva el color dinámico para usar siempre la paleta personalizada.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
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