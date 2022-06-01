package com.roxana.recipeapp.edit.title

data class EditRecipeTitleViewState(
    val title: String = "",
    val isExistingRecipe: Boolean = false,
    val showSaveDialog: Boolean = false
)

fun EditRecipeTitleViewState.isValid() = title.isNotEmpty()
