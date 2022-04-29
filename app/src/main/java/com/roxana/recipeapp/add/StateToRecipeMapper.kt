package com.roxana.recipeapp.add

import androidx.annotation.VisibleForTesting
import com.roxana.recipeapp.add.recap.RecapViewState
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.toDomainModel

fun AddRecipeViewState.toRecipe(): CreationRecipe {
    return CreationRecipe(
        name = title.text,
        photoPath = null,
        categories = categories.filter { it.isSelected }.map { it.type.toDomainModel() },
        portions = portions.value,
        instructions = instructions
            .filter { it.fieldState.text.isNotEmpty() }
            .mapIndexed { index, instr ->
                CreationInstruction(instr.fieldState.text, index.toShort())
            },
        ingredients = ingredients
            .filter { !it.isEmpty }
            .map { ingredient -> ingredient.toDomainModel() },
        timeTotal = time.total.value,
        timePreparation = time.preparation.value,
        timeCooking = time.cooking.value,
        timeWaiting = time.waiting.value,
        temperature = temperature.value,
        temperatureUnit = temperature.value?.let { temperatureUnit.toDomainModel() },
        comments = comments
            .filter { it.fieldState.text.isNotEmpty() }
            .mapIndexed { index, comm -> CreationComment(comm.fieldState.text, index.toShort()) }
    )
}

fun RecapViewState.toRecipe(): CreationRecipe {
    return CreationRecipe(
        name = title,
        photoPath = null,
        categories = categories.map { it.toDomainModel() },
        portions = portions,
        instructions = instructions
            .mapIndexed { index, instr ->
                CreationInstruction(instr, index.toShort())
            },
        ingredients = ingredients.map { ingredient ->
            val type =
                if (ingredient.quantity == null) UiQuantityType.None else ingredient.quantityType
            CreationIngredient(ingredient.name, ingredient.quantity, type.toDomainModel())
        },
        timeTotal = time.total,
        timePreparation = time.preparation,
        timeCooking = time.cooking,
        timeWaiting = time.waiting,
        temperature = temperature,
        temperatureUnit = temperature?.let { temperatureUnit?.toDomainModel() },
        comments = comments
            .mapIndexed { index, comm -> CreationComment(comm, index.toShort()) }
    )
}

@VisibleForTesting
fun IngredientState.toDomainModel(): CreationIngredient {
    val type = if (quantity.value == null) UiQuantityType.None else quantityType
    return CreationIngredient(name.text, quantity.value, type.toDomainModel())
}
