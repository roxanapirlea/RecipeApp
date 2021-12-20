package com.roxana.recipeapp.add

sealed class AddRecipeSideEffect
object ShowCategoryError : AddRecipeSideEffect()
object ShowQuantityError : AddRecipeSideEffect()
object SaveRecipeError : AddRecipeSideEffect()
object SaveRecipeSuccess : AddRecipeSideEffect()
