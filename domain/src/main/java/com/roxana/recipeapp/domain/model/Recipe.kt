package com.roxana.recipeapp.domain.model

data class Recipe(
    val id: Int,
    val name: String,
    val portions: Short?,
    val categories: List<CategoryType>,
    val ingredients: List<Ingredient>,
    val instructions: List<Instruction>,
    val comments: List<Comment>,
    val timeTotal: Short?,
    val timeCooking: Short?,
    val timeWaiting: Short?,
    val timePreparation: Short?,
    val temperature: Short?,
    val temperatureUnit: Temperature?
)

data class Ingredient(
    val id: Int,
    val name: String,
    val quantity: Double?,
    val quantityType: QuantityType?
)

data class Instruction(
    val ordinal: Short,
    val name: String
)

data class Comment(
    val ordinal: Short,
    val name: String
)
