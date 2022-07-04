package com.roxana.recipeapp.edit.categories

import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.uimodel.UiCategoryType

data class EditRecipeCategoriesViewState(
    val categories: List<CategoryState> = emptyList(),
    val isExistingRecipe: Boolean = false,
    val showSaveDialog: Boolean = false,
    val navigation: Navigation? = null,
)

data class CategoryState(
    val categoryType: UiCategoryType,
    val isSelected: Boolean
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Close : Navigation()
    data class ToPage(val page: PageType) : Navigation()
}
