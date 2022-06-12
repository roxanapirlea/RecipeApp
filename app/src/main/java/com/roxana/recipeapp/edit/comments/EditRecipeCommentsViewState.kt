package com.roxana.recipeapp.edit.comments

data class EditRecipeCommentsViewState(
    val comments: List<String> = emptyList(),
    val editingComment: String = "",
    val isExistingRecipe: Boolean = false,
    val showSaveDialog: Boolean = false,
)
