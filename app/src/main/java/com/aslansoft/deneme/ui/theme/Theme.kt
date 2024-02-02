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
    secondary = Color(255, 255, 255, 255), //beyaz
    tertiary = Color(10, 255, 15), // kırmızı
    background = Color.White,
    onPrimary = Color(0xff567962), //yeşil
    onBackground = Color(0xffb0af80), //sarı
    onSecondary = Color(0xff7699a4) // mavi
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Color(186, 203, 196, 255), //
    tertiary = Color(218, 87, 77, 255), //kırmızı
    onPrimary = Color(114, 197, 118, 255), //yeşil
    onBackground = Color(169, 163, 118, 255), // açık yeşil
    background = Color.Black,   //  Koyu yeşil
    onSecondary = Color(115,210,210)
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