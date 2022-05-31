package com.roxana.recipeapp.add

import com.roxana.recipeapp.add.recap.RecapViewState
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.uimodel.UiQuantityType
import com.roxana.recipeapp.uimodel.toDomainModel

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
