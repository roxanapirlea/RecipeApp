package com.roxana.recipeapp.edit.choosephoto

import com.roxana.recipeapp.edit.PageType
import com.roxana.recipeapp.uimodel.UiTemperature

data class EditRecipeChoosePhotoViewState(
    val photoPath: String? = null,
    val isExistingRecipe: Boolean = false,
    val showSaveDialog: Boolean = false,
    val navigation: Navigation? = null,
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Close : Navigation()
    data class ToPage(val page: PageType) : Navigation()
    object PhotoCapture: Navigation()
}
