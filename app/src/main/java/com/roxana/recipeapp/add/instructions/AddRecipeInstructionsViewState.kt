package com.roxana.recipeapp.add.instructions

data class AddRecipeInstructionsViewState(
    val instructions: List<String> = emptyList(),
    val editingInstruction: String = ""
)
