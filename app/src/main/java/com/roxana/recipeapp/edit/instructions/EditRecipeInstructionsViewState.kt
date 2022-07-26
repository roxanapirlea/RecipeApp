package com.roxana.recipeapp.edit.instructions

import com.roxana.recipeapp.edit.PageType

data class EditRecipeInstructionsViewState(
    val instructions: List<String> = emptyList(),
    val editingInstruction: String = "",
    val canAddInstruction: Boolean = false,
    val isExistingRecipe: Boolean = false,
    val navigation: Navigation? = null,
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Back : Navigation()
    data class ToPage(val page: PageType, val isExistingRecipe: Boolean) : Navigation()
}
