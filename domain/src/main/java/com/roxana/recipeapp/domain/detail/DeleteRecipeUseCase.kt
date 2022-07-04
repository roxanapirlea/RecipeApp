package com.roxana.recipeapp.domain.detail

import com.roxana.recipeapp.domain.RecipeRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import javax.inject.Inject

class DeleteRecipeUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository,
) : BaseSuspendableUseCase<Int, Unit>() {
    override suspend fun execute(input: Int) {
        recipeRepository.deleteById(input)
    }
}
