package com.roxana.recipeapp.home

import com.roxana.recipeapp.uimodel.UiCategoryType

sealed class HomeViewState {
    object Empty : HomeViewState()
    object Loading : HomeViewState()
    data class Content(
        val recipes: List<RecipeState> = listOf()
    ) : HomeViewState()
}

data class RecipeState(
    val id: Int = 0,
    val name: String = "",
    val categories: List<UiCategoryType> = emptyList()
)
