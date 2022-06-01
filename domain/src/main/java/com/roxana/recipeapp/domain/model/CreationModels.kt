package com.roxana.recipeapp.domain.model

data class CreationRecipe(
    val id: Int?,
    val name: String,
    val photoPath: String?,
    val portions: Short?,
    val categories: List<CategoryType>,
    val instructions: List<CreationInstruction>,
    val ingredients: List<CreationIngredient>,
    val timeTotal: Short?,
    val timePreparation: Short?,
    val timeCooking: Short?,
    val timeWaiting: Short?,
    val temperature: Short?,
    val temperatureUnit: Temperature?,
    val comments: List<CreationComment>
)

data class CreationInstruction(
    val name: String,
    val ordinal: Short
)

data class CreationIngredient(
    val id: Int?,
    val name: String,
    val quantity: Double?,
    val quantityType: QuantityType?
)

data class CreationComment(
    val detail: String,
    val ordinal: Short
)
