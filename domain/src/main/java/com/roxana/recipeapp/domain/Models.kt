package com.roxana.recipeapp.domain

data class Recipe(
    val name: String,
    val photoPath: String?,
    val categories: List<Category>,
    val instructions: List<Instruction>,
    val ingredients: List<Ingredient>,
    val timeTotal: Int?,
    val timePreparation: Int?,
    val timeCooking: Int?,
    val timeWaiting: Int?,
    val temperature: Int?,
    val comments: List<Comment>
)

data class Instruction(
    val name: String,
    val ordinal: Int
)

data class Ingredient(
    val id: Int?,
    val name: String,
    val quantity: Double,
    val quantityType: String
)

data class Category(
    val id: Int?,
    val name: String
)

data class Comment(
    val detail: String,
    val ordinal: Int
)
