package com.roxana.recipeapp.edit.comments

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeCommentsSideEffect

object ForwardForCreation : EditRecipeCommentsSideEffect()
object ForwardForEditing : EditRecipeCommentsSideEffect()
object Close : EditRecipeCommentsSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeCommentsSideEffect()
