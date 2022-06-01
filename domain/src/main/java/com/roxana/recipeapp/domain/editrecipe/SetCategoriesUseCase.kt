package com.roxana.recipeapp.domain.editrecipe

import com.roxana.recipeapp.domain.RecipeCreationRepository
import com.roxana.recipeapp.domain.base.BaseSuspendableUseCase
import com.roxana.recipeapp.domain.model.CategoryType
import javax.inject.Inject

class SetCategoriesUseCase @Inject constructor(
    private val creationRepository: RecipeCreationRepository
) : BaseSuspendableUseCase<Set<CategoryType>, Unit>() {
    override suspend fun execute(input: Set<CategoryType>) {
        creationRepository.setCategories(input)
    }
}
