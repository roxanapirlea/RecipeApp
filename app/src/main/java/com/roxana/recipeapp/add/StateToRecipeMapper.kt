package com.roxana.recipeapp.add

import androidx.annotation.VisibleForTesting
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.CreationRecipe

fun AddRecipeViewState.toRecipe(): CreationRecipe {
    return CreationRecipe(
        name = title.text,
        photoPath = null,
        categories = categories.filter { it.isSelected }.map { it.type },
        portions = portions.value,
        instructions = instructions
            .filter { it.fieldState.text.isNotEmpty() }
            .mapIndexed { index, instr ->
                CreationInstruction(instr.fieldState.text, index.toShort())
            },
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
            .mapIndexed { index, comm -> CreationComment(comm.fieldState.text, index.toShort()) }
    )
}

@VisibleForTesting
fun IngredientState.toDomainModel(index: Int): CreationIngredient {
    val type = if (quantity.value == null) null else quantityType
    return CreationIngredient(
        index,
        name.text,
        quantity.value,
        type
    )
}
