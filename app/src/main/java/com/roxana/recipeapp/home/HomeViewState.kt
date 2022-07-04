package com.roxana.recipeapp.home

import com.roxana.recipeapp.uimodel.UiCategoryType

data class HomeViewState(
    val recipes: List<RecipeState> = listOf(),
    val showFilters: Boolean = false,
    val filtersState: FiltersState = FiltersState(),
    val filtersSelectionCount: Int = 0,
    val query: String = "",
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
    val isFetchingError: Boolean = false
)

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
