package com.roxana.recipeapp.domain.home

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import com.roxana.recipeapp.domain.model.RecipeSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetRecipesSummaryUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : BaseFlowUseCase<Any?, List<RecipeSummary>>() {
    override fun execute(input: Any?): Flow<List<RecipeSummary>> =
        recipeRepository.getRecipesSummary()
            .flowOn(Dispatchers.IO)
}
