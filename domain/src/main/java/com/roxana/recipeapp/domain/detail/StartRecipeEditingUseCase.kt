package com.roxana.recipeapp.domain.detail

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.base.CommonDispatchers
import com.roxana.recipeapp.domain.model.CreationComment
import com.roxana.recipeapp.domain.model.CreationIngredient
import com.roxana.recipeapp.domain.model.CreationInstruction
import com.roxana.recipeapp.domain.model.CreationRecipe
import com.roxana.recipeapp.domain.model.Recipe
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StartRecipeEditingUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
    private val creationRepository: RecipeCreationRepository,
    private val dispatchers: CommonDispatchers
) : BaseSuspendableUseCase<Int, Unit>() {
    override suspend fun execute(input: Int) {
        val recipe = recipeRepository.getRecipeById(input)
        val creationRecipe = withContext(dispatchers.default) { recipe.toCreationRecipe() }
        creationRepository.setRecipe(creationRecipe)
    }

    private fun Recipe.toCreationRecipe(): CreationRecipe =
        CreationRecipe(
            id = id,
            name = name,
            photoPath = photoPath,
            portions = portions,
            categories = categories,
            instructions = instructions.map { CreationInstruction(it.name, it.ordinal) },
            ingredients = ingredients.map {
                CreationIngredient(it.id, it.name, it.quantity, it.quantityType)
            },
            timeTotal = timeTotal,
            timePreparation = timePreparation,
            timeCooking = timeCooking,
            timeWaiting = timeWaiting,
            temperature = temperature,
            temperatureUnit = temperatureUnit,
            comments = comments.map { CreationComment(it.name, it.ordinal) }
        )
}
