package com.roxana.recipeapp.add

data class AddRecipeViewState(
    val title: Title = Title()
) {
    val isValid = title.isValid
}

data class Title(val name: String = "", val isValid: Boolean = false)
