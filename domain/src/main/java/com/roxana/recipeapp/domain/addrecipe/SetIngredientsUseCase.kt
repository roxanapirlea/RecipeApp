package com.roxana.recipeapp.domain.addrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.CreationIngredient
import javax.inject.Inject

class SetIngredientsUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseSuspendableUseCase<List<CreationIngredient>, Unit>() {
    override suspend fun execute(input: List<CreationIngredient>) {
        creationRepository.setIngredients(input)
    }
}
