package com.roxana.recipeapp.edit.choosephoto

import com.roxana.recipeapp.edit.PageType

data class EditRecipeChoosePhotoViewState(
    val photoPath: String? = null,
    val isExistingRecipe: Boolean = false,
    val navigation: Navigation? = null,
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Back : Navigation()
    data class ToPage(val page: PageType, val isExistingRecipe: Boolean) : Navigation()
    object PhotoCapture : Navigation()
}
