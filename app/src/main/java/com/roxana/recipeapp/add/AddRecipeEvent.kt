package com.roxana.recipeapp.add

sealed class AddRecipeEvent
object ShowCategoryError : AddRecipeEvent()
object ShowQuantityError : AddRecipeEvent()