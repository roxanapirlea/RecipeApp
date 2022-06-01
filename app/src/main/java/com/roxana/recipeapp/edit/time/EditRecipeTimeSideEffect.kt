package com.roxana.recipeapp.edit.time

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeTimeSideEffect

object Forward : EditRecipeTimeSideEffect()
object Back : EditRecipeTimeSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeTimeSideEffect()
