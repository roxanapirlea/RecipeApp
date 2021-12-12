package com.roxana.recipeapp.add

import androidx.annotation.VisibleForTesting
import com.roxana.recipeapp.domain.Comment
import com.roxana.recipeapp.domain.Ingredient
import com.roxana.recipeapp.domain.Instruction
import com.roxana.recipeapp.domain.Recipe

fun AddRecipeViewState.toRecipe(): Recipe {
    return Recipe(
        name = title.text,
        photoPath = null,
        categories = categories.filter { it.isSelected }.map { it.type },
        portions = portions.value,
        instructions = instructions
            .filter { it.fieldState.text.isNotEmpty() }
            .mapIndexed { index, instr -> Instruction(instr.fieldState.text, index.toShort()) },
        ingredients = ingredients
            .filter { !it.isEmpty }
            .mapIndexed { index, ingredient -> ingredient.toDomainModel(index) },
        timeTotal = time.total.value,
        timePreparation = time.preparation.value,
        timeCooking = time.cooking.value,
        timeWaiting = time.waiting.value,
        temperature = temperature.value,
        comments = comments
            .filter { it.fieldState.text.isNotEmpty() }
            .mapIndexed { index, comm -> Comment(comm.fieldState.text, index.toShort()) }
    )
}

@VisibleForTesting
fun IngredientState.toDomainModel(index: Int): Ingredient {
    val type = if (quantity.value == null) null else quantityType
    return Ingredient(
        index,
        name.text,
        quantity.value,
        type
    )
}
