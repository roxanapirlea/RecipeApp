package com.roxana.recipeapp.add

import com.roxana.recipeapp.domain.CategoryType

data class AddRecipeViewState(
    val title: Title = Title(),
    val categories: List<Category> = emptyList()
) {
    val isValid = title.isValid
}

data class Title(val name: String = "", val isValid: Boolean = false)

data class Category(val type: CategoryType, val isSelected: Boolean)