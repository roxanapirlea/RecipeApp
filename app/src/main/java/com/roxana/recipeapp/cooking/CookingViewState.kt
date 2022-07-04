package com.roxana.recipeapp.cooking

import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature

data class CookingViewState(
    val title: String = "",
    val portions: Short? = null,
    val selectedPortions: Double = 1.0,
    val ingredients: List<IngredientState> = emptyList(),
    val instructions: List<InstructionState> = emptyList(),
    val comments: List<String> = emptyList(),
    val time: TimeState = TimeState(),
    val temperature: Short? = null,
    val temperatureUnit: UiTemperature? = null,
    val isLoading: Boolean = true
)

data class IngredientState(
    val id: Int,
    val name: String,
    val quantity: Double?,
    val quantityForSelectedPortion: Double?,
    val quantityType: UiQuantityType,
    val isChecked: Boolean = false
)

data class InstructionState(
    val id: Short,
    val instruction: String,
    val isChecked: Boolean = false,
    val isCurrent: Boolean = false
)

data class TimeState(
    val total: Short? = null,
    val cooking: Short? = null,
    val waiting: Short? = null,
    val preparation: Short? = null,
) {
    val isEmpty = total == null && cooking == null && preparation == null && waiting == null
}
