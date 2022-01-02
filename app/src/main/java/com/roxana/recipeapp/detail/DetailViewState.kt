package com.roxana.recipeapp.detail

import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.UiTemperature

sealed class DetailViewState {
    object Loading : DetailViewState()
    data class Content(
        val title: String = "",
        val categories: List<UiCategoryType> = emptyList(),
        val portions: Short? = null,
        val ingredients: List<IngredientState> = emptyList(),
        val instructions: List<String> = emptyList(),
        val comments: List<String> = emptyList(),
        val time: TimeState = TimeState(),
        val temperature: Short? = null,
        val temperatureUnit: UiTemperature? = null
    ) : DetailViewState()
}

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
