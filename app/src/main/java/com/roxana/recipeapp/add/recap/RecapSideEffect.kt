package com.roxana.recipeapp.add.recap

sealed class RecapSideEffect

object FetchingError : RecapSideEffect()
object SaveRecipeSuccess : RecapSideEffect()
object SaveRecipeError : RecapSideEffect()
