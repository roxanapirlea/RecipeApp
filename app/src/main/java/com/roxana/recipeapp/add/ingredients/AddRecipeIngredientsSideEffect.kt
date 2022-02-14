package com.roxana.recipeapp.add.ingredients

import com.roxana.recipeapp.add.PageType

sealed class AddRecipeIngredientsSideEffect

object Forward : AddRecipeIngredientsSideEffect()
object Back : AddRecipeIngredientsSideEffect()
data class NavigateToPage(val page: PageType) : AddRecipeIngredientsSideEffect()
