package com.roxana.recipeapp.edit.temperature

import com.roxana.recipeapp.uimodel.UiTemperature

data class EditRecipeTemperatureViewState(
    val temperature: String = "",
    val temperatureUnit: UiTemperature = UiTemperature.Celsius,
    val isExistingRecipe: Boolean = false,
    val showSaveDialog: Boolean = false,
)

fun EditRecipeTemperatureViewState.isValid() =
    temperature.isEmpty() || temperature.toShortOrNull() != null
