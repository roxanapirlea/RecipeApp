package com.roxana.recipeapp.add.portions

import com.roxana.recipeapp.add.PageType

sealed class AddRecipePortionsSideEffect

object Forward : AddRecipePortionsSideEffect()
object Back : AddRecipePortionsSideEffect()
data class NavigateToPage(val page: PageType) : AddRecipePortionsSideEffect()
