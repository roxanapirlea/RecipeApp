package com.roxana.recipeapp.add.time

import com.roxana.recipeapp.add.PageType

sealed class AddRecipeTimeSideEffect

object Forward : AddRecipeTimeSideEffect()
object Back : AddRecipeTimeSideEffect()
data class NavigateToPage(val page: PageType) : AddRecipeTimeSideEffect()
