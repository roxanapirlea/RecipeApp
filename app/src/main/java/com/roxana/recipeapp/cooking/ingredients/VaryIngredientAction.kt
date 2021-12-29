package com.roxana.recipeapp.cooking.ingredients

sealed class VaryIngredientAction

data class IngredientSelected(val id: Int?) : VaryIngredientAction()
data class IngredientQuantityChanged(val quantity: String) : VaryIngredientAction()
object Validate : VaryIngredientAction()
