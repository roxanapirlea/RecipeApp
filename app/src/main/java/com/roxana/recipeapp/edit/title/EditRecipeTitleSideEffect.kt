package com.roxana.recipeapp.edit.title

import com.roxana.recipeapp.edit.PageType

sealed class EditRecipeTitleSideEffect

object ForwardForCreation : EditRecipeTitleSideEffect()
object ForwardForEditing : EditRecipeTitleSideEffect()
object Back : EditRecipeTitleSideEffect()
data class NavigateToPage(val page: PageType) : EditRecipeTitleSideEffect()
object RevealBackdrop : EditRecipeTitleSideEffect()
