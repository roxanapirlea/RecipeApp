package com.roxana.recipeapp.domain.detail

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.Recipe
import javax.inject.Inject

class GetRecipeByIdUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : BaseSuspendableUseCase<Int, Recipe>() {
    override suspend fun execute(input: Int): Recipe = recipeRepository.getRecipeById(input)
}
