package com.roxana.recipeapp.edit.instructions

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeInstructionsSideEffect

object Forward : EditRecipeInstructionsSideEffect()
object Back : EditRecipeInstructionsSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeInstructionsSideEffect()
