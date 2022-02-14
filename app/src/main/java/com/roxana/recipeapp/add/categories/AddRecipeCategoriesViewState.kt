package com.roxana.recipeapp.add.categories

import com.roxana.recipeapp.uimodel.UiCategoryType

data class AddRecipeCategoriesViewState(
    val categories: List<CategoryState> = emptyList()
)

data class CategoryState(
    val categoryType: UiCategoryType,
    val isSelected: Boolean
)
