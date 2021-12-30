package com.roxana.recipeapp.domain.detail

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import com.roxana.recipeapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipeByIdAsFlowUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : BaseFlowUseCase<Int, Recipe>() {
    override fun execute(input: Int): Flow<Recipe> =
        recipeRepository.getRecipeByIdAsFlow(input)
}
