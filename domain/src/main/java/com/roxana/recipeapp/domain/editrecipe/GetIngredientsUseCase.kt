package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseFlowUseCase
import com.roxana.recipeapp.domain.model.CreationIngredient
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetIngredientsUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseFlowUseCase<Any?, List<CreationIngredient>>() {
    override fun execute(input: Any?): Flow<List<CreationIngredient>> =
        creationRepository.getIngredients()
}
