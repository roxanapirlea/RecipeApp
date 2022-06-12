package com.roxana.recipeapp.edit.time

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeTimeSideEffect

object ForwardForCreation : EditRecipeTimeSideEffect()
object ForwardForEditing : EditRecipeTimeSideEffect()
object Close : EditRecipeTimeSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeTimeSideEffect()
