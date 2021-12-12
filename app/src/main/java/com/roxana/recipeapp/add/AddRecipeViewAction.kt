package com.roxana.recipeapp.add

import com.roxana.recipeapp.domain.CategoryType
import com.roxana.recipeapp.domain.QuantityType

sealed class AddRecipeViewAction

data class TitleChanged(val name: String) : AddRecipeViewAction()
object Back : AddRecipeViewAction()
data class CategoryClicked(val type: CategoryType) : AddRecipeViewAction()
data class PortionsChanged(val portions: String) : AddRecipeViewAction()
object AddIngredientClicked : AddRecipeViewAction()
data class IngredientNameChanged(val id: Int, val name: String) : AddRecipeViewAction()
data class IngredientQuantityChanged(val id: Int, val quantity: String) : AddRecipeViewAction()
data class IngredientQuantityTypeChanged(val id: Int, val quantityType: QuantityType?) :
    AddRecipeViewAction()
data class DeleteIngredientClicked(val id: Int) : AddRecipeViewAction()
data class IngredientClicked(val id: Int) : AddRecipeViewAction()
object AddInstructionClicked : AddRecipeViewAction()
data class InstructionChanged(val id: Int, val name: String) : AddRecipeViewAction()
data class DeleteInstructionClicked(val id: Int) : AddRecipeViewAction()
data class InstructionClicked(val id: Int) : AddRecipeViewAction()
