package com.roxana.recipeapp.edit.recap

import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature

data class RecapViewState(
    val title: String = "",
    val categories: List<UiCategoryType> = emptyList(),
    val portions: Short? = null,
    val ingredients: List<IngredientState> = emptyList(),
    val instructions: List<String> = emptyList(),
    val comments: List<String> = emptyList(),
    val time: TimeState = TimeState(),
    val temperature: Short? = null,
    val temperatureUnit: UiTemperature? = null,
    val photoPath: String? = null,
    val isExistingRecipe: Boolean = false,
    val showSaveDialog: Boolean = false,
    val isFetchingError: Boolean = false,
    val shouldClose: Boolean = false,
    val saveResult: SaveResult? = null
)

data class IngredientState(
    val name: String,
    val quantity: Double?,
    val quantityType: UiQuantityType
)

data class TimeState(
    val total: Short? = null,
    val cooking: Short? = null,
    val waiting: Short? = null,
    val preparation: Short? = null,
) {
    val isEmpty = total == null && cooking == null && preparation == null && waiting == null
}

data class SaveResult(val isSuccessful: Boolean)
