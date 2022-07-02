package com.roxana.recipeapp.home

import com.roxana.recipeapp.uimodel.UiCategoryType

sealed class HomeViewState {
    object Empty : HomeViewState()
    object Loading : HomeViewState()
    data class Content(
        val recipes: List<RecipeState> = listOf(),
        val showFilters: Boolean = false,
        val filtersState: FiltersState = FiltersState(),
        val filtersSelectionCount: Int = 0,
        val query: String = ""
    ) : HomeViewState()
}

data class RecipeState(
    val id: Int = 0,
    val name: String = "",
    val categories: List<UiCategoryType> = emptyList()
)

data class FiltersState(
    val maxTotalTime: Int? = null,
    val maxCookingTime: Int? = null,
    val maxPreparationTime: Int? = null,
    val categories: List<UiCategoryType> = emptyList(),
    val selectedTotalTime: Int? = null,
    val selectedCookingTime: Int? = null,
    val selectedPreparationTime: Int? = null,
    val selectedCategory: UiCategoryType? = null
)

data class FiltersSelection(
    val totalTime: Int? = null,
    val cookingTime: Int? = null,
    val preparationTime: Int? = null,
    val category: UiCategoryType? = null,
    val query: String = ""
)
