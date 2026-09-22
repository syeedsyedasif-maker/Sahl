// SPDX-License-Identifier: AGPL-3.0-or-later
// Copyright (C) 2026 Sahl contributors

package com.sahl.app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = Primary,
    secondary = Secondary,
    tertiary = Tertiary,
)

private val DarkColors = darkColorScheme(
    primary = PrimaryDark,
    secondary = SecondaryDark,
    tertiary = TertiaryDark,
)

@Composable
fun SahlTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Material You wallpaper colors, available on Android 12+.
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SahlTypography,
        content = content,
    )
}
