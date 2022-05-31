package com.roxana.recipeapp.settings

import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature

sealed class SettingsViewAction

object Back : SettingsViewAction()
data class TemperatureSelected(val temperature: UiTemperature) : SettingsViewAction()
data class MeasuringUnitChanged(
    val unit: UiQuantityType,
    val isChecked: Boolean
) : SettingsViewAction()
object DebugSettingsSelected : SettingsViewAction()
