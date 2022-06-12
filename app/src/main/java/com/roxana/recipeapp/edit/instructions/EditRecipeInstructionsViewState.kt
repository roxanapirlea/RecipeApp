package com.roxana.recipeapp.edit.instructions

data class EditRecipeInstructionsViewState(
    val instructions: List<String> = emptyList(),
    val editingInstruction: String = "",
    val isExistingRecipe: Boolean = false,
    val showSaveDialog: Boolean = false,
)
