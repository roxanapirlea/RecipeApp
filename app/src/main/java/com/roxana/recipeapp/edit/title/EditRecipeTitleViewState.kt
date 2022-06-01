package com.roxana.recipeapp.edit.title

data class EditRecipeTitleViewState(
    val title: String = "",
    val showSaveDialog: Boolean = false
)

fun EditRecipeTitleViewState.isValid() = title.isNotEmpty()
