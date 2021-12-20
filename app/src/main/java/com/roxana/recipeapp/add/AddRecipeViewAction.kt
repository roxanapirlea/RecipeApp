package com.roxana.recipeapp.add

import com.roxana.recipeapp.uimodel.UiCategoryType
import com.roxana.recipeapp.uimodel.UiQuantityType

sealed class AddRecipeViewAction

data class TitleChanged(val name: String) : AddRecipeViewAction()
object Back : AddRecipeViewAction()
data class CategoryClicked(val type: UiCategoryType) : AddRecipeViewAction()
data class PortionsChanged(val portions: String) : AddRecipeViewAction()
object AddIngredientClicked : AddRecipeViewAction()
data class IngredientNameChanged(val id: Int, val name: String) : AddRecipeViewAction()
data class IngredientQuantityChanged(val id: Int, val quantity: String) : AddRecipeViewAction()
data class IngredientQuantityTypeChanged(val id: Int, val quantityType: UiQuantityType) :
    AddRecipeViewAction()
data class DeleteIngredientClicked(val id: Int) : AddRecipeViewAction()
data class IngredientClicked(val id: Int) : AddRecipeViewAction()
object AddInstructionClicked : AddRecipeViewAction()
data class InstructionChanged(val id: Int, val name: String) : AddRecipeViewAction()
data class DeleteInstructionClicked(val id: Int) : AddRecipeViewAction()
data class InstructionClicked(val id: Int) : AddRecipeViewAction()
object AddCommentClicked : AddRecipeViewAction()
data class CommentChanged(val id: Int, val name: String) : AddRecipeViewAction()
data class DeleteCommentClicked(val id: Int) : AddRecipeViewAction()
data class CommentClicked(val id: Int) : AddRecipeViewAction()
data class TimeCookingChanged(val time: String) : AddRecipeViewAction()
data class TimePreparationChanged(val time: String) : AddRecipeViewAction()
data class TimeWaitingChanged(val time: String) : AddRecipeViewAction()
data class TimeTotalChanged(val time: String) : AddRecipeViewAction()
object ComputeTotal : AddRecipeViewAction()
data class TemperatureChanged(val temperature: String) : AddRecipeViewAction()
object Validate : AddRecipeViewAction()
