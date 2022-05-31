package com.roxana.recipeapp.add.title

data class AddRecipeTitleViewState(
    val title: String = "",
    val showSaveDialog: Boolean = false
)

fun AddRecipeTitleViewState.isValid() = title.isNotEmpty()
