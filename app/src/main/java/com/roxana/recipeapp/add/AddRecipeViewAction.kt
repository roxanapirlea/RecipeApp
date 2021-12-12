package com.roxana.recipeapp.add

sealed class AddRecipeViewAction

data class TitleChanged(val name: String) : AddRecipeViewAction()
object Back : AddRecipeViewAction()
