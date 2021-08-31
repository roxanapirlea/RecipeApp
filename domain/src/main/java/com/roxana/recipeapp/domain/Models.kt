package com.roxana.recipeapp.domain

data class Recipe(
    val name: String,
    val photoPath: String?,
    val portions: Short?,
    val categories: List<CategoryType>,
    val instructions: List<Instruction>,
    val ingredients: List<Ingredient>,
    val timeTotal: Short?,
    val timePreparation: Short?,
    val timeCooking: Short?,
    val timeWaiting: Short?,
    val temperature: Short?,
    val comments: List<Comment>
)

data class Instruction(
    val name: String,
    val ordinal: Short
)

data class Ingredient(
    val id: Int?,
    val name: String,
    val quantity: Double?,
    val quantityType: QuantityType?
)

data class Comment(
    val detail: String,
    val ordinal: Short
)

enum class CategoryType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK,
    MAIN,
    SIDE,
    DESSERT,
    DRINK
}

enum class QuantityType {
    POUND,
    OUNCE,
    GRAM,
    KILOGRAM,
    TEASPOON,
    TABLESPOON,
    FLUID_OUNCE,
    GILL,
    CUP,
    PINT,
    QUART,
    GALLON,
    LITER,
    DECILITER,
    CENTILITER
}
