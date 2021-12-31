package com.roxana.recipeapp.settings

import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature

data class SettingsViewState(
    val temperatures: List<UiTemperature> = listOf(UiTemperature.Celsius, UiTemperature.Fahrenheit),
    val selectedTemperature: UiTemperature = UiTemperature.Celsius,
    val measuringUnits: List<MeasuringUnit> = emptyList()
)

data class MeasuringUnit(
    val unit: UiQuantityType,
    val isChecked: Boolean = true
)
