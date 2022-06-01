package com.roxana.recipeapp.home.filters

import com.roxana.recipeapp.uimodel.UiCategoryType

data class FiltersViewState(
    val maxTotalTime: Int? = null,
    val maxCookingTime: Int? = null,
    val maxPreparationTime: Int? = null,
    val categories: List<UiCategoryType> = emptyList(),
    val selectedTotalTime: Int? = null,
    val selectedCookingTime: Int? = null,
    val selectedPreparationTime: Int? = null,
    val selectedCategory: UiCategoryType? = null,
)
