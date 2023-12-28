package com.aslansoft.deneme.ui.theme


import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView


private val DarkColorScheme = darkColorScheme(
    primary = Color.Black,
    secondary = Color(255, 255, 255, 255),
    tertiary = Color(244, 67, 54, 255),
    background = Color.White,
    onPrimary = Color(255, 152, 0, 255),
    onBackground = Color(238, 184, 105, 255),
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Color(255, 255, 255, 255),
    tertiary = Color(218, 87, 77, 255),
    onPrimary = Color(52, 168, 83, 255),
    onBackground = Color(153, 230, 173, 255),
    background = Color(76,175,80)
)

@Composable
fun DenemeTheme(
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {


        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}