package com.roxana.recipeapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Apricot,
    primaryVariant = SantaFe,
    onPrimary = Color.White,
    secondary = Buttermilk,
    onSecondary = Color.Black,
    error = Color.Red
)

private val LightColorPalette = lightColors(
    primary = Rosewood,
    primaryVariant = Temptress,
    onPrimary = Color.White,
    secondary = BrightSun,
    secondaryVariant = BrightSun,
    onSecondary = Color.Black,
    surface = Buttermilk,
    error = Color.Red
)

@Composable
fun RecipeTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = RecipesTypography,
        shapes = RecipesShapes,
        content = content
    )
}
