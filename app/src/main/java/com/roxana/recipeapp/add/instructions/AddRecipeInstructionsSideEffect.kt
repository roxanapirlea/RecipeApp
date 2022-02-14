package com.roxana.recipeapp.add.instructions

import com.roxana.recipeapp.add.PageType

sealed class AddRecipeInstructionsSideEffect

object Forward : AddRecipeInstructionsSideEffect()
object Back : AddRecipeInstructionsSideEffect()
data class NavigateToPage(val page: PageType) : AddRecipeInstructionsSideEffect()
