package com.roxana.recipeapp.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

const val IMAGE_COLOR_COUNT = 10

fun getIntermediateColors(colorStart: Color, colorEnd: Color, colorCount: Int = 10): List<Color> {
    val alphaStart = colorStart.alpha
    val alphaEnd = colorEnd.alpha
    val redStart = colorStart.red
    val redEnd = colorEnd.red
    val greenStart = colorStart.green
    val greenEnd = colorEnd.green
    val blueStart = colorStart.blue
    val blueEnd = colorEnd.blue
    return IntRange(1, colorCount).map {
        val alpha = (alphaEnd - alphaStart) / (colorCount + 1) * it + alphaStart
        val red = (redEnd - redStart) / (colorCount + 1) * it + redStart
        val green = (greenEnd - greenStart) / (colorCount + 1) * it + greenStart
        val blue = (blueEnd - blueStart) / (colorCount + 1) * it + blueStart
        Color(red, green, blue, alpha)
    }
}

@Composable
fun getPrimarySecondaryColors() = getIntermediateColors(
    MaterialTheme.colors.primary,
    MaterialTheme.colors.secondary,
    IMAGE_COLOR_COUNT / 2
) + getIntermediateColors(
    MaterialTheme.colors.primary,
    MaterialTheme.colors.secondary,
    IMAGE_COLOR_COUNT / 2
).reversed()

@Composable
fun getPrimarySecondaryColor(index: Int) = getPrimarySecondaryColors()[index % IMAGE_COLOR_COUNT]
