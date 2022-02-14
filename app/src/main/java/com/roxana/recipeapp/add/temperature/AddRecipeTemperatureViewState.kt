package com.roxana.recipeapp.add.temperature

import com.roxana.recipeapp.uimodel.UiTemperature

data class AddRecipeTemperatureViewState(
    val temperature: String = "",
    val temperatureUnit: UiTemperature = UiTemperature.Celsius
)

fun AddRecipeTemperatureViewState.isValid() =
    temperature.isEmpty() || temperature.toShortOrNull() != null
