package com.example.alquilerapp.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme // Usamos ColorScheme para Material 3
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),      // Púrpura, común en temas oscuros
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC5),
    // Puedes añadir más colores aquí (tertiary, background, surface, etc.)
)

/**
 * Composable que define el tema de la aplicación.
 *
 */
@Composable
fun AlquilerTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> DarkColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}