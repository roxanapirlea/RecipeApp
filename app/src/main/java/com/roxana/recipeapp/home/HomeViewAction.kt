package com.roxana.recipeapp.home

sealed class HomeViewAction

object AddRecipe : HomeViewAction()
object Settings : HomeViewAction()
object Filters : HomeViewAction()
data class RecipeDetail(val id: Int) : HomeViewAction()
