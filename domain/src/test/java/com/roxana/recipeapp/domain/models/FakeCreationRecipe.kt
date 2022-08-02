package com.roxana.recipeapp.domain.models

import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.QuantityType
import com.roxana.recipeapp.domain.model.Temperature

val fakeCreationRecipe = CreationRecipe(
    id = null,
    name = "fake",
    photoPath = "photo",
    portions = 1,
    categories = listOf(CategoryType.BREAKFAST),
    instructions = listOf(CreationInstruction("instr 1", 1)),
    ingredients = listOf(CreationIngredient(2, "ingr", 3.0, QuantityType.CUP)),
    timeTotal = 4,
    timePreparation = 5,
    timeCooking = 6,
    timeWaiting = 7,
    temperature = 8,
    temperatureUnit = Temperature.CELSIUS,
    comments = listOf(CreationComment("comment", 1))
)
