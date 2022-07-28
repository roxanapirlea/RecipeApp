package com.roxana.recipeapp.edit.time

import com.roxana.recipeapp.edit.PageType

data class EditRecipeTimeViewState(
    val cooking: String = "",
    val preparation: String = "",
    val waiting: String = "",
    val total: String = "",
    val isExistingRecipe: Boolean = false,
    val navigation: Navigation? = null,
)

sealed class Navigation {
    object ForwardCreation : Navigation()
    object ForwardEditing : Navigation()
    object Back : Navigation()
    data class ToPage(val page: PageType, val isExistingRecipe: Boolean) : Navigation()
}

fun EditRecipeTimeViewState.isCookingValid() = cooking.isEmpty() || cooking.toShortOrNull() != null

fun EditRecipeTimeViewState.isPreparationValid() =
    preparation.isEmpty() || preparation.toShortOrNull() != null

fun EditRecipeTimeViewState.isWaitingValid() = waiting.isEmpty() || waiting.toShortOrNull() != null

fun EditRecipeTimeViewState.isTotalValid() = total.isEmpty() || total.toShortOrNull() != null

fun EditRecipeTimeViewState.isValid() =
    isCookingValid() && isPreparationValid() && isWaitingValid() && isTotalValid()
