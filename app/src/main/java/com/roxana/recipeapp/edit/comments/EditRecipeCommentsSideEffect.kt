package com.roxana.recipeapp.edit.comments

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeCommentsSideEffect

object ForwardForCreation : EditRecipeCommentsSideEffect()
object ForwardForEditing : EditRecipeCommentsSideEffect()
object Back : EditRecipeCommentsSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeCommentsSideEffect()
