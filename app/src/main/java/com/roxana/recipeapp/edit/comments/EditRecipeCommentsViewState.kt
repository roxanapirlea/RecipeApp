package com.roxana.recipeapp.edit.comments

import com.roxana.recipeapp.edit.PageType

data class EditRecipeCommentsViewState(
    val comments: List<String> = emptyList(),
    val editingComment: String = "",
    val canAddEditingComment: Boolean = false,
    val isExistingRecipe: Boolean = false,
    val navigation: Navigation? = null,
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Back : Navigation()
    data class ToPage(val page: PageType, val isExistingRecipe: Boolean) : Navigation()
}
