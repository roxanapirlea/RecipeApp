package com.roxana.recipeapp.edit.title

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeTitleSideEffect

object Forward : EditRecipeTitleSideEffect()
object Back : EditRecipeTitleSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeTitleSideEffect()
object RevealBackdrop : EditRecipeTitleSideEffect()
