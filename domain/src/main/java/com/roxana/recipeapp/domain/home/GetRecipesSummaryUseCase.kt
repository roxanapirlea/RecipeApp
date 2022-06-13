package com.roxana.recipeapp.domain.home

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import com.roxana.recipeapp.domain.model.CategoryType
import com.roxana.recipeapp.domain.model.RecipeSummary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetRecipesSummaryUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) : BaseFlowUseCase<GetRecipesSummaryUseCase.Input, List<RecipeSummary>>() {
    override fun execute(input: Input): Flow<List<RecipeSummary>> =
        recipeRepository.getRecipesSummary(
            input.query.trim(),
            input.totalTime,
            input.cookingTime,
            input.preparationTime,
            input.category
        ).flowOn(Dispatchers.IO)

    data class Input(
        val query: String = "",
        val totalTime: Short? = null,
        val cookingTime: Short? = null,
        val preparationTime: Short? = null,
        val category: CategoryType? = null
    )
}
