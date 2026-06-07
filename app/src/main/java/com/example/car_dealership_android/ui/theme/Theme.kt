package com.example.car_dealership_android.ui.theme

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

private val DarkColorScheme = darkColorScheme(
    primary = Gold500,
    onPrimary = Navy900,
    primaryContainer = Gold200,
    onPrimaryContainer = Navy900,
    secondary = Navy500,
    onSecondary = Steel50,
    tertiary = Gold600,
    background = Navy900,
    onBackground = Steel50,
    surface = Navy800,
    onSurface = Steel50,
    surfaceVariant = Navy700,
    onSurfaceVariant = Steel100,
    error = SoldRed,
    errorContainer = SoldContainer
)

private val LightColorScheme = lightColorScheme(
    primary = Navy700,
    onPrimary = Steel50,
    primaryContainer = Steel100,
    onPrimaryContainer = Navy900,
    secondary = Gold600,
    onSecondary = Navy900,
    tertiary = Gold500,
    background = Steel50,
    onBackground = Navy900,
    surface = Color(0xFFFFFFFF),
    onSurface = Navy900,
    surfaceVariant = Steel100,
    onSurfaceVariant = Steel700,
    error = SoldRed,
    errorContainer = SoldContainer
)

@Composable
fun Car_dealership_androidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
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
