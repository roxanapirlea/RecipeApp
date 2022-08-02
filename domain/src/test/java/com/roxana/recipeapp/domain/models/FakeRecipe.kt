package com.roxana.recipeapp.domain.models

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.Comment
import com.roxana.recipeapp.domain.model.Ingredient
import com.roxana.recipeapp.domain.model.Instruction
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Recipe
import com.roxana.recipeapp.domain.model.Temperature

val fakeRecipe = Recipe(
    id = 1234,
    name = "fake",
    photoPath = "photo",
    portions = 1,
    categories = listOf(CategoryType.BREAKFAST),
    instructions = listOf(Instruction(1, "instr 1")),
    ingredients = listOf(Ingredient(2, "ingr", 3.0, QuantityType.CUP)),
    timeTotal = 4,
    timePreparation = 5,
    timeCooking = 6,
    timeWaiting = 7,
    temperature = 8,
    temperatureUnit = Temperature.CELSIUS,
    comments = listOf(Comment(1, "comment"))
)
