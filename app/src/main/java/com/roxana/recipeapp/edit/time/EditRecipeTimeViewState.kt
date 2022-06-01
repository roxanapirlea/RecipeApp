package com.roxana.recipeapp.edit.time

data class EditRecipeTimeViewState(
    val cooking: String = "",
    val preparation: String = "",
    val waiting: String = "",
    val total: String = ""
)

fun EditRecipeTimeViewState.isCookingValid() = cooking.isEmpty() || cooking.toShortOrNull() != null

fun EditRecipeTimeViewState.isPreparationValid() =
    preparation.isEmpty() || preparation.toShortOrNull() != null

fun EditRecipeTimeViewState.isWaitingValid() = waiting.isEmpty() || waiting.toShortOrNull() != null

fun EditRecipeTimeViewState.isTotalValid() = total.isEmpty() || total.toShortOrNull() != null

fun EditRecipeTimeViewState.isValid() =
    isCookingValid() && isPreparationValid() && isWaitingValid() && isTotalValid()
