package com.roxana.recipeapp.add.time

data class AddRecipeTimeViewState(
    val cooking: String = "",
    val preparation: String = "",
    val waiting: String = "",
    val total: String = ""
)

fun AddRecipeTimeViewState.isCookingValid() = cooking.isEmpty() || cooking.toShortOrNull() != null

fun AddRecipeTimeViewState.isPreparationValid() =
    preparation.isEmpty() || preparation.toShortOrNull() != null

fun AddRecipeTimeViewState.isWaitingValid() = waiting.isEmpty() || waiting.toShortOrNull() != null

fun AddRecipeTimeViewState.isTotalValid() = total.isEmpty() || total.toShortOrNull() != null

fun AddRecipeTimeViewState.isValid() =
    isCookingValid() && isPreparationValid() && isWaitingValid() && isTotalValid()
