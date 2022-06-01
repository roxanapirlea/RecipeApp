package com.roxana.recipeapp.edit.categories

import com.roxana.recipeapp.uimodel.UiCategoryType

data class EditRecipeCategoriesViewState(
    val categories: List<CategoryState> = emptyList()
)

data class CategoryState(
    val categoryType: UiCategoryType,
    val isSelected: Boolean
)
