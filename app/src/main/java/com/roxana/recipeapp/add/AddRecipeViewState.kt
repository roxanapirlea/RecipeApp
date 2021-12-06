package com.roxana.recipeapp.add

import com.roxana.recipeapp.domain.CategoryType

data class AddRecipeViewState(
    val title: Title = Title(),
    val categories: List<Category> = emptyList(),
    val portions: PortionsState = PortionsState()
) {
    val isValid = title.isValid && portions.isValid
}

data class Title(val name: String = "", val isValid: Boolean = false)

data class Category(val type: CategoryType, val isSelected: Boolean)

data class PortionsState(
    val value: Short? = null,
    val text: String = "",
    val isValid: Boolean = true
)