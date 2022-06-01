package com.roxana.recipeapp.edit.recap

sealed class RecapSideEffect

object FetchingError : RecapSideEffect()
object SaveRecipeSuccess : RecapSideEffect()
object SaveRecipeError : RecapSideEffect()
