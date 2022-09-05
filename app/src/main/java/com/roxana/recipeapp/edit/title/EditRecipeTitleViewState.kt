package com.roxana.recipeapp.edit.title

import com.roxana.recipeapp.edit.PageType

data class EditRecipeTitleViewState(
    val title: String = "",
    val isExistingRecipe: Boolean = false,
    val showSaveDialog: Boolean = false,
    val navigation: Navigation? = null,
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Close : Navigation()
    data class ToPage(val page: PageType, val isExistingRecipe: Boolean) : Navigation()
}

fun EditRecipeTitleViewState.isValid() = title.isNotEmpty()
