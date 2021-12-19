package com.roxana.recipeapp.home

import com.roxana.recipeapp.domain.model.CategoryType

sealed class HomeViewState {
    object Empty : HomeViewState()
    data class Content(
        val recipes: List<RecipeState> = listOf()
    ) : HomeViewState()
}

data class RecipeState(
    val id: Int = 0,
    val name: String = "",
    val categories: List<CategoryType> = emptyList()
)
