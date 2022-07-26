package com.roxana.recipeapp.edit.temperature

import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.uimodel.UiTemperature

data class EditRecipeTemperatureViewState(
    val temperature: String = "",
    val temperatureUnit: UiTemperature = UiTemperature.Celsius,
    val isExistingRecipe: Boolean = false,
    val navigation: Navigation? = null,
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Back : Navigation()
    data class ToPage(val page: PageType, val isExistingRecipe: Boolean) : Navigation()
}

fun EditRecipeTemperatureViewState.isValid() =
    temperature.isEmpty() || temperature.toShortOrNull() != null
